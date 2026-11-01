import java.awt.*;

// Kelas parent untuk objek-objek yang muncul in-game
public class Object {
    // Posisi koordinat, lebar, tinggi
    private int pos_x, pos_y, width, height;
    private Image image;

    public Object(int x, int y, int w, int h) {
        // Konstruktor ini dibuat khusus untuk ParallaxBackground, agar mempermudah pengambilan gambar dari kelas View
        pos_x = x;
        pos_y = y;
        width = w;
        height = h;
    }
    public Object(int x, int y, int w, int h, Image img) {
        pos_x = x;
        pos_y = y;
        width = w;
        height = h;
        image = img;
    }

    public void setPosX(int x) {
        pos_x = x;
    }
    public int getPosX() {
        return pos_x;
    }

    public void setPosY(int y) {
        pos_y = y;
    }
    public int getPosY() {
        return pos_y;
    }

    public void setWidth(int w) {
        width = w;
    }
    public int getWidth() {
        return width;
    }

    public void setHeight(int h) {
        height = h;
    }
    public int getHeight() {
        return height;
    }

    public void setImage(Image img) {
        image = img;
    }
    public Image getImage() {
        return image;
    }
}