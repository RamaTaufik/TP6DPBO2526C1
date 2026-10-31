import javax.swing.*;
import java.awt.*;

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
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}