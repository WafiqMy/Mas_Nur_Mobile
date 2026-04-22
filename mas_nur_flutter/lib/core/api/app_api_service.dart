import 'dart:io';

import 'package:dio/dio.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';

class AppApiService {
  AppApiService._();

  static const String baseUrl = 'https://masnurhudanganjuk.pbltifnganjuk.com/';

  static final Dio _dio = Dio(
    BaseOptions(
      baseUrl: baseUrl,
      connectTimeout: const Duration(seconds: 30),
      receiveTimeout: const Duration(seconds: 30),
      sendTimeout: const Duration(seconds: 30),
      headers: {'Accept': 'application/json'},
    ),
  );

  static Future<ApiStatusResponse> loginAdmin(String username, String password) async {
    final response = await _dio.post(
      'login_admin1.php',
      data: {'username': username, 'password': password},
      options: Options(
        // Terima semua status code agar bisa parse error body sendiri
        validateStatus: (status) => status != null && status < 600,
        contentType: Headers.formUrlEncodedContentType,
      ),
    );
    // Response bisa berupa Map atau String JSON
    final data = response.data;
    if (data is Map<String, dynamic>) {
      return ApiStatusResponse.fromJson(data);
    }
    // Jika server mengembalikan string mentah
    if (data is String) {
      if (data.contains('success')) {
        return ApiStatusResponse(status: 'success', message: 'Login berhasil');
      }
      return ApiStatusResponse(status: 'error', message: data);
    }
    return ApiStatusResponse(status: 'error', message: 'Response tidak dikenali');
  }

  static Future<UserProfileModel?> getUserProfile(String username) async {
    final response = await _dio.get('API/profil_admin.php', queryParameters: {'username': username});
    final data = response.data['data'];
    if (data is Map<String, dynamic>) {
      return UserProfileModel.fromJson(data);
    }
    return null;
  }

