import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener {
    int frame_w = 360, frame_h = 640;
    int player_start_pos_x = frame_w / 2;
    int player_start_pos_y = frame_h / 2;
    int player_w = 34, player_h = 24, gravity = 1;

    int obs_start_pos_x = frame_w;
    int obs_start_pos_y = 0;
    int obs_w = 64, obs_h = 512, obs_velocity_x = -2;

    Timer game_time, obs_cd; // game_time untuk game loop, obs_cd untuk cooldown obstacle
    View view;
    Image player_img, lower_obs_img, upper_obs_img;
    Player player;
    ArrayList<Obstacle> obss;

    public Logic() {
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
        obs_cd.start();

        game_time = new Timer(1000 / 60, this);
        game_time.start();
    }

    public void setView(View v) {
        view = v;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        move();

        if(view != null) {
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
        }
    }
    public void keyReleased(KeyEvent e) {}
}