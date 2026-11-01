import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Kelas untuk mengatur logika permainan, seperti collision dan score
public class Logic implements ActionListener, KeyListener {
    // GAME STATE:
    // -2 => Menabrak penghalang, -1 => Tampilan game over, 0 => Menu, 1 => Bermain, 2 => Tutorial/Pemanasan
    int frame_w = 360, frame_h = 640, score = 0, game_state = 0;
    int player_start_pos_x = frame_w / 2;
    int player_start_pos_y = frame_h / 2;
    int player_w = 34, player_h = 24, gravity = 0;

    int obs_start_pos_x = frame_w;
    int obs_start_pos_y = 0;
    int obs_w = 64, obs_h = 512, obs_velocity_x = -2;

    // indeks berikut digunakan dalam animasi, karena button pada menu dan teks menggunakan animasi dengan model spritesheet
    int text_idx = 0, menu_idx = 0;

    // game_time untuk game loop, obs_cd untuk cooldown obstacle, text_cd untuk animasi teks, delay untuk transisi state
    Timer game_time, obs_cd, text_cd, delay;
    View view;
    Image logo_img, guide_img, player_img, lower_obs_img, upper_obs_img;
    // BufferedImage memudahkan pemotongan spritesheet
    BufferedImage button_start, button_exit, text_jump, text_restart;
    Player player;
    ArrayList<Obstacle> obss;
    // BGM menu dan saat bermain dibuat berbeda
    Clip menu_bgm, playing_bgm;

