import javax.swing.*;

// Objek background parallax, yaitu ilusi kedalaman dengan gambar 2D seolah-olah seperti 3D
public class ParallaxBackground extends Object {
     private int velocity_x;

    public ParallaxBackground(int x, int y, int w, int h, String img_n, int vx) {
        super(x, y, w, h);
        // Karena ParallaxBackground dipanggil dari View, maka pengambilan gambar dilakukan langsung disini
        // agar tidak perlu instansiasi dua kali, untuk gambar dan untuk ParallaxBackground, di kelas View
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
