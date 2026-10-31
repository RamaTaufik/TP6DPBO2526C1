import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class View extends JPanel {
    int width = 360, height = 640;

    private Logic logic;
    JLabel score_label;

    public View(Logic l) {
        logic = l;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);

        setFocusable(true);
        addKeyListener(logic);

        score_label = new JLabel("Score: 0");
        score_label.setFont(new Font("Monospaced", Font.BOLD, 20));
        score_label.setForeground(Color.WHITE);
        // score_label.setBounds(50, 30, 150, 30);
        this.add(score_label);
    }

    public void updateScore(int score) {
        score_label.setText("Score : " + score);
    }

    public void draw(Graphics g) {
        Player player = logic.getPlayer();
        ArrayList<Obstacle> obss = logic.getObss();

        if(player != null) {
            g.drawImage(
                player.getImage(),
                player.getPosX(),
                player.getPosY(),
                player.getWidth(),
                player.getHeight(),
                null
            );
        }

        if(!obss.isEmpty()) {
            for(int i = 0; i < obss.size(); i++) {
                Obstacle obs = obss.get(i);

                g.drawImage(
                    obs.getImage(),
                    obs.getPosX(),
                    obs.getPosY(),
                    obs.getWidth(),
                    obs.getHeight(),
                    null
                );
            }
        }

        if(logic.game_state == -1 ) {
            score_label.setText("");

            g.setColor(new Color(0, 0, 0, 128));
            g.fillRect(0, 0, width, height);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 30));
            g.drawString("Final Score:" + logic.score, 30, height / 2);
        }

        if(logic.text_jump != null && logic.game_state == 2) {
            BufferedImage t_j = logic.text_jump;

            g.drawImage(
                t_j.getSubimage(0, t_j.getHeight() / 2 * logic.text_idx, t_j.getWidth(), t_j.getHeight() / 2),
                0,
                (height / 3) * 2,
                width,
                (int)((float)(width / t_j.getWidth()) * t_j.getHeight()),
                null
            );
        } else if(logic.text_restart != null && logic.game_state == -1) {
            BufferedImage t_r = logic.text_restart;

            g.drawImage(
                t_r.getSubimage(0, t_r.getHeight() / 2 * logic.text_idx, t_r.getWidth(), t_r.getHeight() / 2),
                0,
                (height / 3) * 2,
                width,
                (int) ((float) (width / t_r.getWidth()) * t_r.getHeight()),
                null
            );
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}