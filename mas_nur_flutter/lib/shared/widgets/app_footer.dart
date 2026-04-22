import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/acara/acara_page.dart';
import 'package:mas_nur_flutter/features/berita/berita_page.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/features/informasi/informasi_masjid_page.dart';
import 'package:mas_nur_flutter/features/persewaan/pemesanan_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';

/// Footer persis seperti footer.xml:
/// [Beranda] [Acara] [Informasi Masjid] [Pemesanan] [Berita]
/// Background: #99D5F9, paddingVertical 15dp
class AppFooter extends StatelessWidget {
  const AppFooter({super.key, this.currentIndex = 0});

  final int currentIndex;

  @override
  Widget build(BuildContext context) {
    return Container(
      color: kColorHeader,
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        children: [
          _FooterItem(
            icon: Icons.home,
            label: 'Beranda',
            isActive: currentIndex == 0,
            onTap: () => Navigator.pushNamedAndRemoveUntil(
              context, DashboardPage.routeName, (r) => false),
          ),
          _FooterItem(
            icon: Icons.calendar_today,
            label: 'Acara',
            isActive: currentIndex == 1,
            onTap: () => Navigator.pushNamed(context, AcaraPage.routeName),
          ),
          _FooterItem(
            icon: Icons.info_outline,
            label: 'Informasi Masjid',
            isActive: currentIndex == 2,
            onTap: () => Navigator.pushNamed(context, InformasiMasjidPage.routeName),
          ),
          _FooterItem(
            icon: Icons.inventory_2_outlined,
            label: 'Pemesanan',
            isActive: currentIndex == 3,
            onTap: () => Navigator.pushNamed(context, PemesananPage.routeName),
          ),
          _FooterItem(
            icon: Icons.article_outlined,
            label: 'Berita',
            isActive: currentIndex == 4,
            onTap: () => Navigator.pushNamed(context, BeritaPage.routeName),
          ),
        ],
      ),
    );
  }
}

class _FooterItem extends StatelessWidget {
  const _FooterItem({
    required this.icon,
    required this.label,
    required this.onTap,
    this.isActive = false,
  });

  final IconData icon;
  final String label;
  final VoidCallback onTap;
  final bool isActive;

  @override
  Widget build(BuildContext context) {
    final color = isActive ? Colors.blue[800]! : Colors.black;
    return Expanded(
      child: GestureDetector(
        onTap: onTap,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(icon, size: 25, color: color),
            const SizedBox(height: 2),
            Text(
              label,
              style: TextStyle(fontSize: 10, color: color),
              textAlign: TextAlign.center,
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
          ],
        ),
      ),
    );
  }
}
