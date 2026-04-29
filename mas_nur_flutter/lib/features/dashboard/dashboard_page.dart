import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/acara/acara_page.dart';
import 'package:mas_nur_flutter/features/berita/berita_page.dart';
import 'package:mas_nur_flutter/features/food_court/food_court_page.dart';
import 'package:mas_nur_flutter/features/informasi/informasi_masjid_page.dart';
import 'package:mas_nur_flutter/features/persewaan/pemesanan_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_drawer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_header.dart';
import 'package:mas_nur_flutter/shared/widgets/swipe_page_shell.dart';

class DashboardPage extends StatelessWidget {
  const DashboardPage({super.key});
  static const routeName = '/dashboard';

  @override
  Widget build(BuildContext context) {
    return SwipePageShell(
      currentIndex: 0,
      child: Scaffold(
        backgroundColor: kColorBackground,
        drawer: const AppDrawer(),
        body: Column(
          children: [
            const AppHeader(),
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // ── Banner selamat datang ────────────────────────────
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.fromLTRB(20, 20, 20, 28),
                      decoration: const BoxDecoration(gradient: kAppGradient),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text(
                            'Halaman Utama',
                            style: TextStyle(
                              fontSize: 22,
                              fontWeight: FontWeight.bold,
                              color: kColorWhite,
                            ),
                          ),
                          const SizedBox(height: 4),
                          Text(
                            'Kelola konten masjid dengan mudah',
                            style: TextStyle(
                                fontSize: 13,
                                color: kColorWhite.withValues(alpha: 0.8)),
                          ),
                        ],
                      ),
                    ),

                    // ── Konten utama ─────────────────────────────────────
                    Padding(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          _SectionLabel(label: 'Status Pemesanan Terbaru'),
                          const SizedBox(height: 10),
                          _StatusCard(
                            nomor: '1',
                            judul: 'Pemesanan Gedung',
                            accentColor: kColorPrimary,
                            atasNama: 'Atas Nama: -',
                            onLihat: () => Navigator.pushNamed(
                                context, PemesananPage.routeName),
                          ),
                          const SizedBox(height: 8),
                          _StatusCard(
                            nomor: '2',
                            judul: 'Permintaan Alat',
                            accentColor: kColorGold,
                            atasNama: 'Atas Nama: -',
                            onLihat: () => Navigator.pushNamed(
                                context, PemesananPage.routeName),
                          ),
                          const SizedBox(height: 24),
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
                                color: kColorPrimaryLight,
                                onKelola: () => Navigator.pushNamed(
                                    context, AcaraPage.routeName),
                              ),
                              _MenuCard(
                                icon: Icons.mosque_outlined,
                                label: 'Informasi Masjid',
                                color: kColorPrimary,
                                onKelola: () => Navigator.pushNamed(
                                    context, InformasiMasjidPage.routeName),
                              ),
                              _MenuCard(
                                icon: Icons.inventory_2_outlined,
                                label: 'Pemesanan',
                                color: kColorPrimaryDark,
                                onKelola: () => Navigator.pushNamed(
                                    context, PemesananPage.routeName),
                              ),
                              _MenuCard(
                                icon: Icons.article_outlined,
                                label: 'Berita',
                                color: kColorPrimaryMid,
                                onKelola: () => Navigator.pushNamed(
                                    context, BeritaPage.routeName),
                              ),
                              _MenuCard(
                                icon: Icons.restaurant_menu_outlined,
                                label: 'Food Court',
                                color: const Color(0xFF0288D1),
                                onKelola: () => Navigator.pushNamed(
                                    context, FoodCourtPage.routeName),
                              ),
                            ],
                          ),
                          const SizedBox(height: 16),
                        ],
                      ),
                    ),
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
          width: 4,
          height: 18,
          decoration: BoxDecoration(
            color: kColorPrimary,
            borderRadius: BorderRadius.circular(2),
          ),
        ),
        const SizedBox(width: 8),
        Text(
          label,
          style: const TextStyle(
            fontSize: 15,
            fontWeight: FontWeight.w700,
            color: kColorTextPrimary,
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
        color: kColorWhite,
        borderRadius: BorderRadius.circular(kCardRadius),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        children: [
          Container(
            width: 40,
            height: 40,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: accentColor.withValues(alpha: 0.12),
            ),
            child: Center(
              child: Text(
                nomor,
                style: TextStyle(
                    color: accentColor,
                    fontWeight: FontWeight.bold,
                    fontSize: 16),
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
                        color: kColorTextPrimary)),
                const SizedBox(height: 2),
                Text(atasNama,
                    style: const TextStyle(
                        fontSize: 12, color: kColorTextSecondary)),
              ],
            ),
          ),
          ElevatedButton(
            onPressed: onLihat,
            style: ElevatedButton.styleFrom(
              backgroundColor: accentColor,
              foregroundColor: kColorWhite,
              elevation: 0,
              padding:
                  const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8),
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
    required this.color,
    required this.onKelola,
  });

  final IconData icon;
  final String label;
  final Color color;
  final VoidCallback onKelola;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onKelola,
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: kColorWhite,
          borderRadius: BorderRadius.circular(kCardRadius),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.06),
              blurRadius: 8,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: 56,
              height: 56,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: color.withValues(alpha: 0.12),
              ),
              child: Icon(icon, size: 28, color: color),
            ),
            const SizedBox(height: 12),
            Text(
              label,
              style: const TextStyle(
                fontWeight: FontWeight.w600,
                fontSize: 13,
                color: kColorTextPrimary,
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
                color: color.withValues(alpha: 0.1),
                borderRadius: BorderRadius.circular(8),
              ),
              child: Center(
                child: Text(
                  'Kelola',
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                    color: color,
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
