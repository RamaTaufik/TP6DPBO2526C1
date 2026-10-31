import java.awt.*;

public class Obstacle extends Object {
    private int velocity_x;
    private boolean is_passed;

    public Obstacle(int x, int y, int w, int h, Image img) {
        super(x, y, w, h, img);
        velocity_x = 0;
        is_passed = false;
    }
}