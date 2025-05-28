package model;

import java.util.List;

public class Resep {
    private int id;
    private String judul;
    private String deskripsi;
    private List<String> bahan;
    private List<String> langkah;
    private String kategori;
    private int waktuPersiapan; // dalam menit
    private String preferensiDiet;
    private String penulis;
    private double rating;

    public Resep(int id, String judul, String deskripsi, List<String> bahan, 
               List<String> langkah, String kategori, int waktuPersiapan, 
               String preferensiDiet, String penulis) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.bahan = bahan;
        this.langkah = langkah;
        this.kategori = kategori;
        this.waktuPersiapan = waktuPersiapan;
        this.preferensiDiet = preferensiDiet;
        this.penulis = penulis;
        this.rating = 0.0;
    }

    // Getter dan setter
    public int getId() { return id; }
    public String getJudul() { return judul; }
    public String getDeskripsi() { return deskripsi; }
    public List<String> getBahan() { return bahan; }
    public List<String> getLangkah() { return langkah; }
    public String getKategori() { return kategori; }
    public int getWaktuPersiapan() { return waktuPersiapan; }
    public String getPreferensiDiet() { return preferensiDiet; }
    public String getPenulis() { return penulis; }
    public double getRating() { return rating; }
    
    public void setRating(double rating) { this.rating = rating; }
}
