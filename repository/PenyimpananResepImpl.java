package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Resep;

public class PenyimpananResepImpl implements IPenyimpananResepMgt {
    private Map<Integer, Resep> daftarResep = new HashMap<>();
    private int idBerikutnya = 1;

    @Override
    public void tambahResep(Resep resep) {
        daftarResep.put(resep.getId(), resep);
        System.out.println("Resep '" + resep.getJudul() + "' berhasil ditambahkan!");
    }

    @Override
    public Resep dapatkanResepById(int id) {
        return daftarResep.get(id);
    }

    @Override
    public List<Resep> dapatkanSemuaResep() {
        return new ArrayList<>(daftarResep.values());
    }

    @Override
    public List<Resep> dapatkanResepByKategori(String kategori) {
        List<Resep> hasil = new ArrayList<>();
        for (Resep resep : daftarResep.values()) {
            if (resep.getKategori().equalsIgnoreCase(kategori)) {
                hasil.add(resep);
            }
        }
        return hasil;
    }
}