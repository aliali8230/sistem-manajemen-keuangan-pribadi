package view;

import controller.TransaksiController;
import model.Transaksi;
import util.FormatUtil;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainView extends JFrame {

    // ===== Palet Warna =====
    private static final Color HIJAU      = new Color(22, 101, 52);
    private static final Color MERAH      = new Color(153, 27, 27);
    private static final Color BIRU       = new Color(30, 64, 175);
    private static final Color UNGU       = new Color(79, 70, 229);
    private static final Color BG_UTAMA   = new Color(245, 246, 250);
    private static final Color BG_SIDEBAR = new Color(30, 41, 59);
    private static final Color BG_KARTU   = new Color(41, 55, 79);
    private static final Color BORDER_ABU = new Color(209, 213, 219);

    // ===== Font =====
    private static final Font FONT_JUDUL = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BOLD  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_ANGKA = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_KECIL = new Font("Segoe UI", Font.PLAIN, 11);

    // ===== Controller =====
    private final TransaksiController controller;

    // ===== Komponen UI =====
    private JTable tabelTransaksi;
    private DefaultTableModel tableModel;
    private JLabel lblSaldo, lblPemasukan, lblPengeluaran;
    private JTextField txtTanggal, txtKeterangan, txtJumlah, txtCari;
    private JComboBox<String> cmbKategori, cmbJenis;
    private JButton btnHapus, btnEdit;

    private int selectedId = -1;

    // ===== KONSTRUKTOR =====
    public MainView() {
        this.controller = new TransaksiController();
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Catatan Keuangan Pribadi");
        setSize(1150, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_UTAMA);

        add(buatHeader(), BorderLayout.NORTH);
        add(buatSidebar(), BorderLayout.WEST);
        add(buatKontenUtama(), BorderLayout.CENTER);

        setVisible(true);
    }

    // ===== HEADER =====
    private JPanel buatHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SIDEBAR);
        header.setPreferredSize(new Dimension(0, 55));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblJudul = new JLabel("Keuangan Pribadi");
        lblJudul.setFont(FONT_JUDUL);
        lblJudul.setForeground(Color.WHITE);

        String tanggal = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy",
                        new java.util.Locale("id", "ID")));
        JLabel lblTanggal = new JLabel(tanggal);
        lblTanggal.setFont(FONT_KECIL);
        lblTanggal.setForeground(Color.WHITE);

        JPanel kiri = new JPanel(new GridLayout(2, 1));
        kiri.setOpaque(false);
        kiri.add(lblJudul);
        kiri.add(lblTanggal);

        header.add(kiri, BorderLayout.WEST);
        return header;
    }

    // ===== SIDEBAR =====
    private JPanel buatSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(18, 14, 18, 14));

        sidebar.add(buatKartu("SALDO", "Rp 0", UNGU));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(buatKartu("PEMASUKAN", "Rp 0", HIJAU));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(buatKartu("PENGELUARAN", "Rp 0", MERAH));
        sidebar.add(Box.createVerticalStrut(18));

        JButton btnTambah = buatTombol("+ Tambah Transaksi", UNGU, Color.WHITE);
        btnTambah.addActionListener(e -> tampilkanFormDialog(null));
        sidebar.add(btnTambah);

        return sidebar;
    }

    private JPanel buatKartu(String judul, String nilai, Color aksen) {
        JPanel kartu = new JPanel(new GridLayout(2, 1, 0, 4));
        kartu.setBackground(BG_KARTU);
        kartu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, aksen),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        kartu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        JLabel lblJ = new JLabel(judul);
        lblJ.setFont(FONT_KECIL);
        lblJ.setForeground(Color.WHITE);

        JLabel lblN = new JLabel(nilai);
        lblN.setFont(FONT_ANGKA);
        lblN.setForeground(aksen);

        if (judul.equals("SALDO"))       lblSaldo       = lblN;
        if (judul.equals("PEMASUKAN"))   lblPemasukan   = lblN;
        if (judul.equals("PENGELUARAN")) lblPengeluaran = lblN;

        kartu.add(lblJ);
        kartu.add(lblN);
        return kartu;
    }

    // ===== KONTEN UTAMA =====
    private JPanel buatKontenUtama() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG_UTAMA);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        panel.add(buatToolbar(), BorderLayout.NORTH);
        panel.add(buatPanelTabel(), BorderLayout.CENTER);
        panel.add(buatPanelAksi(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buatToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.setBackground(BG_UTAMA);

        txtCari = new JTextField(20);
        txtCari.setFont(FONT_LABEL);
        txtCari.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_ABU, 1, true),
                BorderFactory.createEmptyBorder(5, 9, 5, 9)
        ));

        JButton btnCari = buatTombolKecil("Cari", BIRU, Color.WHITE);
        btnCari.addActionListener(e -> cariTransaksi());

        JLabel lblFilter = new JLabel("Filter:");
        lblFilter.setFont(FONT_LABEL);

        String[] opsiFilter = {"SEMUA", "PEMASUKAN", "PENGELUARAN"};
        JComboBox<String> cmbFilter = new JComboBox<>(opsiFilter);
        styleCmb(cmbFilter);
        cmbFilter.addActionListener(e ->
                filterTransaksi((String) cmbFilter.getSelectedItem()));

        JButton btnRefresh = buatTombolKecil("Refresh", new Color(107, 114, 128), Color.WHITE);
        btnRefresh.addActionListener(e -> loadData());

        toolbar.add(txtCari);
        toolbar.add(btnCari);
        toolbar.add(Box.createHorizontalStrut(8));
        toolbar.add(lblFilter);
        toolbar.add(cmbFilter);
        toolbar.add(Box.createHorizontalStrut(8));
        toolbar.add(btnRefresh);

        return toolbar;
    }

    private JScrollPane buatPanelTabel() {
        String[] kolom = {"ID", "Tanggal", "Keterangan", "Kategori", "Jenis", "Jumlah"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabelTransaksi = new JTable(tableModel);
        tabelTransaksi.setFont(FONT_LABEL);
        tabelTransaksi.setRowHeight(34);
        tabelTransaksi.setShowGrid(false);
        tabelTransaksi.setIntercellSpacing(new Dimension(0, 0));
        tabelTransaksi.setBackground(Color.WHITE);
        tabelTransaksi.setSelectionBackground(Color.WHITE);
        tabelTransaksi.setSelectionForeground(Color.DARK_GRAY);
        tabelTransaksi.setRowSelectionAllowed(true);
        tabelTransaksi.setColumnSelectionAllowed(false);

        // Header tabel
        JTableHeader header = tabelTransaksi.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lbl = new JLabel(value != null ? value.toString() : "");
                lbl.setFont(FONT_BOLD);
                lbl.setForeground(Color.WHITE);
                lbl.setBackground(BG_SIDEBAR);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                lbl.setPreferredSize(new Dimension(0, 38));
                return lbl;
            }
        });

        // Lebar kolom
        int[] lebarKolom = {45, 95, 250, 120, 110, 130};
        for (int i = 0; i < lebarKolom.length; i++)
            tabelTransaksi.getColumnModel().getColumn(i).setPreferredWidth(lebarKolom[i]);

        // Renderer statis
        tabelTransaksi.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                String jenis = (String) table.getValueAt(row, 4);
                setForeground(col == 5
                        ? ("PEMASUKAN".equals(jenis) ? HIJAU : MERAH)
                        : Color.DARK_GRAY);
                return this;
            }
        });

        tabelTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tabelTransaksi.getSelectedRow();
                if (row >= 0) {
                    selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    btnHapus.setEnabled(true);
                    btnEdit.setEnabled(true);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelTransaksi);
        scroll.setBorder(new LineBorder(BORDER_ABU, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private JPanel buatPanelAksi() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        panel.setBackground(BG_UTAMA);

        btnEdit  = buatTombolKecil("Edit",  BIRU,  Color.WHITE);
        btnHapus = buatTombolKecil("Hapus", MERAH, Color.WHITE);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);

        btnEdit.addActionListener(e  -> editTransaksi());
        btnHapus.addActionListener(e -> hapusTransaksi());

        panel.add(btnEdit);
        panel.add(btnHapus);
        return panel;
    }

    // ===== DIALOG FORM =====
    private void tampilkanFormDialog(Transaksi transaksiEdit) {
        boolean isEdit = transaksiEdit != null;
        JDialog dialog = new JDialog(this, isEdit ? "Edit Transaksi" : "Tambah Transaksi", true);
        dialog.setSize(440, 390);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel headerD = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerD.setBackground(BG_SIDEBAR);
        headerD.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        JLabel lblJd = new JLabel(isEdit ? "Edit Transaksi" : "Tambah Transaksi Baru");
        lblJd.setFont(FONT_BOLD);
        lblJd.setForeground(Color.WHITE);
        headerD.add(lblJd);
        dialog.add(headerD, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 4, 5, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTanggal    = buatTextField();
        txtKeterangan = buatTextField();
        txtJumlah     = buatTextField();

        cmbJenis    = new JComboBox<>(new String[]{"PEMASUKAN", "PENGELUARAN"});
        cmbKategori = new JComboBox<>(FormatUtil.getKategoriPemasukan());
        styleCmb(cmbJenis);
        styleCmb(cmbKategori);

        cmbJenis.addActionListener(e -> {
            cmbKategori.removeAllItems();
            String[] cats = "PEMASUKAN".equals(cmbJenis.getSelectedItem())
                    ? FormatUtil.getKategoriPemasukan()
                    : FormatUtil.getKategoriPengeluaran();
            for (String c : cats) cmbKategori.addItem(c);
        });

        if (isEdit) {
            txtTanggal.setText(transaksiEdit.getTanggal().toString());
            txtKeterangan.setText(transaksiEdit.getKeterangan());
            txtJumlah.setText(String.valueOf((long) transaksiEdit.getJumlah()));
            cmbJenis.setSelectedItem(transaksiEdit.getJenis());
            cmbKategori.setSelectedItem(transaksiEdit.getKategori());
        } else {
            txtTanggal.setText(LocalDate.now().toString());
        }

        String[] labels = {"Tanggal (YYYY-MM-DD):", "Keterangan:", "Jenis:", "Kategori:", "Jumlah (Rp):"};
        Component[] fields = {txtTanggal, txtKeterangan, cmbJenis, cmbKategori, txtJumlah};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.38;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(FONT_LABEL);
            form.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.62;
            form.add(fields[i], gbc);
        }

        dialog.add(form, BorderLayout.CENTER);

        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        tombolPanel.setBackground(Color.WHITE);
        tombolPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_ABU));

        JButton btnBatalD  = buatTombolKecil("Batal", new Color(107, 114, 128), Color.WHITE);
        JButton btnSimpanD = buatTombolKecil(isEdit ? "Update" : "Simpan", UNGU, Color.WHITE);

        btnBatalD.addActionListener(e -> dialog.dispose());
        btnSimpanD.addActionListener(e -> {
            boolean ok = isEdit
                    ? controller.updateTransaksi(transaksiEdit.getId(),
                            txtTanggal.getText(), txtKeterangan.getText(),
                            (String) cmbKategori.getSelectedItem(),
                            (String) cmbJenis.getSelectedItem(), txtJumlah.getText())
                    : controller.tambahTransaksi(
                            txtTanggal.getText(), txtKeterangan.getText(),
                            (String) cmbKategori.getSelectedItem(),
                            (String) cmbJenis.getSelectedItem(), txtJumlah.getText());

            if (ok) {
                dialog.dispose();
                loadData();
                JOptionPane.showMessageDialog(this,
                        "Transaksi berhasil " + (isEdit ? "diperbarui." : "ditambahkan."),
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Gagal menyimpan. Periksa kembali isian form.",
                        "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        tombolPanel.add(btnBatalD);
        tombolPanel.add(btnSimpanD);
        dialog.add(tombolPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ===== AKSI =====
    private void loadData() {
        updateTabel(controller.getAllTransaksi());
        updateRingkasan();
        selectedId = -1;
        if (btnHapus != null) {
            btnHapus.setEnabled(false);
            btnEdit.setEnabled(false);
        }
    }

    private void cariTransaksi() {
        updateTabel(controller.cari(txtCari.getText()));
    }

    private void filterTransaksi(String jenis) {
        updateTabel(controller.filterByJenis(jenis));
    }

    private void editTransaksi() {
        if (selectedId < 0) return;
        Transaksi t = controller.getById(selectedId);
        if (t != null) tampilkanFormDialog(t);
    }

    private void hapusTransaksi() {
        if (selectedId < 0) return;
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus transaksi ini?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (controller.hapusTransaksi(selectedId)) {
                loadData();
                JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus.", "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus transaksi.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ===== UPDATE UI =====
    private void updateTabel(List<Transaksi> list) {
        tableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Transaksi t : list) {
            tableModel.addRow(new Object[]{
                t.getId(),
                t.getTanggal().format(fmt),
                t.getKeterangan(),
                t.getKategori(),
                t.getJenis(),
                FormatUtil.formatRupiah(t.getJumlah())
            });
        }
    }

    private void updateRingkasan() {
        lblSaldo.setText(FormatUtil.formatRupiah(controller.getSaldo()));
        lblPemasukan.setText(FormatUtil.formatRupiah(controller.getTotalPemasukan()));
        lblPengeluaran.setText(FormatUtil.formatRupiah(controller.getTotalPengeluaran()));
        lblSaldo.setForeground(controller.getSaldo() >= 0 ? UNGU : MERAH);
    }

    // ===== HELPER KOMPONEN =====
    private JButton buatTombol(String teks, Color bg, Color fg) {
        JButton btn = new JButton(teks);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private JButton buatTombolKecil(String teks, Color bg, Color fg) {
        JButton btn = new JButton(teks);
        btn.setFont(FONT_LABEL);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return btn;
    }

    private JTextField buatTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_LABEL);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_ABU, 1, true),
                BorderFactory.createEmptyBorder(6, 9, 6, 9)
        ));
        tf.setPreferredSize(new Dimension(195, 32));
        return tf;
    }

    private void styleCmb(JComboBox<String> cmb) {
        cmb.setFont(FONT_LABEL);
        cmb.setBackground(Color.WHITE);
        cmb.setPreferredSize(new Dimension(195, 32));
    }
}
