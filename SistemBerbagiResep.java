import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import model.Resep;
import repository.IPenyimpananResepMgt;
import repository.PenyimpananResepImpl;
import service.IPencariResepMgt;
import service.PencariResepImpl;
import service.IPengelolaPenggunaMgt;
import service.PengelolaPenggunaImpl;

public class SistemBerbagiResep {
    private static String penggunaSaatIni = null; // Untuk menyimpan nama pengguna yang login
    private static IPengelolaPenggunaMgt pengelolaPengguna;
    private static IPenyimpananResepMgt penyimpananResep;
    private static IPencariResepMgt pencariResep;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        
        // Inisialisasi komponen dengan interface references
        pengelolaPengguna = new PengelolaPenggunaImpl();
        penyimpananResep = new PenyimpananResepImpl();
        pencariResep = new PencariResepImpl(penyimpananResep);
        
        // Data contoh
        inisialisasiDataContoh();
        
        // Menu utama
        while (true) {
            tampilkanMenuUtama();
            
            try {
                int pilihan = Integer.parseInt(scanner.nextLine());
                
                switch (pilihan) {
                    case 1:
                        daftarPengguna();
                        break;
                        
                    case 2:
                        masuk();
                        break;
                        
                    case 3:
                        if (cekLogin()) {
                            tambahResep();
                        } else {
                            System.out.println("Anda harus masuk terlebih dahulu!");
                        }
                        break;
                        
                    case 4:
                        cariResep();
                        break;
                        
                    case 5:
                        lihatSemuaResep();
                        break;
                        
                    case 6:
                        lihatDetailResep();
                        break;
                        
                    case 7:
                        if (penggunaSaatIni != null) {
                            keluar();
                        } else {
                            System.out.println("Anda belum masuk!");
                        }
                        break;
                        
                    case 8:
                        System.out.println("Keluar dari Sistem Berbagi Resep. Sampai jumpa!");
                        scanner.close();
                        System.exit(0);
                        
                    default:
                        System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }
    }

    private static void inisialisasiDataContoh() {
        // Daftarkan pengguna contoh
        pengelolaPengguna.daftarkanPengguna("koki_andi", "sandi123", "andi@gmail.com");
        pengelolaPengguna.daftarkanPengguna("lia", "lia123", "lia@gmail.com");
        pengelolaPengguna.daftarkanPengguna("chef_budi", "budi456", "budi@example.id");
        pengelolaPengguna.daftarkanPengguna("diana_masak", "diana789", "diana@gmail.com");
        pengelolaPengguna.daftarkanPengguna("eko_kuliner", "eko101", "eko@example.id");

        // Resep 1: Nasi Goreng Ayam
        List<String> bahan1 = List.of("Ayam", "Nasi", "Sayuran", "Bawang merah", "Bawang putih", "Kecap manis");
        List<String> langkah1 = List.of("Masak nasi hingga matang", "Goreng ayam yang sudah dipotong dadu", 
                                        "Tumis bawang merah dan bawang putih hingga harum", 
                                        "Masukkan sayuran dan aduk rata", 
                                        "Masukkan nasi dan ayam, tambahkan kecap manis", 
                                        "Aduk hingga merata dan sajikan");
        Resep resep1 = new Resep(1, "Nasi Goreng Ayam", "Nasi goreng spesial dengan potongan ayam dan sayuran", 
                               bahan1, langkah1, "Asia", 30, "Non-vegetarian", "koki_andi");
        resep1.setRating(4.5);

        // Resep 2: Nasi Goreng Telur
        List<String> bahan2 = List.of("Nasi", "Telur", "Bawang merah", "Bawang putih", "Kecap manis");
        List<String> langkah2 = List.of("Panaskan minyak dalam wajan", "Tumis bawang merah dan bawang putih hingga harum", 
                                      "Masukkan nasi dan aduk rata", "Tambahkan kecap manis sesuai selera", 
                                      "Masukkan telur dan aduk hingga merata", "Sajikan selagi hangat");
        Resep resep2 = new Resep(2, "Nasi Goreng Telur", "Nasi goreng simpel untuk pemula dengan telur", 
                               bahan2, langkah2, "Asia", 15, "Vegetarian", "lia");
        resep2.setRating(4.2);

        // Resep 3: Salad Sayuran
        List<String> bahan3 = List.of("Selada", "Tomat", "Mentimun", "Paprika", "Minyak zaitun", "Balsamic vinegar", "Garam", "Lada hitam");
        List<String> langkah3 = List.of("Cuci semua sayuran", "Potong selada, tomat, mentimun, dan paprika sesuai selera", 
                                        "Campurkan semua sayuran dalam mangkuk", 
                                        "Buat dressing dengan mencampur minyak zaitun, balsamic vinegar, garam, dan lada hitam", 
                                        "Tuang dressing ke atas salad dan aduk rata");
        Resep resep3 = new Resep(3, "Salad Sayuran Segar", "Salad sayuran segar dengan dressing balsamic vinegar", 
                               bahan3, langkah3, "Eropa", 10, "Vegan", "diana_masak");
        resep3.setRating(4.7);

        // Resep 4: Sup Ayam
        List<String> bahan4 = List.of("Ayam", "Wortel", "Kentang", "Seledri", "Bawang bombay", "Bawang putih", "Kaldu ayam", "Garam", "Lada");
        List<String> langkah4 = List.of("Rebus ayam hingga matang dan potong dadu", 
                                        "Potong wortel, kentang, dan seledri", 
                                        "Tumis bawang bombay dan bawang putih hingga harum", 
                                        "Masukkan kaldu ayam, wortel, dan kentang", 
                                        "Setelah sayuran setengah matang, masukkan ayam", 
                                        "Tambahkan garam dan lada sesuai selera", 
                                        "Masak hingga semua bahan matang", 
                                        "Taburi dengan seledri dan sajikan");
        Resep resep4 = new Resep(4, "Sup Ayam Klasik", "Sup ayam hangat dengan sayuran dan kaldu yang lezat", 
                               bahan4, langkah4, "Internasional", 45, "Non-vegetarian", "chef_budi");
        resep4.setRating(4.8);

        // Resep 5: Pancake Pisang
        List<String> bahan5 = List.of("Tepung terigu", "Pisang", "Telur", "Susu", "Gula", "Baking powder", "Garam", "Madu");
        List<String> langkah5 = List.of("Campur tepung terigu, gula, baking powder, dan garam dalam mangkuk", 
                                        "Di mangkuk terpisah, kocok telur dan tambahkan susu", 
                                        "Gabungkan bahan kering dan basah, aduk hingga merata", 
                                        "Haluskan pisang dan masukkan ke dalam adonan", 
                                        "Panaskan wajan antilengket dengan sedikit mentega", 
                                        "Tuang adonan dan masak hingga muncul gelembung", 
                                        "Balik dan masak sisi lainnya hingga kecoklatan", 
                                        "Sajikan dengan madu atau topping favorit");
        Resep resep5 = new Resep(5, "Pancake Pisang", "Pancake lembut dengan cita rasa pisang yang manis", 
                               bahan5, langkah5, "Hidangan Penutup", 20, "Vegetarian", "lia");
        resep5.setRating(4.6);

        // Resep 6: Mie Goreng Sayuran
        List<String> bahan6 = List.of("Mie instan", "Wortel", "Sawi", "Kol", "Bawang merah", "Bawang putih", "Cabai", "Kecap manis", "Garam");
        List<String> langkah6 = List.of("Rebus mie hingga setengah matang, tiriskan", 
                                        "Potong-potong semua sayuran", 
                                        "Tumis bawang merah, bawang putih, dan cabai hingga harum", 
                                        "Masukkan sayuran dan aduk hingga layu", 
                                        "Tambahkan mie, kecap manis, dan garam", 
                                        "Aduk hingga semua bahan tercampur rata dan matang", 
                                        "Sajikan selagi hangat");
        Resep resep6 = new Resep(6, "Mie Goreng Sayuran", "Mie goreng dengan aneka sayuran yang sehat dan lezat", 
                               bahan6, langkah6, "Asia", 15, "Vegetarian", "diana_masak");
        resep6.setRating(4.3);

        // Resep 7: Tahu Crispy
        List<String> bahan7 = List.of("Tahu", "Tepung terigu", "Tepung beras", "Bawang putih bubuk", "Garam", "Lada", "Air");
        List<String> langkah7 = List.of("Potong tahu menjadi bentuk segitiga", 
                                        "Campur tepung terigu, tepung beras, bawang putih bubuk, garam, dan lada", 
                                        "Tambahkan air secukupnya hingga membentuk adonan kental", 
                                        "Celupkan tahu ke dalam adonan tepung", 
                                        "Goreng dalam minyak panas hingga keemasan dan crispy", 
                                        "Tiriskan dan sajikan dengan saus sambal");
        Resep resep7 = new Resep(7, "Tahu Crispy", "Tahu dengan balutan tepung crispy yang renyah", 
                               bahan7, langkah7, "Asia", 25, "Vegetarian", "eko_kuliner");
        resep7.setRating(4.4);

        // Tambahkan semua resep ke penyimpanan
        penyimpananResep.tambahResep(resep1);
        penyimpananResep.tambahResep(resep2);
        penyimpananResep.tambahResep(resep3);
        penyimpananResep.tambahResep(resep4);
        penyimpananResep.tambahResep(resep5);
        penyimpananResep.tambahResep(resep6);
        penyimpananResep.tambahResep(resep7);
    }

    private static void tampilkanMenuUtama() {
        System.out.println("\n=== Sistem Berbagi Resep ===");
        if (penggunaSaatIni != null) {
            System.out.println("Selamat datang, " + penggunaSaatIni + "!");
        }
        System.out.println("1. Daftar Pengguna");
        System.out.println("2. Masuk");
        System.out.println("3. Tambah Resep");
        System.out.println("4. Cari Resep");
        System.out.println("5. Lihat Semua Resep");
        System.out.println("6. Lihat Detail Resep");
        if (penggunaSaatIni != null) {
            System.out.println("7. Keluar dari Akun");
        }
        System.out.println("8. Keluar dari Aplikasi");
        System.out.print("Pilih menu: ");
    }

    private static void daftarPengguna() {
        System.out.print("Masukkan nama pengguna: ");
        String namaPengguna = scanner.nextLine();
        System.out.print("Masukkan kata sandi: ");
        String kataSandi = scanner.nextLine();
        
        String email;
        boolean emailValid = false;
        
        do {
            System.out.print("Masukkan email: ");
            email = scanner.nextLine();
            
            // Validasi email
            if (validasiEmail(email)) {
                emailValid = true;
            } else {
                System.out.println("Email tidak valid, mohon ulangi! (harus mengandung @ dan diakhiri dengan gmail.com atau .id)");
            }
        } while (!emailValid);
        
        pengelolaPengguna.daftarkanPengguna(namaPengguna, kataSandi, email);
    }
    
    private static boolean validasiEmail(String email) {
        // Email harus mengandung @ dan diakhiri dengan gmail.com atau .id
        return email.contains("@") && (email.endsWith("gmail.com") || email.endsWith(".id"));
    }

    private static void masuk() {
        System.out.print("Masukkan nama pengguna: ");
        String namaMasuk = scanner.nextLine();
        System.out.print("Masukkan kata sandi: ");
        String sandiMasuk = scanner.nextLine();
        
        if (pengelolaPengguna.verifikasiPengguna(namaMasuk, sandiMasuk)) {
            penggunaSaatIni = namaMasuk;
            System.out.println("Berhasil masuk! Selamat datang, " + penggunaSaatIni + "!");
        } else {
            System.out.println("Kredensial tidak valid atau pengguna belum terdaftar!");
        }
    }
    
    private static void keluar() {
        System.out.println("Berhasil keluar dari akun " + penggunaSaatIni);
        penggunaSaatIni = null;
    }
    
    private static boolean cekLogin() {
        return penggunaSaatIni != null;
    }

    // Validasi resep
    private static boolean validasiResep(String judul, String deskripsi, List<String> bahan, 
                                        List<String> langkah, String kategori, int waktuPersiapan) {
        // Validasi judul
        if (judul == null || judul.trim().isEmpty()) {
            System.out.println("Error: Judul resep tidak boleh kosong!");
            return false;
        }
        
        // Validasi deskripsi
        if (deskripsi == null || deskripsi.trim().isEmpty()) {
            System.out.println("Error: Deskripsi resep tidak boleh kosong!");
            return false;
        }
        
        // Validasi bahan
        if (bahan == null || bahan.isEmpty() || (bahan.size() == 1 && bahan.get(0).trim().isEmpty())) {
            System.out.println("Error: Daftar bahan tidak boleh kosong!");
            return false;
        }
        
        // Validasi langkah
        if (langkah == null || langkah.isEmpty() || (langkah.size() == 1 && langkah.get(0).trim().isEmpty())) {
            System.out.println("Error: Langkah-langkah memasak tidak boleh kosong!");
            return false;
        }
        
        // Validasi kategori
        if (kategori == null || kategori.trim().isEmpty()) {
            System.out.println("Error: Kategori tidak boleh kosong!");
            return false;
        }
        
        // Validasi waktu persiapan
        if (waktuPersiapan <= 0) {
            System.out.println("Error: Waktu persiapan harus lebih dari 0 menit!");
            return false;
        }
        
        return true;
    }

    private static void tambahResep() {
        System.out.print("Masukkan judul resep: ");
        String judul = scanner.nextLine();
        
        System.out.print("Masukkan deskripsi: ");
        String deskripsi = scanner.nextLine();
        
        System.out.print("Masukkan bahan (pisahkan dengan koma): ");
        String bahanInput = scanner.nextLine();
        List<String> bahan = new ArrayList<>();
        if (!bahanInput.trim().isEmpty()) {
            String[] bahanArray = bahanInput.split(",");
            for (String b : bahanArray) {
                if (!b.trim().isEmpty()) {
                    bahan.add(b.trim());
                }
            }
        }
        
        System.out.print("Masukkan langkah (pisahkan dengan koma): ");
        String langkahInput = scanner.nextLine();
        List<String> langkah = new ArrayList<>();
        if (!langkahInput.trim().isEmpty()) {
            String[] langkahArray = langkahInput.split(",");
            for (String l : langkahArray) {
                if (!l.trim().isEmpty()) {
                    langkah.add(l.trim());
                }
            }
        }
        
        System.out.print("Masukkan kategori: ");
        String kategori = scanner.nextLine();
        
        int waktu = 0;
        boolean waktuValid = false;
        while (!waktuValid) {
            System.out.print("Masukkan waktu persiapan (menit): ");
            try {
                waktu = Integer.parseInt(scanner.nextLine());
                if (waktu > 0) {
                    waktuValid = true;
                } else {
                    System.out.println("Waktu persiapan harus lebih dari 0 menit. Silakan ulangi.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka. Silakan ulangi.");
            }
        }
        
        // Pilihan preferensi diet
        System.out.println("\nPilih preferensi diet:");
        System.out.println("1. Vegetarian");
        System.out.println("2. Vegan");
        System.out.println("3. Non-vegetarian");
        System.out.print("Pilihan Anda: ");
        
        String diet = "Non-vegetarian"; // Default
        try {
            int pilihanDiet = Integer.parseInt(scanner.nextLine());
            switch (pilihanDiet) {
                case 1:
                    diet = "Vegetarian";
                    break;
                case 2:
                    diet = "Vegan";
                    break;
                case 3:
                    diet = "Non-vegetarian";
                    break;
                default:
                    System.out.println("Pilihan tidak valid, diet diatur ke 'Non-vegetarian'.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid, diet diatur ke 'Non-vegetarian'.");
        }
        
        // Validasi resep sebelum menambahkan
        if (validasiResep(judul, deskripsi, bahan, langkah, kategori, waktu)) {
            Resep resepBaru = new Resep(penyimpananResep.dapatkanSemuaResep().size() + 1, 
                                      judul, deskripsi, bahan, langkah, 
                                      kategori, waktu, diet, penggunaSaatIni);
            penyimpananResep.tambahResep(resepBaru);
            System.out.println("Resep '" + judul + "' berhasil ditambahkan!");
        } else {
            System.out.println("Resep tidak dapat ditambahkan karena data tidak valid. Silakan coba lagi.");
        }
    }

    private static void cariResep() {
        System.out.println("\nCari berdasarkan:");
        System.out.println("1. Bahan");
        System.out.println("2. Preferensi Diet");
        System.out.println("3. Waktu Persiapan");
        System.out.print("Pilih menu: ");
        
        try {
            int pilihanCari = Integer.parseInt(scanner.nextLine());
            
            List<Resep> hasilCari = new ArrayList<>();
            switch (pilihanCari) {
                case 1:
                    // Menampilkan daftar bahan dari resep yang tersedia
                    System.out.println("\nBahan-bahan yang tersedia dalam resep:");
                    List<String> semuaBahan = new ArrayList<>();
                    for (Resep r : penyimpananResep.dapatkanSemuaResep()) {
                        for (String bahan : r.getBahan()) {
                            if (!semuaBahan.contains(bahan)) {
                                semuaBahan.add(bahan);
                                System.out.println("- " + bahan);
                            }
                        }
                    }
                    
                    System.out.print("\nMasukkan bahan untuk dicari (pisahkan dengan koma): ");
                    String[] cariBahan = scanner.nextLine().split(",");
                    List<String> bahanCari = new ArrayList<>();
                    for (String cb : cariBahan) {
                        bahanCari.add(cb.trim());
                    }
                    hasilCari = pencariResep.cariBerdasarkanBahan(bahanCari);
                    break;
                    
                case 2:
                    // Menampilkan pilihan preferensi diet
                    System.out.println("\nPilih preferensi diet:");
                    System.out.println("1. Vegetarian");
                    System.out.println("2. Vegan");
                    System.out.println("3. Non-vegetarian");
                    System.out.print("Pilihan Anda: ");
                    
                    int pilihanDiet = Integer.parseInt(scanner.nextLine());
                    String preferensi;
                    switch (pilihanDiet) {
                        case 1:
                            preferensi = "Vegetarian";
                            break;
                        case 2:
                            preferensi = "Vegan";
                            break;
                        case 3:
                            preferensi = "Non-vegetarian";
                            break;
                        default:
                            System.out.println("Pilihan tidak valid!");
                            return;
                    }
                    hasilCari = pencariResep.cariBerdasarkanDiet(preferensi);
                    break;
                    
                case 3:
                    System.out.println("\nWaktu persiapan resep yang tersedia (dalam menit):");
                    List<Integer> waktuList = new ArrayList<>();
                    for (Resep r : penyimpananResep.dapatkanSemuaResep()) {
                        if (!waktuList.contains(r.getWaktuPersiapan())) {
                            waktuList.add(r.getWaktuPersiapan());
                            System.out.println("- " + r.getWaktuPersiapan() + " menit");
                        }
                    }
                    
                    System.out.print("\nMasukkan maksimal waktu persiapan (menit): ");
                    int maksWaktu = Integer.parseInt(scanner.nextLine());
                    hasilCari = pencariResep.cariBerdasarkanWaktuPersiapan(maksWaktu);
                    break;
                    
                default:
                    System.out.println("Pilihan tidak valid!");
                    return;
            }
            
            System.out.println("\nHasil Pencarian:");
            if (hasilCari.isEmpty()) {
                System.out.println("Tidak ada resep yang sesuai dengan kriteria pencarian.");
            } else {
                for (int i = 0; i < hasilCari.size(); i++) {
                    Resep r = hasilCari.get(i);
                    System.out.println((i+1) + ". " + r.getJudul() + " (" + r.getKategori() + ") - " + r.getDeskripsi());
                }
                
                System.out.print("\nApakah Anda ingin melihat detail resep? (Y/N): ");
                String jawaban = scanner.nextLine();
                if (jawaban.equalsIgnoreCase("Y")) {
                    System.out.print("Pilih nomor resep: ");
                    int nomorResep = Integer.parseInt(scanner.nextLine());
                    if (nomorResep > 0 && nomorResep <= hasilCari.size()) {
                        tampilkanDetailResep(hasilCari.get(nomorResep-1));
                    } else {
                        System.out.println("Nomor resep tidak valid!");
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka!");
        }
    }

    private static void lihatSemuaResep() {
        List<Resep> semuaResep = penyimpananResep.dapatkanSemuaResep();
        System.out.println("\nSemua Resep:");
        for (int i = 0; i < semuaResep.size(); i++) {
            Resep r = semuaResep.get(i);
            System.out.println((i+1) + ". " + r.getJudul() + " oleh " + r.getPenulis());
            System.out.println("   Deskripsi: " + r.getDeskripsi());
            System.out.println("   Waktu: " + r.getWaktuPersiapan() + " menit | Diet: " + r.getPreferensiDiet() + " | Rating: " + r.getRating() + "/5.0");
            System.out.println();
        }
    }

    private static void lihatDetailResep() {
        List<Resep> semuaResep = penyimpananResep.dapatkanSemuaResep();
        
        if (semuaResep.isEmpty()) {
            System.out.println("\nBelum ada resep yang tersedia.");
            return;
        }
        
        System.out.println("\nDaftar Resep:");
        for (int i = 0; i < semuaResep.size(); i++) {
            Resep r = semuaResep.get(i);
            System.out.println((i+1) + ". " + r.getJudul() + " - " + r.getDeskripsi().substring(0, Math.min(r.getDeskripsi().length(), 50)) + 
                              (r.getDeskripsi().length() > 50 ? "..." : ""));
        }
        
        System.out.print("\nPilih nomor resep yang ingin dilihat: ");
        try {
            int pilihan = Integer.parseInt(scanner.nextLine());
            
            if (pilihan > 0 && pilihan <= semuaResep.size()) {
                Resep resepDipilih = semuaResep.get(pilihan-1);
                tampilkanDetailResep(resepDipilih);
                
                // Fitur interaktif tambahan - Rating
                if (cekLogin()) {
                    System.out.print("\nApakah Anda ingin memberi rating untuk resep ini? (Y/N): ");
                    String jawaban = scanner.nextLine();
                    if (jawaban.equalsIgnoreCase("Y")) {
                        System.out.print("Berikan rating (1-5): ");
                        try {
                            double rating = Double.parseDouble(scanner.nextLine());
                            if (rating >= 1 && rating <= 5) {
                                // Dalam contoh ini, kita hanya memperbarui rating langsung
                                // Dalam aplikasi nyata, kita perlu menyimpan rating dari setiap pengguna
                                resepDipilih.setRating((resepDipilih.getRating() + rating) / 2);
                                System.out.println("Rating berhasil diberikan! Rating baru: " + resepDipilih.getRating());
                            } else {
                                System.out.println("Rating harus antara 1-5!");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Rating harus berupa angka!");
                        }
                    }
                }
            } else {
                System.out.println("Nomor tidak valid!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka!");
        }
    }

    private static void tampilkanDetailResep(Resep resep) {
        System.out.println("\n=== Detail Resep ===");
        System.out.println("ID: " + resep.getId());
        System.out.println("Judul: " + resep.getJudul());
        System.out.println("Penulis: " + resep.getPenulis());
        System.out.println("Deskripsi: " + resep.getDeskripsi());
        System.out.println("Kategori: " + resep.getKategori());
        System.out.println("Waktu Persiapan: " + resep.getWaktuPersiapan() + " menit");
        System.out.println("Preferensi Diet: " + resep.getPreferensiDiet());
        System.out.println("Rating: " + resep.getRating() + "/5.0");
        
        System.out.println("\nBahan-bahan:");
        for (int i = 0; i < resep.getBahan().size(); i++) {
            System.out.println((i+1) + ". " + resep.getBahan().get(i));
        }
        
        System.out.println("\nLangkah-langkah:");
        for (int i = 0; i < resep.getLangkah().size(); i++) {
            System.out.println((i+1) + ". " + resep.getLangkah().get(i));
        }
    }
}