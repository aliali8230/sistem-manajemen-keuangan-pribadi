package model;

import java.time.LocalDate;

 // BaseModel - Abstract class sebagai fondasi semua model
 // Menerapkan konsep ABSTRACTION

public abstract class BaseModel {

    // Encapsulation: field protected agar subclass bisa akses
    protected int id;
    protected LocalDate tanggal;
    protected String keterangan;

    // Constructor
    public BaseModel(int id, LocalDate tanggal, String keterangan) {
        this.id = id;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
    }

    // Abstract method - wajib diimplementasi oleh subclass (Abstraction)
    public abstract double getJumlah();
    public abstract String getJenis();
    public abstract String getRingkasan();

    // Getter & Setter (Encapsulation)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    // toString bisa di-override (Polymorphism)
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", getJenis(), tanggal, keterangan);
    }
}
