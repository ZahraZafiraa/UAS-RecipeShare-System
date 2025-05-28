package service;

import java.util.HashMap;
import java.util.Map;

public class PengelolaPenggunaImpl implements IPengelolaPenggunaMgt {
    private Map<String, String> kredensialPengguna = new HashMap<>();
    private Map<String, String> profilPengguna = new HashMap<>();
    private Map<String, String> emailPengguna = new HashMap<>();

    @Override
    public void daftarkanPengguna(String namaPengguna, String kataSandi, String email) {
        if (kredensialPengguna.containsKey(namaPengguna)) {
            System.out.println("Nama pengguna sudah digunakan! Silakan pilih nama lain.");
            return;
        }
        
        kredensialPengguna.put(namaPengguna, kataSandi);
        profilPengguna.put(namaPengguna, "Email: " + email);
        emailPengguna.put(namaPengguna, email);
        System.out.println("Pengguna " + namaPengguna + " berhasil didaftarkan!");
    }

    @Override
    public boolean verifikasiPengguna(String namaPengguna, String kataSandi) {
        return kredensialPengguna.containsKey(namaPengguna) && 
               kredensialPengguna.get(namaPengguna).equals(kataSandi);
    }

    @Override
    public void perbaruiProfil(String namaPengguna, String dataProfilBaru) {
        if (profilPengguna.containsKey(namaPengguna)) {
            profilPengguna.put(namaPengguna, dataProfilBaru);
            System.out.println("Profil diperbarui untuk " + namaPengguna);
        } else {
            System.out.println("Pengguna tidak ditemukan!");
        }
    }
    
    public String getEmail(String namaPengguna) {
        return emailPengguna.get(namaPengguna);
    }
}