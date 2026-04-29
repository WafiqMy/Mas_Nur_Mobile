import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/acara/acara_page.dart';
import 'package:mas_nur_flutter/features/berita/berita_page.dart';
import 'package:mas_nur_flutter/features/food_court/food_court_page.dart';
import 'package:mas_nur_flutter/features/informasi/informasi_masjid_page.dart';
import 'package:mas_nur_flutter/features/persewaan/pemesanan_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_drawer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_gradient_background.dart';
import 'package:mas_nur_flutter/shared/widgets/app_header.dart';

class DashboardPage extends StatelessWidget {
  const DashboardPage({super.key});
  static const routeName = '/dashboard';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.transparent,
      drawer: const AppDrawer(),
      body: AppGradientBackground(
        child: Column(
        children: [
          const AppHeader(),
          Expanded(
            child: SingleChildScrollView(
              padding:
                  const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Greeting
                  Text(
                    'Halaman Utama',
                    style: TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                      color: kColorWhite,
                      letterSpacing: 0.5,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    'Kelola konten masjid dengan mudah',
                    style: TextStyle(
                        fontSize: 13,
                        color: kColorSkyBlue.withOpacity(0.7)),
                  ),
                  const SizedBox(height: 20),

                  // Status Pemesanan
                  _SectionLabel(label: 'Status Pemesanan Terbaru'),
                  const SizedBox(height: 10),
                  _StatusCard(
                    nomor: '1',
                    judul: 'Pemesanan Gedung',
                    accentColor: kColorSkyBlue,
                    atasNama: 'Atas Nama: -',
                    onLihat: () => Navigator.pushNamed(
                        context, PemesananPage.routeName),
                  ),
                  const SizedBox(height: 8),
                  _StatusCard(
                    nomor: '2',
                    judul: 'Permintaan Alat',
                    accentColor: kColorYellow,
                    atasNama: 'Atas Nama: -',
                    onLihat: () => Navigator.pushNamed(
                        context, PemesananPage.routeName),
                  ),
                  const SizedBox(height: 24),

                  // Grid Menu
                  _SectionLabel(label: 'Kelola Fitur'),
                  const SizedBox(height: 10),
                  GridView.count(
                    crossAxisCount: 2,
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    crossAxisSpacing: 12,
                    mainAxisSpacing: 12,
                    childAspectRatio: 0.95,
                    children: [
                      _MenuCard(
                        icon: Icons.calendar_today_outlined,
                        label: 'Acara',
                        onKelola: () => Navigator.pushNamed(
                            context, AcaraPage.routeName),
                      ),
                      _MenuCard(
                        icon: Icons.mosque_outlined,
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
                        onKelola: () => Navigator.pushNamed(
                            context, BeritaPage.routeName),
                      ),
                      _MenuCard(
                        icon: Icons.restaurant_menu_outlined,
                        label: 'Food Court',
                        onKelola: () => Navigator.pushNamed(
                            context, FoodCourtPage.routeName),
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                ],
              ),
            ),
          ),
          const AppFooter(currentIndex: 0),
        ],
      ),
      ),
    );
  }
}

class _SectionLabel extends StatelessWidget {
  const _SectionLabel({required this.label});
  final String label;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Container(
          width: 3,
          height: 16,
          decoration: BoxDecoration(
            color: kColorSkyBlue,
            borderRadius: BorderRadius.circular(2),
          ),
        ),
        const SizedBox(width: 8),
        Text(
          label,
          style: const TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.w600,
            color: kColorWhiteSoft,
            letterSpacing: 0.3,
          ),
        ),
      ],
    );
  }
}

class _StatusCard extends StatelessWidget {
  const _StatusCard({
    required this.nomor,
    required this.judul,
    required this.accentColor,
    required this.atasNama,
    required this.onLihat,
  });

  final String nomor;
  final String judul;
  final Color accentColor;
  final String atasNama;
  final VoidCallback onLihat;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: kColorNavyLight,
        borderRadius: BorderRadius.circular(kCardRadius),
        border: Border.all(color: accentColor.withOpacity(0.25), width: 1),
      ),
      child: Row(
        children: [
          Container(
            width: 36,
            height: 36,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: accentColor.withOpacity(0.12),
              border:
                  Border.all(color: accentColor.withOpacity(0.3), width: 1),
            ),
            child: Center(
              child: Text(
                nomor,
                style: TextStyle(
                    color: accentColor,
                    fontWeight: FontWeight.bold,
                    fontSize: 14),
              ),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(judul,
                    style: TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w600,
                        color: accentColor)),
                const SizedBox(height: 2),
                Text(atasNama,
                    style: const TextStyle(
                        fontSize: 12, color: kColorWhiteSoft)),
              ],
            ),
          ),
          ElevatedButton(
            onPressed: onLihat,
            style: ElevatedButton.styleFrom(
              backgroundColor: accentColor.withOpacity(0.15),
              foregroundColor: accentColor,
              elevation: 0,
              padding:
                  const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8),
                side: BorderSide(
                    color: accentColor.withOpacity(0.3), width: 1),
              ),
              textStyle: const TextStyle(
                  fontSize: 13, fontWeight: FontWeight.w600),
            ),
            child: const Text('Lihat'),
          ),
        ],
      ),
    );
  }
}

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
    return GestureDetector(
      onTap: onKelola,
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: kColorNavyLight,
          borderRadius: BorderRadius.circular(kCardRadius),
          border: Border.all(
              color: kColorSkyBlue.withOpacity(0.18), width: 1),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: 52,
              height: 52,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: kColorRoyal.withOpacity(0.15),
                border: Border.all(
                    color: kColorSkyBlue.withOpacity(0.3), width: 1),
              ),
              child: Icon(icon, size: 26, color: kColorSkyBlue),
            ),
            const SizedBox(height: 10),
            Text(
              label,
              style: const TextStyle(
                fontWeight: FontWeight.w600,
                fontSize: 13,
                color: kColorWhite,
              ),
              textAlign: TextAlign.center,
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ),
            const SizedBox(height: 10),
            Container(
              width: double.infinity,
              height: 34,
              decoration: BoxDecoration(
                color: kColorRoyal.withOpacity(0.2),
                borderRadius: BorderRadius.circular(8),
                border: Border.all(
                    color: kColorRoyal.withOpacity(0.4), width: 1),
              ),
              child: const Center(
                child: Text(
                  'Kelola',
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                    color: kColorSkyBlue,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
