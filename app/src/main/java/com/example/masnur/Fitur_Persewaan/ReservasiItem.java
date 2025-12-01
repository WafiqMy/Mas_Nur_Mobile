// File: app/src/main/java/com/example/masnur/Fitur_Persewaan/ReservasiItem.java

package com.example.masnur.Fitur_Persewaan;

public class ReservasiItem {
    private int id;
    private String peminjam;
    private String barang;
    private String status;

    public ReservasiItem(int id, String peminjam, String barang, String status) {
        this.id = id;
        this.peminjam = peminjam;
        this.barang = barang;
        this.status = status;
    }

    public int getId() { return id; }
    public String getPeminjam() { return peminjam; }
    public String getBarang() { return barang; }
    public String getStatus() { return status; }
}