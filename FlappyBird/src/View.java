import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    int width = 360, height = 640;

    private Logic logic;

    public View(Logic l) {
        logic = l;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);

        setFocusable(true);
        addKeyListener(logic);
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

        if(obss != null) {
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
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}