import java.awt.*;

public class Player {
    private int pos_x, pos_y, width, height, velocity_y;
    private Image image;

    public Player(int x, int y, int w, int h, Image img) {
        pos_x = x;
        pos_y = y;
        width = w;
        height = h;
        image = img;
        velocity_y = 0;
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

    public void setVelocityY(int vy) {
        velocity_y = vy;
    }
    public int getVelocityY() {
        return velocity_y;
    }
}