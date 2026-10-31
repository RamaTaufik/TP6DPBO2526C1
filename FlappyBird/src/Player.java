import java.awt.*;

public class Player extends Object {
    private int velocity_y;

    public Player(int x, int y, int w, int h, Image img) {
        super(x, y, w, h, img);
        velocity_y = 0;
    }

    public void setVelocityY(int vy) {
        velocity_y = vy;
    }
    public int getVelocityY() {
        return velocity_y;
    }
}