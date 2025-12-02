package com.example.masnur.Api;

import com.example.masnur.Fitur_Acara.AcaraListResponse;
import com.example.masnur.Fitur_Acara.AcaraModel;
import com.example.masnur.Fitur_Berita.BeritaListResponse;
import com.example.masnur.Fitur_Berita.BeritaModel;
import com.example.masnur.Fitur_Informasi_Masjid.ProfilMasjidResponse;
import com.example.masnur.Fitur_Informasi_Masjid.StrukturOrganisasiResponse;
import com.example.masnur.Fitur_Masuk.LoginResponse;
import com.example.masnur.Fitur_Notifikasi.NotifResponse;
import com.example.masnur.Fitur_Masuk.OtpResponse;
import com.example.masnur.Fitur_Masuk.ResetResponse;
import com.example.masnur.Fitur_Persewaan.BarangModel;
import com.example.masnur.Fitur_Persewaan.ReservasiDetailResponse;
import com.example.masnur.Fitur_Persewaan.ReservasiItemModel;
import com.example.masnur.Fitur_Persewaan.ReservasiResponse;  // ✅ TAMBAHKAN INI
import com.example.masnur.Fitur_Berita.BeritaResponse;  // ✅ TAMBAHKAN INI
import com.example.masnur.Fitur_Acara.AcaraResponse;
import com.example.masnur.Fitur_Profil_Admin.UserProfileResponse;
import com.google.gson.JsonObject;

