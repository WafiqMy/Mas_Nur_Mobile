import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/acara/acara_page.dart';
import 'package:mas_nur_flutter/features/berita/berita_page.dart';
import 'package:mas_nur_flutter/features/informasi/informasi_masjid_page.dart';
import 'package:mas_nur_flutter/features/persewaan/pemesanan_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_drawer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_header.dart';

class DashboardPage extends StatelessWidget {
  const DashboardPage({super.key});
  static const routeName = '/dashboard';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kColorWhite,
      drawer: const AppDrawer(),
      body: Column(
        children: [
          // ── Header ──────────────────────────────────────────────────────────
          const AppHeader(),

          // ── Konten ──────────────────────────────────────────────────────────
          Expanded(
            child: SingleChildScrollView(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Judul
                  const Center(
                    child: Text(
                      'Halaman Utama',
                      style: TextStyle(
                        fontSize: 22,
                        fontWeight: FontWeight.bold,
                        color: Colors.black,
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),

                  // Status Pemesanan Terbaru
                  const Text(
                    'Status Pemesanan Terbaru',
                    style: TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.bold,
                      color: Colors.black,
                    ),
                  ),
                  const SizedBox(height: 8),

                  // Card Pemesanan Gedung
                  _StatusCard(
                    nomor: '1.',
                    judul: 'Pemesanan Gedung',
                    judulColor: const Color(0xFF0066CC),
                    atasNama: 'Atas Nama: -',
                    onLihat: () => Navigator.pushNamed(context, PemesananPage.routeName),
                  ),
                  const SizedBox(height: 8),

                  // Card Permintaan Alat
                  _StatusCard(
                    nomor: '2.',
                    judul: 'Permintaan Alat',
                    judulColor: const Color(0xFF996600),
                    atasNama: 'Atas Nama: -',
                    onLihat: () => Navigator.pushNamed(context, PemesananPage.routeName),
                  ),
                  const SizedBox(height: 24),

                  // Grid Menu 2 kolom
                  GridView.count(
                    crossAxisCount: 2,
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    crossAxisSpacing: 12,
                    mainAxisSpacing: 12,
                    childAspectRatio: 0.9,
                    children: [
                      _MenuCard(
                        icon: Icons.calendar_today,
                        label: 'Acara',
                        onKelola: () => Navigator.pushNamed(context, AcaraPage.routeName),
                      ),
                      _MenuCard(
                        icon: Icons.mosque,
                        label: 'Informasi Masjid',
                        onKelola: () => Navigator.pushNamed(
                            context, InformasiMasjidPage.routeName),
                      ),
                      _MenuCard(
                        icon: Icons.inventory_2_outlined,
                        label: 'Pemesanan',
                        onKelola: () => Navigator.pushNamed(
                            context, PemesananPage.routeName),
                      ),
                      _MenuCard(
                        icon: Icons.article_outlined,
                        label: 'Berita',
                        onKelola: () => Navigator.pushNamed(context, BeritaPage.routeName),
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                ],
              ),
            ),
          ),

          // ── Footer ──────────────────────────────────────────────────────────
          const AppFooter(currentIndex: 0),
        ],
      ),
    );
  }
}

// ── Status Card ──────────────────────────────────────────────────────────────
class _StatusCard extends StatelessWidget {
  const _StatusCard({
    required this.nomor,
    required this.judul,
    required this.judulColor,
    required this.atasNama,
    required this.onLihat,
  });

  final String nomor;
  final String judul;
  final Color judulColor;
  final String atasNama;
  final VoidCallback onLihat;

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 3,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Row(
          children: [
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    '$nomor $judul',
                    style: TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                      color: judulColor,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(atasNama,
                      style: const TextStyle(fontSize: 14, color: Color(0xFF444444))),
                ],
              ),
            ),
            ElevatedButton(
              onPressed: onLihat,
              style: ElevatedButton.styleFrom(
                backgroundColor: kColorKembali,
                foregroundColor: kColorTextButton,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(kButtonRadius)),
                elevation: 0,
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                textStyle: const TextStyle(fontSize: 16),
              ),
              child: const Text('Lihat'),
            ),
          ],
        ),
      ),
    );
  }
}

// ── Menu Card ────────────────────────────────────────────────────────────────
class _MenuCard extends StatelessWidget {
  const _MenuCard({
    required this.icon,
    required this.label,
    required this.onKelola,
  });

  final IconData icon;
  final String label;
  final VoidCallback onKelola;

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 3,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 40, color: Colors.black87),
            const SizedBox(height: 8),
            Text(
              label,
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 8),
            SizedBox(
              width: double.infinity,
              height: 40,
              child: ElevatedButton(
                onPressed: onKelola,
                style: ElevatedButton.styleFrom(
                  backgroundColor: kColorButton,
                  foregroundColor: kColorTextButton,
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(kButtonRadius)),
                  elevation: 0,
                  textStyle: const TextStyle(fontSize: 14),
                ),
                child: const Text('Kelola'),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
