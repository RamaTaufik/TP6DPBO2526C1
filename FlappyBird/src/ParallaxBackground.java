import javax.swing.*;
import java.awt.*;

public class ParallaxBackground extends Object {
     private int velocity_x;

    public ParallaxBackground(int x, int y, int w, int h, String img_n, int vx) {
        super(x, y, w, h);
        setImage(new ImageIcon(getClass().getResource("assets/" + img_n)).getImage());
        velocity_x = vx;
    }

    public void setVelocityX(int vx) {
        velocity_x = vx;
    }
    public int getVelocityX() {
        return velocity_x;
    }
}
