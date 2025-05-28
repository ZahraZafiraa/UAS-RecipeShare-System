package service;

import java.util.ArrayList;
import java.util.List;
import model.Resep;
import repository.IPenyimpananResepMgt;

public class PencariResepImpl implements IPencariResepMgt {
    private IPenyimpananResepMgt penyimpananResep;

    public PencariResepImpl(IPenyimpananResepMgt penyimpananResep) {
        this.penyimpananResep = penyimpananResep;
    }

    @Override
    public List<Resep> cariBerdasarkanBahan(List<String> bahan) {
        List<Resep> semuaResep = penyimpananResep.dapatkanSemuaResep();
        List<Resep> hasil = new ArrayList<>();
        
        for (Resep resep : semuaResep) {
            boolean mengandungSatu = false;
            for (String b : bahan) {
                // Periksa jika resep mengandung setidaknya satu dari bahan yang dicari
                for (String bahanResep : resep.getBahan()) {
                    if (bahanResep.toLowerCase().contains(b.toLowerCase())) {
                        mengandungSatu = true;
                        break;
                    }
                }
                if (mengandungSatu) break;
            }
            if (mengandungSatu) {
                hasil.add(resep);
            }
        }
        return hasil;
    }

    @Override
    public List<Resep> cariBerdasarkanDiet(String preferensiDiet) {
        List<Resep> semuaResep = penyimpananResep.dapatkanSemuaResep();
        List<Resep> hasil = new ArrayList<>();
        
        for (Resep resep : semuaResep) {
            if (resep.getPreferensiDiet().equalsIgnoreCase(preferensiDiet)) {
                hasil.add(resep);
            }
        }
        return hasil;
    }

    @Override
    public List<Resep> cariBerdasarkanWaktuPersiapan(int maksimalMenit) {
        List<Resep> semuaResep = penyimpananResep.dapatkanSemuaResep();
        List<Resep> hasil = new ArrayList<>();
        
        for (Resep resep : semuaResep) {
            if (resep.getWaktuPersiapan() <= maksimalMenit) {
                hasil.add(resep);
            }
        }
        return hasil;
    }
}