import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/acara/acara_page.dart';
import 'package:mas_nur_flutter/features/acara/acara_form_page.dart';
import 'package:mas_nur_flutter/features/acara/detail_acara_page.dart';
import 'package:mas_nur_flutter/features/auth/konfirmasi_email_page.dart';
import 'package:mas_nur_flutter/features/auth/kode_otp_page.dart';
import 'package:mas_nur_flutter/features/auth/login_page.dart';
import 'package:mas_nur_flutter/features/auth/sandi_baru_page.dart';
import 'package:mas_nur_flutter/features/berita/berita_form_page.dart';
import 'package:mas_nur_flutter/features/berita/berita_page.dart';
import 'package:mas_nur_flutter/features/berita/detail_berita_page.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/features/informasi/edit_profil_masjid_page.dart';
import 'package:mas_nur_flutter/features/informasi/edit_struktur_organisasi_page.dart';
import 'package:mas_nur_flutter/features/informasi/informasi_masjid_page.dart';
import 'package:mas_nur_flutter/features/notifikasi/notifikasi_page.dart';
import 'package:mas_nur_flutter/features/persewaan/barang_form_page.dart';
import 'package:mas_nur_flutter/features/persewaan/detail_barang_page.dart';
import 'package:mas_nur_flutter/features/persewaan/pemesanan_page.dart';
import 'package:mas_nur_flutter/features/persewaan/persewaan_page.dart';
import 'package:mas_nur_flutter/features/persewaan/reservasi_detail_page.dart';
import 'package:mas_nur_flutter/features/profil/ganti_nama_page.dart';
import 'package:mas_nur_flutter/features/profil/ganti_sandi_page.dart';
import 'package:mas_nur_flutter/features/profil/profil_admin_page.dart';

void runMasNurApp() {
  runApp(const MasNurApp());
}

class MasNurApp extends StatelessWidget {
  const MasNurApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Mas Nur Mobile',
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.green),
      ),
      initialRoute: DashboardPage.routeName, // TODO: kembalikan ke LoginPage.routeName setelah selesai cek
      routes: {
        LoginPage.routeName: (_) => const LoginPage(),
        KonfirmasiEmailPage.routeName: (_) => const KonfirmasiEmailPage(),
        KodeOtpPage.routeName: (_) => const KodeOtpPage(),
        SandiBaruPage.routeName: (_) => const SandiBaruPage(),
        DashboardPage.routeName: (_) => const DashboardPage(),
        BeritaPage.routeName: (_) => const BeritaPage(),
        BeritaFormPage.routeName: (_) => const BeritaFormPage(),
        DetailBeritaPage.routeName: (_) => const DetailBeritaPage(),
        AcaraPage.routeName: (_) => const AcaraPage(),
        AcaraFormPage.routeName: (_) => const AcaraFormPage(),
        DetailAcaraPage.routeName: (_) => const DetailAcaraPage(),
        PersewaanPage.routeName: (_) => const PersewaanPage(),
        BarangFormPage.routeName: (_) => const BarangFormPage(),
        DetailBarangPage.routeName: (_) => const DetailBarangPage(),
        PemesananPage.routeName: (_) => const PemesananPage(),
        ReservasiDetailPage.routeName: (_) => const ReservasiDetailPage(),
        NotifikasiPage.routeName: (_) => const NotifikasiPage(),
        InformasiMasjidPage.routeName: (_) => const InformasiMasjidPage(),
        EditProfilMasjidPage.routeName: (_) => const EditProfilMasjidPage(),
        EditStrukturOrganisasiPage.routeName: (_) => const EditStrukturOrganisasiPage(),
        ProfilAdminPage.routeName: (_) => const ProfilAdminPage(),
        GantiNamaPage.routeName: (_) => const GantiNamaPage(),
        GantiSandiPage.routeName: (_) => const GantiSandiPage(),
      },
    );
  }
}

