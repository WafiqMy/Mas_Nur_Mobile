import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/features/infaq/infaq_page.dart';
import 'package:mas_nur_flutter/features/persewaan/pemesanan_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/utils/app_navigation.dart';

/// Index footer:
/// 0 = Beranda, 1 = Infaq, 2 = Pemesanan
/// -1 = tidak ada yang aktif (halaman dari sidebar)
class AppFooter extends StatelessWidget {
  const AppFooter({super.key, this.currentIndex = 0});
  final int currentIndex;

  void _navigate(BuildContext context, int index) {
    if (index == currentIndex) return;
    Widget page;
    switch (index) {
      case 0:
        page = const DashboardPage();
        break;
      case 1:
        page = const InfaqPage();
        break;
      case 2:
        page = const PemesananPage();
        break;
      default:
        return;
    }
    Navigator.pushReplacement(context, fadeRoute(page));
  }

  @override
  Widget build(BuildContext context) {
    if (currentIndex == -1) return const SizedBox.shrink();

    return Container(
      decoration: BoxDecoration(
        color: kColorWhite,
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.08),
            blurRadius: 12,
            offset: const Offset(0, -3),
          ),
        ],
      ),
      padding: const EdgeInsets.symmetric(vertical: 6, horizontal: 8),
      child: Row(
        children: [
          _FooterItem(
            icon: Icons.home_rounded,
            label: 'Beranda',
            isActive: currentIndex == 0,
            onTap: () => _navigate(context, 0),
          ),
          _FooterItem(
            icon: Icons.volunteer_activism_rounded,
            label: 'Infaq',
            isActive: currentIndex == 1,
            onTap: () => _navigate(context, 1),
          ),
          _FooterItem(
            icon: Icons.inventory_2_rounded,
            label: 'Pemesanan',
            isActive: currentIndex == 2,
            onTap: () => _navigate(context, 2),
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
    return Expanded(
      child: GestureDetector(
        onTap: onTap,
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 200),
          curve: Curves.easeInOut,
          margin: const EdgeInsets.symmetric(horizontal: 4, vertical: 2),
          padding: const EdgeInsets.symmetric(vertical: 8),
          decoration: BoxDecoration(
            color: isActive
                ? kColorPrimary.withValues(alpha: 0.08)
                : Colors.transparent,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              AnimatedScale(
                scale: isActive ? 1.1 : 1.0,
                duration: const Duration(milliseconds: 200),
                child: Icon(
                  icon,
                  size: 24,
                  color: isActive ? kColorPrimary : kColorGrey,
                ),
              ),
              const SizedBox(height: 4),
              Text(
                label,
                style: TextStyle(
                  fontSize: 11,
                  fontWeight:
                      isActive ? FontWeight.w600 : FontWeight.normal,
                  color: isActive ? kColorPrimary : kColorGrey,
                  letterSpacing: 0.2,
                ),
                textAlign: TextAlign.center,
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
              const SizedBox(height: 2),
              AnimatedContainer(
                duration: const Duration(milliseconds: 200),
                height: 3,
                width: isActive ? 24 : 0,
                decoration: BoxDecoration(
                  color: kColorPrimary,
                  borderRadius: BorderRadius.circular(2),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
