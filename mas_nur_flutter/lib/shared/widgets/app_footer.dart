import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/features/persewaan/pemesanan_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/feature_placeholder_page.dart';

/// Footer navigasi bawah: [Beranda] [Infaq] [Pemesanan]
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
            icon: Icons.volunteer_activism,
            label: 'Infaq',
            isActive: currentIndex == 1,
            onTap: () => Navigator.push(
              context,
              MaterialPageRoute(
                builder: (_) => const FeaturePlaceholderPage(
                  title: 'Infaq',
                  subtitle: 'Fitur Infaq akan segera hadir.',
                ),
              ),
            ),
          ),
          _FooterItem(
            icon: Icons.inventory_2_outlined,
            label: 'Pemesanan',
            isActive: currentIndex == 2,
            onTap: () => Navigator.pushNamed(context, PemesananPage.routeName),
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
