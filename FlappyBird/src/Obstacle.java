import java.awt.*;

public class Obstacle extends Object {
    private boolean is_passed;

    public Obstacle(int x, int y, int w, int h, Image img) {
        super(x, y, w, h, img);
        is_passed = false;
    }

    public void setIsPassed(boolean ip) {
        is_passed = ip;
    }
    public boolean getIsPassed() {
        return is_passed;
    }
}