package view;

import database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;

// SplashScreen - Layar pembuka aplikasi

public class SplashScreen extends JWindow {

    private JProgressBar progressBar;
    private JLabel lblStatus;

    public SplashScreen() {
        initUI();
    }

    private void initUI() {
        setSize(520, 300);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 23, 42));

        setLayout(new BorderLayout());

        // ---- Panel Utama ----
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(15, 23, 42));
        main.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;

        JLabel ikon = new JLabel("💰", SwingConstants.CENTER);
        ikon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        gbc.gridy = 0;
        main.add(ikon, gbc);

        JLabel judul = new JLabel("Sistem Catatan Keuangan Pribadi", SwingConstants.CENTER);
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(8, 0, 4, 0);
        main.add(judul, gbc);

        JLabel sub = new JLabel("Kelola keuangan Anda dengan mudah", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(148, 163, 184));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        main.add(sub, gbc);

        progressBar = new JProgressBar(0, 100);
        progressBar.setForeground(new Color(99, 102, 241));
        progressBar.setBackground(new Color(30, 41, 59));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 6));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 8, 0);
        main.add(progressBar, gbc);

        lblStatus = new JLabel("Memulai aplikasi...", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(100, 116, 139));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        main.add(lblStatus, gbc);

        add(main, BorderLayout.CENTER);

        // Border tipis
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(99, 102, 241), 2));
    }

    public void tampilkan() {
        setVisible(true);

        // Simulasi loading dengan SwingWorker
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish(10);
                Thread.sleep(300);
                lblStatus.setText("Memuat driver database...");
                publish(30);
                Thread.sleep(300);
                lblStatus.setText("Menghubungkan ke MySQL...");
                DatabaseConnection.getInstance(); // inisialisasi DB
                publish(70);
                Thread.sleep(300);
                lblStatus.setText("Memuat antarmuka...");
                publish(90);
                Thread.sleep(400);
                lblStatus.setText("Siap!");
                publish(100);
                Thread.sleep(300);
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                progressBar.setValue(chunks.get(chunks.size() - 1));
            }

            @Override
            protected void done() {
                dispose();
                new MainView(); // Buka window utama
            }
        };

        worker.execute();
    }
}
