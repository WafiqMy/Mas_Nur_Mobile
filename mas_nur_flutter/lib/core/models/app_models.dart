class ApiStatusResponse {
  ApiStatusResponse({required this.status, required this.message});

  factory ApiStatusResponse.fromJson(Map<String, dynamic> json) {
    return ApiStatusResponse(
      status: json['status']?.toString() ?? '',
      message: json['message']?.toString() ?? '',
    );
  }

  final String status;
  final String message;
}

class BeritaModel {
  BeritaModel({
    required this.idBerita,
    required this.judulBerita,
    required this.isiBerita,
    required this.tanggalBerita,
    required this.fotoBerita,
    required this.username,
  });

  factory BeritaModel.fromJson(Map<String, dynamic> json) => BeritaModel(
    idBerita: json['id_berita']?.toString() ?? '',
    judulBerita: json['judul_berita']?.toString() ?? '',
    isiBerita: json['isi_berita']?.toString() ?? '',
    tanggalBerita: json['tanggal_berita']?.toString() ?? '',
    fotoBerita: json['foto_berita']?.toString() ?? '',
    username: json['username']?.toString() ?? '',
  );

  final String idBerita;
  final String judulBerita;
  final String isiBerita;
  final String tanggalBerita;
  final String fotoBerita;
  final String username;
}

class AcaraModel {
  AcaraModel({
    required this.idEvent,
    required this.namaEvent,
    required this.tanggalEvent,
    required this.deskripsiEvent,
    required this.lokasiEvent,
    required this.gambarEvent,
    required this.username,
  });

  factory AcaraModel.fromJson(Map<String, dynamic> json) => AcaraModel(
    idEvent: json['id_event']?.toString() ?? '',
    namaEvent: json['nama_event']?.toString() ?? '',
    tanggalEvent: json['tanggal_event']?.toString() ?? '',
    deskripsiEvent: json['deskripsi_event']?.toString() ?? '',
    lokasiEvent: json['lokasi_event']?.toString() ?? '',
    gambarEvent: json['gambar_event']?.toString() ?? '',
    username: json['username']?.toString() ?? '',
  );

  final String idEvent;
  final String namaEvent;
  final String tanggalEvent;
  final String deskripsiEvent;
  final String lokasiEvent;
  final String gambarEvent;
  final String username;
}

class BarangModel {
  BarangModel({
    required this.idPersewaan,
    required this.namaBarang,
    required this.jenis,
    required this.harga,
    required this.jumlah,
    required this.deskripsi,
    required this.spesifikasi,
    required this.fasilitas,
    this.gambar = '',
  });

  factory BarangModel.fromJson(Map<String, dynamic> json) => BarangModel(
    idPersewaan: int.tryParse(json['id_persewaan']?.toString() ?? '') ?? 0,
    namaBarang: json['nama_barang']?.toString() ?? '',
    jenis: json['Jenis']?.toString() ?? '',
    harga: int.tryParse(json['harga']?.toString() ?? '') ?? 0,
    jumlah: int.tryParse(json['jumlah']?.toString() ?? '') ?? 0,
    deskripsi: json['deskripsi']?.toString() ?? '',
    spesifikasi: json['spesifikasi']?.toString() ?? '',
    fasilitas: json['fasilitas']?.toString() ?? '',
    gambar: json['gambar']?.toString() ?? '',
  );

  final int idPersewaan;
  final String namaBarang;
  final String jenis;
  final int harga;
  final int jumlah;
  final String deskripsi;
  final String spesifikasi;
  final String fasilitas;
  final String gambar;
}

class NotificationItem {
  NotificationItem({
    required this.idReservasi,
    required this.namaPengguna,
    required this.jenis,
  });

  factory NotificationItem.fromJson(Map<String, dynamic> json) => NotificationItem(
    idReservasi: json['id_reservasi']?.toString() ?? '',
    namaPengguna: json['nama_pengguna']?.toString() ?? '',
    jenis: json['jenis']?.toString() ?? '',
  );

  final String idReservasi;
  final String namaPengguna;
  final String jenis;
}

class ProfilMasjidModel {
  ProfilMasjidModel({
    required this.judulSejarah,
    required this.deskripsiSejarah,
    required this.gambarSejarahMasjid,
  });

  factory ProfilMasjidModel.fromJson(Map<String, dynamic> json) => ProfilMasjidModel(
    judulSejarah: json['judul_sejarah']?.toString() ?? '',
    deskripsiSejarah: json['deskripsi_sejarah']?.toString() ?? '',
    gambarSejarahMasjid: json['gambar_sejarah_masjid']?.toString() ?? '',
  );

  final String judulSejarah;
  final String deskripsiSejarah;
  final String gambarSejarahMasjid;
}

class StrukturOrganisasiModel {
  StrukturOrganisasiModel({
    required this.gambarStrukturOrganisasi,
    required this.gambarStrukturRemas,
  });

  factory StrukturOrganisasiModel.fromJson(Map<String, dynamic> json) => StrukturOrganisasiModel(
    gambarStrukturOrganisasi: json['gambar_struktur_organisasi']?.toString() ?? '',
    gambarStrukturRemas: json['gambar_struktur_remas']?.toString() ?? '',
  );