    public Logic() {
        // Load BGM
        menu_bgm = SoundManager.getMusic("menu.wav");
        playing_bgm = SoundManager.getMusic("playing.wav");

        // Load gambar yang berupa spritesheet
        try {
            text_jump = ImageIO.read(new File("src/assets/text - Jump.png"));
            text_restart = ImageIO.read(new File("src/assets/text - Restart.png"));
            button_start = ImageIO.read(new File("src/assets/button - Start.png"));
            button_exit = ImageIO.read(new File("src/assets/button - Exit.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }

        // timer untuk transisi dari menu ke awal permainan, dan dari saat menabrak obstacle ke layar game over
        delay = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(game_state == 0) {
                    game_state = 2;
                    setScore(0);
                    text_cd.start();
                } else if(game_state == -2) {
                    game_state = -1;
                }
            }
        });
        delay.setRepeats(false); // karena berupa delay, buat tidak berulang

        // timer untuk animasi teks
        text_cd = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text_idx = (text_idx + 1) % 2;
            }
        });

        logo_img = new ImageIcon(getClass().getResource("assets/logo.png")).getImage();
        guide_img = new ImageIcon(getClass().getResource("assets/text - Guide.png")).getImage();
        player_img = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(player_start_pos_x, player_start_pos_y, player_w, player_h, player_img);

        lower_obs_img = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upper_obs_img = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        obss = new ArrayList<Obstacle>();

        // timer untuk menambah obstacle tiap 1,5 detik sekali
        obs_cd = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Obstacle");
                placeObss();
            }
        });

        // timer untuk frame atau tick, dibuat 60 FPS
        game_time = new Timer(1000 / 60, this);
        game_time.start();

        // metode loop() mengatur loop dan langsung memutar suara
        menu_bgm.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setView(View v) {
        view = v;
    }

    // Ubah skor di kelas ini sekaligus update teks skor pada kelas View
    public void setScore(int s) {
        score = s;
        view.updateScore(score);
    }

    public Player getPlayer() {
        return player;
    }

    // Metode untuk menghitung letak gap pada obstacle
    public void placeObss() {
        int random_y = (int) (obs_start_pos_y - (obs_h / 4) - Math.random() * (obs_h / 2));
        int gap = frame_h / 4;

        Obstacle upper = new Obstacle(obs_start_pos_x, random_y, obs_w, obs_h, upper_obs_img);
        obss.add(upper);
        Obstacle lower = new Obstacle(obs_start_pos_x, random_y + gap + obs_h, obs_w, obs_h, lower_obs_img);
        obss.add(lower);
    }
    public ArrayList<Obstacle> getObss() {
        return obss;
    }

    // Metode untuk menggerakan pemain (termasuk implementasi gravitasi) dan obstacle
    public void move() {
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.clamp(player.getPosY(), 0, frame_h - player_h));

        for(int i = 0; i < obss.size(); i++) {
            Obstacle obs = obss.get(i);

            obs.setPosX((obs.getPosX() + obs_velocity_x));
        }
    }

    // metode untuk menghitung coliision, apakah dia menyentuh tanah atau menabrak obstacle
    public boolean isColliding() {
        if(player.getPosY() + player.getHeight() >= frame_h) {
            return true;
        }

        // alasan kenapa di cek hanya 4 pertama, cek pada README.md
        for(int i = 0; i < Math.min(obss.size(), 4); i++) {
            Obstacle cur = obss.get(i);
            int end_x = player.getPosX() + player.getWidth();
            int end_y = player.getPosY() + player.getHeight();

            if(
                !cur.getIsPassed() &&
                (end_x >= cur.getPosX() + 7 && player.getPosX() < cur.getPosX() + cur.getWidth()) &&
                (end_y >= cur.getPosY() && player.getPosY() < cur.getPosY() + cur.getHeight())
            ) {
                // jika benar menabrak obstacle
                return true;
            }
            if(!cur.getIsPassed() && player.getPosX() >= cur.getPosX() + cur.getWidth()) {
                // jika obstacle sudah dilewati
                cur.setIsPassed(true);
                if(i % 2 == 0) setScore(score + 1);
            }
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(game_state == 1) {
            // Hapus sepasang obstacle pertama jika sudah tidak terlihat pada layar
            if(!obss.isEmpty()) {
                Obstacle del_target = obss.getFirst();

                if(del_target != null && del_target.getPosX() < -del_target.getWidth()) {
                    obss.remove(del_target);
                }
            }

            move();

            if(isColliding()) {
                // Jika terdeteksi menabrak, maka hentikan permainan dan ubah state ke -2
                obs_cd.stop();
                game_state = -2;
                delay.start();
                // play juga SFX menabrak
                SoundManager.playSound("dead.wav");
                playing_bgm.stop();
                playing_bgm.setFramePosition(0);
            }
        } else if(game_state == -1) {
            text_cd.start();
        }

        if(view != null) {
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        if(game_state == 0) {
            // Enter untuk memilih opsi button
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                // idx 1 berarti tombol START, idx 2 berarti tombol EXIT
                if(menu_idx == 1 || menu_idx == 2) {
                    menu_bgm.stop();
                    menu_bgm.setFramePosition(0);

                    SoundManager.playSound("menu - Select.wav");

                    if (menu_idx == 1) {
                        menu_idx = 11;
                        delay.start();
                    } else {
                        menu_idx = 22;
                        System.exit(0);
                    }

                    return;
                }
            }
            // geser pilihan tombol ke bawah
            if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                if(menu_idx == 1) {
                    menu_idx = 2;
                } else {
                    menu_idx = 1;
                }

                SoundManager.playSound("menu - Move.wav");
            }
            // geser pilihan tombol ke atas
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                if(menu_idx == 2) {
                    menu_idx = 1;
                } else {
                    menu_idx = 2;
                }

                SoundManager.playSound("menu - Move.wav");
            }
        } else if(game_state == 1 || game_state == 2) {
            // tekan spasi untuk terbang
            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                SoundManager.playSound("jump.wav");
                player.setVelocityY(-10);

                if(game_state == 2) {
                    text_cd.stop();
                    gravity = 1;
                    game_state = 1;
                    obs_cd.start();
                    playing_bgm.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        } else if(game_state == -1) {
            // tekan R untuk mengulang permainan saat game over
            if(e.getKeyCode() == KeyEvent.VK_R) {
                obss.clear();
                player.setVelocityY(0);
                player.setPosY(player_start_pos_y);
                gravity = 0;
                setScore(0);
                game_state = 2;
            }
        }
    }
    public void keyReleased(KeyEvent e) {}
}