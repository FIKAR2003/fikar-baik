package com.example.data.model

data class Article(
    val id: String,
    val title: String,
    val shortDescription: String,
    val fullContent: String,
    val imageUrl: String = "",
    val durationSeconds: Int = 80 // For the interactive/audio player simulation
) {
    companion object {
        val DEFAULT_ARTICLES = listOf(
            Article(
                id = "gizi_seimbang",
                title = "Gizi Seimbang",
                shortDescription = "Kenali 4 pilar gizi seimbang untuk tumbuh kembang optimal.",
                fullContent = "Gizi seimbang adalah susunan makanan sehari-hari yang mengandung zat gizi dalam jenis dan jumlah yang sesuai dengan kebutuhan tubuh, dengan memperhatikan prinsip keanekaragaman pangan, aktivitas fisik, perilaku hidup bersih, dan memantau berat badan secara teratur.\n\n4 Pilar Utama Gizi Seimbang bagi Anak:\n1. Mengonsumsi Keanekaragaman Makanan: Padukan karbohidrat, protein (hewani & nabati), serta vitamin & mineral.\n2. Membiasakan Perilaku Hidup Bersih: Selalu mencuci tangan sebelum makan demi mencegah infeksi penyakit.\n3. Melakukan Aktivitas Fisik: Ajari anak aktif bergerak melalui permainan luar ruangan yang sehat.\n4. Memantau Berat Badan Secara Teratur: Timbang berat badan anak setiap bulan di Posyandu/Buku KMS Digital untuk melacak status tumbuh kembanga anak.",
                durationSeconds = 80
            ),
            Article(
                id = "makanan_bergizi",
                title = "Makanan Bergizi",
                shortDescription = "Pilihan makanan sehat dan kaya zat gizi untuk si kecil.",
                fullContent = "Anak membutuhkan asupan gizi lengkap demi masa depan yang cerah. Pastikan piring makan si kecil selalu berwarna-warni dengan kombinasi karbohidrat (nasi, ubi, jagung), protein tinggi (ikan kembung, ayam, sapi, telur, tempe), lemak baik, buah-buahan, dan sayuran segar.\n\nTips Penyajian Makanan Bergizi:\n- Sajikan ikan kembung karena kaya akan omega-3 yang sangat baik untuk kecerdasan otak anak.\n- Kurangi konsumsi makanan cepat saji atau camilan kemasan tinggi gula sintetik dan pengawet.\n- Berikan air putih secukupnya dan kurangi minuman manis kemasan.",
                durationSeconds = 60
            ),
            Article(
                id = "stimulasi_tumbuh",
                title = "Stimulasi Tumbuh",
                shortDescription = "Pemberian stimulus terbaik untuk asah kemampuan motorik.",
                fullContent = "Perkembangan kognitif dan fisik anak tidak hanya didukung oleh gizi yang baik, namun juga stimulasi asah-asih-asuh dari orang tua setiap hari.\n\nBentuk Stimulasi Praktis:\n- Bayi (0-6 bulan): Sering diajak berbicara, tummy-time, dan memegang mainan bertekstur.\n- Balita (1-3 tahun): Belajar menyusun balok, menggambar coretan, bernyanyi bersama, serta melatih fisik di taman bermain untuk koordinasi motorik kasar dan halus.\n- Komunikasi dua arah: Ajukan pertanyaan terbuka untuk meningkatkan interaksi kosakata.",
                durationSeconds = 90
            ),
            Article(
                id = "pencegahan_stunting",
                title = "Pencegahan Stunting",
                shortDescription = "Langkah konkret cegah stunting pada 1000 Hari Pertama Kehidupan.",
                fullContent = "Stunting adalah kondisi gagal tumbuh kembang pada balita akibat kekurangan gizi kronis terutama pada 1000 Hari Pertama Kehidupan (HPK). Stunting dapat mengganggu perkembangan otak dan menurunkan imunitas anak.\n\nLangkah Konkret Mencegah Stunting:\n1. Penuhi gizi seimbang sejak masa kehamilan (Ibu hamil mengonsumsi tablet tambah darah & protein).\n2. Berikan ASI Eksklusif sampai bayi berumur 6 bulan.\n3. Lanjutkan pemberian ASI ditambah MPASI padat gizi berkualitas setelah berusia 6 bulan.\n4. Imunisasi dasar lengkap serta aktif datang ke Posyandu setiap bulan untuk memantau KMS anak.\n5. Jaga fasilitas sanitasi rumah yang bersih dan pola hidup higienis.",
                durationSeconds = 120
            )
        )
    }
}
