import view.SplashScreen;

import javax.swing.*;

///**
// * Main - Entry point aplikasi
// * Sistem Catatan Keuangan Pribadi
// *
// * @author Sistem Keuangan
// * @version 1.0
// */
public class Main {

    public static void main(String[] args) {
        // Gunakan Look and Feel sistem agar tampilan lebih native
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Gunakan default jika gagal
        }

        // Jalankan di Event Dispatch Thread (Swing best practice)
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.tampilkan();
        });
    }
}