  static Future<ApiStatusResponse> gantiNama({
    required String username,
    required String newName,
  }) async {
    final response = await _dio.post('API/profil_admin.php', data: {'username': username, 'new_name': newName});
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ApiStatusResponse> gantiPassword({
    required String username,
    required String currentPassword,
    required String newPassword,
  }) async {
    final response = await _dio.post('API/profil_admin.php', data: {
      'username': username,
      'current_password': currentPassword,
      'new_password': newPassword,
    });
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ApiStatusResponse> sendOtp(String email) async {
    final response = await _dio.post('send_otp.php', data: {'email': email});
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ApiStatusResponse> verifyOtp(String email, String otp) async {
    final response = await _dio.post(
      'verifikasi_otp.php',
      data: {'email': email, 'otp': otp},
    );
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ApiStatusResponse> resetPassword(String email, String password) async {
    final response = await _dio.post(
      'reset_password.php',
      data: {'email': email, 'password': password},
    );
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<List<BeritaModel>> getBerita() async {
    final response = await _dio.get('API/api_get_berita1.php');
    final list = (response.data['data'] as List<dynamic>? ?? <dynamic>[]);
    return list.map((e) => BeritaModel.fromJson(e as Map<String, dynamic>)).toList();
  }

  static Future<ApiStatusResponse> hapusBerita(String idBerita) async {
    final response = await _dio.post('API/api_hapus_berita1.php', data: {'id_berita': idBerita});
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ApiStatusResponse> tambahBerita({
    required String judul,
    required String isi,
    required String tanggal,
    required String username,
    File? fotoFile,
  }) async {
    final formData = FormData.fromMap({
      'judul_berita': judul,
      'isi_berita': isi,
      'tanggal_berita': tanggal,
      'username': username,
      if (fotoFile != null) 'foto_berita': await MultipartFile.fromFile(fotoFile.path),
    });
    final response = await _dio.post('API/api_tambah_berita1.php', data: formData);
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ApiStatusResponse> editBerita({
    required String idBerita,
    required String judul,
    required String isi,
    required String tanggal,
    required String username,
    File? fotoFile,
  }) async {
    final formData = FormData.fromMap({
      'id_berita': idBerita,
      'judul_berita': judul,
      'isi_berita': isi,
      'tanggal_berita': tanggal,
      'username': username,
      if (fotoFile != null) 'foto_berita': await MultipartFile.fromFile(fotoFile.path),
    });
    final response = await _dio.post('API/api_edit_berita.php', data: formData);
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<List<AcaraModel>> getAcara() async {
    final response = await _dio.get('API/api_get_event1.php');
    final list = (response.data['data'] as List<dynamic>? ?? <dynamic>[]);
    return list.map((e) => AcaraModel.fromJson(e as Map<String, dynamic>)).toList();
  }

  static Future<ApiStatusResponse> hapusAcara(String idEvent) async {
    final response = await _dio.post('API/api_hapus_event.php', data: {'id_event': idEvent});
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ApiStatusResponse> tambahAcara({
    required String namaEvent,
    required String tanggalEvent,
    required String deskripsiEvent,
    required String lokasiEvent,
    required String username,
    File? gambarFile,
  }) async {
    final formData = FormData.fromMap({
      'nama_event': namaEvent,
      'tanggal_event': tanggalEvent,
      'deskripsi_event': deskripsiEvent,
      'lokasi_event': lokasiEvent,
      'username': username,
      if (gambarFile != null) 'gambar_event': await MultipartFile.fromFile(gambarFile.path),
    });
    final response = await _dio.post('API/api_tambah_acara.php', data: formData);
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ApiStatusResponse> editAcara({
    required String idEvent,
    required String namaEvent,
    required String tanggalEvent,
    required String deskripsiEvent,
    required String lokasiEvent,
    required String username,
    File? gambarFile,
  }) async {
    final formData = FormData.fromMap({
      'id_event': idEvent,
      'nama_event': namaEvent,
      'tanggal_event': tanggalEvent,
      'deskripsi_event': deskripsiEvent,
      'lokasi_event': lokasiEvent,
      'username': username,
      if (gambarFile != null) 'gambar_event': await MultipartFile.fromFile(gambarFile.path),
    });
    final response = await _dio.post('API/api_edit_event.php', data: formData);
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<List<BarangModel>> getBarang() async {
    final response = await _dio.get('API/api_barang.php');
    final list = (response.data as List<dynamic>? ?? <dynamic>[]);
    return list.map((e) => BarangModel.fromJson(e as Map<String, dynamic>)).toList();
  }

  static Future<ApiStatusResponse> hapusBarang(int idPersewaan) async {
    final response = await _dio.post(
      'API/api_hapus_barang.php',
      data: {'id_persewaan': idPersewaan},
    );
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ApiStatusResponse> tambahBarang({
    required String namaBarang,
    required String jenis,
    required String harga,
    required String jumlah,
    required String deskripsi,
    required String spesifikasi,
    required String fasilitas,
    File? gambarFile,
  }) async {
    final formData = FormData.fromMap({
      'nama_barang': namaBarang,
      'Jenis': jenis,
      'harga': harga,
      'jumlah': jumlah,
      'deskripsi': deskripsi,
      'spesifikasi': spesifikasi,
      'fasilitas': fasilitas,
      if (gambarFile != null) 'gambar': await MultipartFile.fromFile(gambarFile.path),
    });
    final response = await _dio.post('API/api_tambah_barang.php', data: formData);
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ApiStatusResponse> editBarang({
    required int idPersewaan,
    required String namaBarang,
    required String jenis,
    required String harga,
    required String jumlah,
    required String deskripsi,
    required String spesifikasi,
    required String fasilitas,
    File? gambarFile,
  }) async {
    final formData = FormData.fromMap({
      'id_persewaan': idPersewaan.toString(),
      'nama_barang': namaBarang,
      'Jenis': jenis,
      'harga': harga,
      'jumlah': jumlah,
      'deskripsi': deskripsi,
      'spesifikasi': spesifikasi,
      'fasilitas': fasilitas,
      if (gambarFile != null) 'gambar': await MultipartFile.fromFile(gambarFile.path),
    });
    final response = await _dio.post('API/api_edit_barang.php', data: formData);
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<List<NotificationItem>> getNotifications() async {
    final response = await _dio.get('get_notifikasi.php');
    final list = (response.data['data'] as List<dynamic>? ?? <dynamic>[]);
    return list.map((e) => NotificationItem.fromJson(e as Map<String, dynamic>)).toList();
  }

  static Future<List<ReservasiItemModel>> getAllReservasi() async {
    final response = await _dio.get('API/api_detail_reservasi.php');
    final list = (response.data as List<dynamic>? ?? <dynamic>[]);
    return list.map((e) => ReservasiItemModel.fromJson(e as Map<String, dynamic>)).toList();
  }

  static Future<ReservasiDetailModel?> getReservasiDetail(int id) async {
    final response = await _dio.get('API/api_detail_reservasi.php', queryParameters: {'id': id});
    final data = response.data['data'];
    if (data is Map<String, dynamic>) {
      return ReservasiDetailModel.fromJson(data);
    }
    return null;
  }

  static Future<ApiStatusResponse> updateStatusReservasi({
    required int id,
    required String status,
    String? notes,
  }) async {
    final payload = <String, dynamic>{'id': id, 'status': status};
    if (notes != null && notes.isNotEmpty) {
      payload['notes'] = notes;
    }
    final response = await _dio.post('API/api_update_status.php', data: payload);
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<ProfilMasjidModel?> getProfilMasjid() async {
    final response = await _dio.get('API/api_profil_masjid.php');
    final data = response.data['data'];
    if (data is Map<String, dynamic>) {
      return ProfilMasjidModel.fromJson(data);
    }
    return null;
  }

  static Future<ApiStatusResponse> updateProfilMasjid({
    required String judul,
    required String deskripsi,
    File? gambarFile,
  }) async {
    final formData = FormData.fromMap({
      'judul_sejarah': judul,
      'deskripsi_sejarah': deskripsi,
      'username': 'admin',
      if (gambarFile != null)
        'gambar_sejarah_masjid': await MultipartFile.fromFile(gambarFile.path),
    });
    final response = await _dio.post('API/api_edit_profil_masjid.php', data: formData);
    return ApiStatusResponse.fromJson(response.data);
  }

  static Future<StrukturOrganisasiModel?> getStrukturOrganisasi() async {
    final response = await _dio.get('API/api_struktur.php');
    final data = response.data['data'];
    if (data is Map<String, dynamic>) {
      return StrukturOrganisasiModel.fromJson(data);
    }
    return null;
  }

  static Future<ApiStatusResponse> updateStrukturOrganisasi({
    File? gambarPengurusFile,
    File? gambarRemasFile,
  }) async {
    final formData = FormData.fromMap({
      'username': 'admin',
      if (gambarPengurusFile != null)
        'gambar_struktur_organisasi': await MultipartFile.fromFile(gambarPengurusFile.path),
      if (gambarRemasFile != null)
        'gambar_struktur_remas': await MultipartFile.fromFile(gambarRemasFile.path),
    });
    final response = await _dio.post('API/api_edit_struktur.php', data: formData);
    return ApiStatusResponse.fromJson(response.data);
  }
}

