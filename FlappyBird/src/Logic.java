import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Logic implements ActionListener, KeyListener {
    int frame_w = 360, frame_h = 640;
    int player_start_pos_x = frame_w / 2;
    int player_start_pos_y = frame_h / 2;
    int player_w = 34, player_h = 24, gravity = 1;

    Timer game_time;
    View view;
    Image player_img;
    Player player;

    public Logic() {
        player_img = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(player_start_pos_x, player_start_pos_y, player_w, player_h, player_img);

        game_time = new Timer(1000 / 60, this);
        game_time.start();
    }

    public void setView(View v) {
        view = v;
    }

    public Player getPlayer() {
        return player;
    }

    public void move() {
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.clamp(player.getPosY(), 0, frame_h - player_h));
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