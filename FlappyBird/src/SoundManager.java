import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

// Kelas yang mengatur BGM dan SFX
public class SoundManager {
    // Metode dibuat statis agar mudah dipanggil langsung dengan nama kelas tanpa instansiasi
    public static void playSound(String file_name) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("src/assets/sfxs/" + file_name));
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Antara SFX dan BGM dibedakan metode-nya karena letak direktori yang berbeda sehingga mempersingkat parameter yang
    // dimasukkan, juga karena pada SFX, suara langsung dibunyikan sedangkan BGM, jalannya bunyi diatur pada Logic
    public static Clip getMusic(String file_name) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("src/assets/bgms/" + file_name));
            clip.open(inputStream);

            return clip;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }
}