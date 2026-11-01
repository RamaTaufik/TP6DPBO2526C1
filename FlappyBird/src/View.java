import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class View extends JPanel {
    // opacity digunakan untuk transisi dari menu ke layar bermain, sedangkan game_tick digunakan untuk menghitung frame
    // sehingga pergerakan background parallax dapat lebih mudah diatur
    int width = 360, height = 640, opacity = 0, game_tick = 0;

    private Logic logic;
    JLabel score_label;
    ArrayList<ParallaxBackground> p_bgs;

    public View(Logic l) {
        logic = l;
        p_bgs = new ArrayList<ParallaxBackground>();

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);
        // Instansiasi tiap layer dari background parallax. Dapat dilihat vx (velocity_x) nya berbeda-beda. Ini merupakan
        // kecepatan pergerakan background, dimana semakin jauh jarak background, dari layar perkiraannya, maka akan
        // semakin lambat pergerakannya
        p_bgs.add(new ParallaxBackground(width - 540, 0, 540, 640, "background/parallax - 01.png", 0));
        p_bgs.add(new ParallaxBackground(0, 0, 540, 640, "background/parallax - 02.png", -1));
        p_bgs.add(new ParallaxBackground(0, 0, 540, 640, "background/parallax - 03.png", -1));
        p_bgs.add(new ParallaxBackground(0, 0, 540, 640, "background/parallax - 04.png", -3));
        // Layer paling depan ini bergerak paling cepat namun diberi -1 karena bergerak tiap frame, sedangkan layer lain
        // dibuat bergerak tiap 5 frame
        p_bgs.add(new ParallaxBackground(0, 0, 540, 640, "background/parallax - 05.png", -1));

        setFocusable(true);
        addKeyListener(logic);

        // Di awal-awal menu, label skor tidak dulu ditampilkan sehingga dibuat teks-nya menjadi "" agar tidak terlihat
        score_label = new JLabel("");
        score_label.setFont(new Font("Monospaced", Font.BOLD, 20));
        score_label.setForeground(Color.BLACK);
        this.add(score_label);
    }

    // Fungsi agar dapat meng-update teks label dari kelas Logic
    public void updateScore(int score) {
        score_label.setText("Score : " + score);
    }

    public void draw(Graphics g) {
        for (int i = 0; i < p_bgs.size(); i++) {
            // For loop untuk menggambar background parallax
            ParallaxBackground cur = p_bgs.get(i);

            // Background normalnya akan bergerak setiap 5 frame/tick, namun khusus untuk background layer paling depan,
            // pergerakkan tiap 5 frame jadi terlihat choppy dan membuat pusing, makanya diubah agar bergerak tiap frame
            if (logic.game_state == 1 && (i == p_bgs.size() - 1 || game_tick % 5 == 0)) {
                cur.setPosX(cur.getPosX() + cur.getVelocityX());

                // Ketika background sudah berada di luar layar, maka tarik lagi background ke depan
                if (cur.getPosX() + cur.getWidth() < 0) {
                    cur.setPosX(cur.getPosX() + cur.getWidth());
                }
            }

            // Agar selalu terlihat kontinu, maka gambar tiap layar dua kali berturut-turut, kecuali untuk layer pertama
            // karena layer pertama tidak bergerak (vx = 0)
            g.drawImage(cur.getImage(), cur.getPosX(), cur.getPosY(), cur.getWidth(), cur.getHeight(), null);
            if(i != 0) g.drawImage(cur.getImage(), cur.getPosX() + cur.getWidth(), cur.getPosY(), cur.getWidth(), cur.getHeight(), null);
        }

        // Gambar tampilan menu
        if(logic.game_state == 0) {
            // Penggunaan BufferedImage dikarenakan gambar button merupakan spritesheet, sehingga perlu dipotong agar
            // memudahkan animasi
            BufferedImage b_s = logic.button_start, b_e = logic.button_exit;
            int idx = logic.menu_idx,is_b_s = idx == 1? 10: 0, is_b_e = idx == 2? 10: 0;

            // Gambar logo
            g.drawImage(
                logic.logo_img,
                width / 6,
                20,
                (width / 3) * 2,
                116,
                null
            );
            // Gambar tip / guide tombol
            g.drawImage(
                logic.guide_img,
                0,
                height - 174,
                width,
                174,
                null
            );
            // Gambar tombol START. getSubImage() digunakan untuk memotong sprite yang diperlukan
            g.drawImage(
                b_s.getSubimage(0, b_s.getHeight() / 2 * (idx == 11? 1: 0), b_s.getWidth(), b_s.getHeight() / 2),
                -is_b_s,
                (height / 3) - is_b_s,
                width + (is_b_s * 2),
                (int)((float)(width / b_s.getWidth()) * b_s.getHeight()) + (is_b_s * 2),
                null
            );
            // Gambar tombol EXIT
            g.drawImage(
                b_e.getSubimage(0, b_e.getHeight() / 2 * (idx == 22? 1: 0), b_e.getWidth(), b_e.getHeight() / 2),
                -is_b_e,
                (height / 3) + ((int)((float)(width / b_s.getWidth()) * b_s.getHeight()) / 3 * 2) - is_b_e,
                width + (is_b_e * 2),
                (int)((float)(width / b_e.getWidth()) * b_e.getHeight()) + (is_b_e * 2),
                null
            );

            // idx / menu_idx 11 berarti pemain memilih tombol STARt, maka mulai transisi layar hitam dari tampilan menu
            // ke layar permainan
            if(idx == 11) {
                opacity = Math.min(255, opacity + 7);
                g.setColor(new Color(0, 0, 0, opacity));
                g.fillRect(0, 0, width, height);
            }

            return; // Pada tampilan menu, tidak usah gambar player, skor, atau obstacle sehingga langsung return
        }

        Player player = logic.getPlayer();
        ArrayList<Obstacle> obss = logic.getObss();

        // Gambar pemain
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

        // Gambar tiap obstacle
        if(!obss.isEmpty()) {
            for(int i = 0; i < obss.size(); i++) {
                Obstacle obs = obss.get(i);

                g.drawImage(
                    obs.getImage(),
                    obs.getPosX(),
                    obs.getPosY(),
                    obs.getWidth(),
                    obs.getHeight(),
                    null
                );
            }
        }

        // game_state -1 berarti game over, maka gelapkan layar dengan fillRect() dan tampilkan skor akhir pemain
        if(logic.game_state == -1 ) {
            score_label.setText("");

            g.setColor(new Color(0, 0, 0, 128));
            g.fillRect(0, 0, width, height);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 30));
            g.drawString("Final Score:", 30, (height / 2) - 65);
            g.setFont(new Font("Monospaced", Font.BOLD, 65));
            g.drawString("" + logic.score, 30, height / 2);
        }

        // game_state 2 berarti awal permainan, maka tampilkan gambar hint untuk menekan spasi untuk terbang
        if(logic.text_jump != null && logic.game_state == 2) {
            BufferedImage t_j = logic.text_jump;

            g.drawImage(
                t_j.getSubimage(0, t_j.getHeight() / 2 * logic.text_idx, t_j.getWidth(), t_j.getHeight() / 2),
                0,
                (height / 3) * 2,
                width,
                (int)((float)(width / t_j.getWidth()) * t_j.getHeight()),
                null
            );
        } else if(logic.text_restart != null && logic.game_state == -1) {
            // atau jika game over, tampilkan gambar hint untuk menekan R untuk mengulang
            BufferedImage t_r = logic.text_restart;

            g.drawImage(
                t_r.getSubimage(0, t_r.getHeight() / 2 * logic.text_idx, t_r.getWidth(), t_r.getHeight() / 2),
                0,
                (height / 3) * 2,
                width,
                (int) ((float) (width / t_r.getWidth()) * t_r.getHeight()),
                null
            );
        }

        // hitung game_tick, di modulus 10 agar tidak semakin besar walaupun sedikit redundan karena perlu waktu yang sangat
        // lama hingga terjadi overflow, meskipun dengan 60 FPS sekaligus
        game_tick = (game_tick + 1) % 10;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}