import java.util.List;  // ✅ WAJIB
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiService {

    @GET("get_notifikasi.php")
    Call<NotifResponse> getNotifications();

    @GET("API/api_get_berita1.php")
    Call<BeritaListResponse> getBerita();

    @GET("API/api_barang.php")
    Call<List<BarangModel>> getBarang();


    @FormUrlEncoded
    @POST("login_admin1.php")
    Call<LoginResponse> loginAdmin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("send_otp.php")
    Call<OtpResponse> sendOtp(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("verifikasi_otp.php")
    Call<OtpResponse> verifyOtp(
            @Field("email") String email,
            @Field("otp") String otp
    );

    @FormUrlEncoded
    @POST("reset_password.php")
    Call<ResetResponse> resetPassword(
            @Field("email") String email,
            @Field("password") String password
    );
    // Tambahkan ini di bawah interface yang ada

    // Tambahkan di interface ApiService.java — gantikan yang lama

    // ✅ TAMBAH BARANG — 7 field teks + gambar
    @Multipart
    @POST("API/api_tambah_barang.php")
    Call<ReservasiResponse> tambahBarang(
            @Part("nama_barang") RequestBody namaBarang,
            @Part("Jenis") RequestBody jenis,
            @Part("harga") RequestBody harga,
            @Part("jumlah") RequestBody jumlah,
            // 🔹 TAMBAHAN
            @Part("deskripsi") RequestBody deskripsi,
            @Part("spesifikasi") RequestBody spesifikasi,
            @Part("fasilitas") RequestBody fasilitas,
            @Part MultipartBody.Part gambar  // opsional
    );

    // ✅ EDIT BARANG — 8 field teks (termasuk id) + gambar
    @Multipart
    @POST("API/api_edit_barang.php")
    Call<ReservasiResponse> editBarang(
            @Part("id_persewaan") RequestBody idPersewaan,
            @Part("nama_barang") RequestBody namaBarang,
            @Part("Jenis") RequestBody jenis,
            @Part("harga") RequestBody harga,
            @Part("jumlah") RequestBody jumlah,
            // 🔹 TAMBAHAN
            @Part("deskripsi") RequestBody deskripsi,
            @Part("spesifikasi") RequestBody spesifikasi,
            @Part("fasilitas") RequestBody fasilitas,
            @Part MultipartBody.Part gambar  // opsional
    );
    // ✅ Hapus Barang
    @FormUrlEncoded
    @POST("API/api_hapus_barang.php")
    Call<ReservasiResponse> hapusBarang(
            @Field("id_persewaan") int idPersewaan
    );
    // Tambahkan di ApiService.java

    @Multipart
    @POST("API/api_tambah_berita1.php")
    Call<BeritaResponse> tambahBerita(
            @Part("judul_berita") RequestBody judul,
            @Part("isi_berita") RequestBody isi,
            @Part("tanggal_berita") RequestBody tanggal,
            @Part("username") RequestBody username,
            @Part MultipartBody.Part foto_berita
    );

    @Multipart
    @POST("API/api_edit_berita.php")
    Call<BeritaResponse> editBerita(
            @Part("id_berita") RequestBody idBerita,
            @Part("judul_berita") RequestBody judul,
            @Part("isi_berita") RequestBody isi,
            @Part("tanggal_berita") RequestBody tanggal,
            @Part("username") RequestBody username,
            @Part MultipartBody.Part foto_berita
    );

    @FormUrlEncoded
    @POST("API/api_hapus_berita1.php")
    Call<BeritaResponse> hapusBerita(@Field("id_berita") String idBerita);


    @GET("API/api_get_event1.php")
    Call<AcaraListResponse> getAcara();

    @Multipart
    @POST("API/api_tambah_acara.php")
    Call<AcaraResponse> tambahAcara(
            @Part("nama_event") RequestBody namaEvent,
            @Part("tanggal_event") RequestBody tanggalEvent,
            @Part("deskripsi_event") RequestBody deskripsiEvent,
            @Part("lokasi_event") RequestBody lokasiEvent,
            @Part("username") RequestBody username,
            @Part MultipartBody.Part gambar_event  // ✅ field name: gambar_event
    );
    // TAMBAHKAN INI DI DALAM INTERFACE ApiService

    @Multipart
    @POST("API/api_tambah_acara.php")
    Call<AcaraResponse> tambahAcaraMulti(
            @Part("nama_event") RequestBody namaEvent,
            @Part("tanggal_event") RequestBody tanggalEvent,
            @Part("deskripsi_event") RequestBody deskripsiEvent,
            @Part("lokasi_event") RequestBody lokasiEvent,
            @Part("username") RequestBody username,
            @Part MultipartBody.Part gambar_event,
            @Part List<MultipartBody.Part> dokumentasi,
            @Part("video_urls") RequestBody video_urls
    );

    @Multipart
    @POST("API/api_edit_event.php")
    Call<AcaraResponse> editAcara(
            @Part("id_event") RequestBody idEvent,
            @Part("nama_event") RequestBody namaEvent,
            @Part("tanggal_event") RequestBody tanggalEvent,
            @Part("deskripsi_event") RequestBody deskripsiEvent,
            @Part("lokasi_event") RequestBody lokasiEvent,
            @Part("username") RequestBody username,
            @Part MultipartBody.Part gambar_event  // ✅
    );

    @FormUrlEncoded
    @POST("API/api_hapus_event.php")
    Call<AcaraResponse> hapusAcara(@Field("id_event") String idEvent);

    @GET("API/api_profil_masjid.php")
    Call<ProfilMasjidResponse> getProfilMasjid();

    @Multipart
    @POST("API/api_edit_profil_masjid.php")
    Call<ProfilMasjidResponse> updateProfilMasjid(
            @Part("judul_sejarah") RequestBody judulSejarah,
            @Part("deskripsi_sejarah") RequestBody deskripsiSejarah,
            @Part("username") RequestBody username,
            @Part MultipartBody.Part gambarSejarahMasjid
    );
    // Ganti di ApiService.java:

    @GET("API/api_struktur.php")
    Call<StrukturOrganisasiResponse> getStrukturOrganisasi();

    @Multipart
    @POST("API/api_edit_struktur.php")
    Call<StrukturOrganisasiResponse> updateStrukturOrganisasi(
            @Part MultipartBody.Part gambarStrukturOrganisasi,
            @Part MultipartBody.Part gambarStrukturRemas,
            @Part("username") RequestBody username
    );

//    @FormUrlEncoded
//    @POST("API/api_hapus_struktur.php")
//    Call<StrukturOrganisasiResponse> hapusGambarStruktur(
//            @Field("jenis") String jenis  // "organisasi" atau "remas"
//    );

    @FormUrlEncoded
    @POST("API/profil_admin.php")
    Call<ReservasiResponse> profilAction(
            @Field("username") String username,
            @Field("action") String action,
            @Field("new_name") String new_name,
            @Field("current_password") String current_password,
            @Field("new_password") String new_password
    );

    @GET("API/api_detail_reservasi.php")
    Call<List<ReservasiItemModel>> getAllReservasi();

    @GET("API/api_detail_reservasi.php")
    Call<ReservasiDetailResponse> getReservasiDetail(@Query("id") int id);

    @POST("API/api_update_status.php")
    Call<ReservasiResponse> updateStatusReservasi(@retrofit2.http.Body JsonObject body);
    // Di ApiService.java — tambahkan ini di dalam interface

    @GET("API/api_detail_reservasi.php")
    Call<List<ReservasiItemModel>> getReservasiMenunggu();

    // --- Profil Admin ---
    @GET("API/profil_admin.php")
    Call<UserProfileResponse> getUserProfile(@Query("username") String username);

    @FormUrlEncoded
    @POST("API/profil_admin.php")
    Call<ReservasiResponse> gantiNama(
            @Field("username") String username,
            @Field("new_name") String new_name
    );

    @FormUrlEncoded
    @POST("API/profil_admin.php")
    Call<ReservasiResponse> gantiPassword(
            @Field("username") String username,
            @Field("current_password") String current_password,
            @Field("new_password") String new_password
    );


}