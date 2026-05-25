package util;

import java.text.NumberFormat;
import java.util.Locale;

// FormatUtil - utility class untuk formatting

public class FormatUtil {

    private static final NumberFormat RUPIAH = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public static String formatRupiah(double amount) {
        return RUPIAH.format(amount);
    }

    public static String[] getKategoriPemasukan() {
        return new String[]{"Gaji", "Bonus", "Freelance", "Investasi", "Hadiah", "Lainnya"};
    }

    public static String[] getKategoriPengeluaran() {
        return new String[]{"Makanan", "Transportasi", "Belanja", "Tagihan", "Hiburan",
                "Kesehatan", "Pendidikan", "Tabungan", "Lainnya"};
    }
}