  final String gambarStrukturOrganisasi;
  final String gambarStrukturRemas;
}

class UserProfileModel {
  UserProfileModel({
    required this.username,
    required this.nama,
    required this.email,
  });

  factory UserProfileModel.fromJson(Map<String, dynamic> json) => UserProfileModel(
    username: json['username']?.toString() ?? '',
    nama: json['nama']?.toString() ?? '',
    email: json['email']?.toString() ?? '',
  );

  final String username;
  final String nama;
  final String email;
}

class ReservasiItemModel {
  ReservasiItemModel({
    required this.id,
    required this.title,
    required this.start,
    required this.end,
    required this.color,
    required this.extendedProps,
  });

  factory ReservasiItemModel.fromJson(Map<String, dynamic> json) => ReservasiItemModel(
    id: int.tryParse(json['id']?.toString() ?? '') ?? 0,
    title: json['title']?.toString() ?? '',
    start: json['start']?.toString() ?? '',
    end: json['end']?.toString() ?? '',
    color: json['color']?.toString() ?? '',
    extendedProps: ReservasiExtendedProps.fromJson((json['extendedProps'] as Map<String, dynamic>? ?? {})),
  );

  final int id;
  final String title;
  final String start;
  final String end;
  final String color;
  final ReservasiExtendedProps extendedProps;
}

class ReservasiExtendedProps {
  ReservasiExtendedProps({
    required this.peminjam,
    required this.telepon,
    required this.email,
    required this.barang,
    required this.jenis,
    required this.jumlah,
    required this.harga,
    required this.keperluan,
    required this.status,
    required this.notes,
  });

  factory ReservasiExtendedProps.fromJson(Map<String, dynamic> json) => ReservasiExtendedProps(
    peminjam: json['peminjam']?.toString() ?? '',
    telepon: json['telepon']?.toString() ?? '',
    email: json['email']?.toString() ?? '',
    barang: json['barang']?.toString() ?? '',
    jenis: json['jenis']?.toString() ?? '',
    jumlah: int.tryParse(json['jumlah']?.toString() ?? '') ?? 0,
    harga: int.tryParse(json['harga']?.toString() ?? '') ?? 0,
    keperluan: json['keperluan']?.toString() ?? '',
    status: json['status']?.toString() ?? '',
    notes: json['notes']?.toString() ?? '',
  );

  final String peminjam;
  final String telepon;
  final String email;
  final String barang;
  final String jenis;
  final int jumlah;
  final int harga;
  final String keperluan;
  final String status;
  final String notes;
}

class ReservasiDetailModel {
  ReservasiDetailModel({
    required this.idReservasi,
    required this.namaPengguna,
    required this.noTlpPengguna,
    required this.emailPengguna,
    required this.namaBarang,
    required this.jenis,
    required this.totalPeminjaman,
    required this.totalHarga,
    required this.keperluan,
    required this.statusReservasi,
    required this.notes,
    required this.tanggalMulaiReservasi,
    required this.tanggalSelesaiReservasi,
  });

  factory ReservasiDetailModel.fromJson(Map<String, dynamic> json) => ReservasiDetailModel(
    idReservasi: int.tryParse(json['id_reservasi']?.toString() ?? '') ?? 0,
    namaPengguna: json['nama_pengguna']?.toString() ?? '',
    noTlpPengguna: json['no_tlp_pengguna']?.toString() ?? '',
    emailPengguna: json['email_pengguna']?.toString() ?? '',
    namaBarang: json['nama_barang']?.toString() ?? '',
    jenis: json['jenis']?.toString() ?? '',
    totalPeminjaman: int.tryParse(json['total_peminjaman']?.toString() ?? '') ?? 0,
    totalHarga: int.tryParse(json['total_harga']?.toString() ?? '') ?? 0,
    keperluan: json['keperluan']?.toString() ?? '',
    statusReservasi: json['status_reservasi']?.toString() ?? '',
    notes: json['notes']?.toString() ?? '',
    tanggalMulaiReservasi: json['tanggal_mulai_reservasi']?.toString() ?? '',
    tanggalSelesaiReservasi: json['tanggal_selesai_reservasi']?.toString() ?? '',
  );

  final int idReservasi;
  final String namaPengguna;
  final String noTlpPengguna;
  final String emailPengguna;
  final String namaBarang;
  final String jenis;
  final int totalPeminjaman;
  final int totalHarga;
  final String keperluan;
  final String statusReservasi;
  final String notes;
  final String tanggalMulaiReservasi;
  final String tanggalSelesaiReservasi;
}

// ─── Food Court ──────────────────────────────────────────────────────────────

class FoodCourtModel {
  FoodCourtModel({
    required this.idFoodCourt,
    required this.namaMenu,
    required this.deskripsi,
    required this.foto,
  });

  factory FoodCourtModel.fromJson(Map<String, dynamic> json) => FoodCourtModel(
    idFoodCourt: json['id_food_court']?.toString() ?? '',
    namaMenu: json['nama_menu']?.toString() ?? '',
    deskripsi: json['deskripsi']?.toString() ?? '',
    foto: json['foto']?.toString() ?? '',
  );

  final String idFoodCourt;
  final String namaMenu;
  final String deskripsi;
  final String foto;
}

