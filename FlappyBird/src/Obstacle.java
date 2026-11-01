import java.awt.*;

// Kelas ini digunakan untuk membuat objek penghalang
public class Obstacle extends Object {
    // Atribut is_passed merupakan flag apakah penghalang sudah dilewati atau belum
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