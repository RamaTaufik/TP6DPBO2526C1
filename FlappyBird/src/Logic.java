import javax.imageio.ImageIO;
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

public class Logic implements ActionListener, KeyListener {
    int frame_w = 360, frame_h = 640, score = 0, game_state = 2; // -1 => Game over, 0 => Menu, 1 => Playing, 2 => Tutorial
    int player_start_pos_x = frame_w / 2;
    int player_start_pos_y = frame_h / 2;
    int player_w = 34, player_h = 24, gravity = 0;

    int obs_start_pos_x = frame_w;
    int obs_start_pos_y = 0;
    int obs_w = 64, obs_h = 512, obs_velocity_x = -2;

    int text_idx = 0;

    Timer game_time, obs_cd, text_cd; // game_time untuk game loop, obs_cd untuk cooldown obstacle, text_cd untuk animasi teks
    View view;
    Image player_img, lower_obs_img, upper_obs_img;
    BufferedImage text_jump, text_restart;
    Player player;
    ArrayList<Obstacle> obss;

    public Logic() {
        try {
            text_jump = ImageIO.read(new File("src/assets/text - Jump.png"));
            text_restart = ImageIO.read(new File("src/assets/text - Restart.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }

        text_cd = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text_idx = (text_idx + 1) % 2;
            }
        });

        player_img = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(player_start_pos_x, player_start_pos_y, player_w, player_h, player_img);

        lower_obs_img = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upper_obs_img = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        obss = new ArrayList<Obstacle>();

        obs_cd = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Obstacle");
                placeObss();
            }
        });

        game_time = new Timer(1000 / 60, this);
        game_time.start();
        text_cd.start();
    }

    public void setView(View v) {
        view = v;
    }

    public void setScore(int s) {
        score = s;
        view.updateScore(score);
    }

    public Player getPlayer() {
        return player;
    }

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

    public void move() {
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.clamp(player.getPosY(), 0, frame_h - player_h));

        for(int i = 0; i < obss.size(); i++) {
            Obstacle obs = obss.get(i);

            obs.setPosX((obs.getPosX() + obs_velocity_x));
        }
    }

    public boolean isColliding() {
        for(int i = 0; i < Math.min(obss.size(), 4); i++) {
            Obstacle cur = obss.get(i);
            int end_x = player.getPosX() + player.getWidth();
            int end_y = player.getPosY() + player.getHeight();

            if(
                !cur.getIsPassed() &&
                (end_x >= cur.getPosX() + 7 && player.getPosX() < cur.getPosX() + cur.getWidth()) &&
                (end_y >= cur.getPosY() && player.getPosY() < cur.getPosY() + cur.getHeight())
            ) {
                return true;
            }
            if(!cur.getIsPassed() && player.getPosX() >= cur.getPosX()) {
                cur.setIsPassed(true);
                setScore(score + 5);
            }
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(game_state == 2) {
            // chwck
        } else if(game_state == 1) {
            // Hapus sepasang obstacle pertama jika sudah tidak terlihat pada layar
            if(!obss.isEmpty()) {
                Obstacle del_target = obss.getFirst();

                if(del_target != null && del_target.getPosX() < -del_target.getWidth()) {
                    obss.remove(del_target);
                }
            }

            move();

            if(isColliding()) {
                obs_cd.stop();
                game_state = -1;
            }
        } else if(game_state == 0) {} else if(game_state == -1) {
            text_cd.start();
        }

        if(view != null) {
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        if(game_state == 1 || game_state == 2) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                player.setVelocityY(-10);

                if(game_state == 2) {
                    text_cd.stop();
                    gravity = 1;
                    game_state = 1;
                    obs_cd.start();
                }
            }
        } else if(game_state == -1) {
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