import java.awt.*;

// Kelas ini merupakan kelas yang dipakai untuk instansiasi pemain
public class Player extends Object {
    // Karena dalam Flappy Bird pemain hanya bergerak ke atas dan ke bawah, maka hanya diperlukan velocity_y saja
    // untuk menghitung pergerakan vertikal yang smooth
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