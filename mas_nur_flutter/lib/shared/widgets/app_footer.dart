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
            color: kColorPrimaryDark.withValues(alpha: 0.06),
            blurRadius: 6,
            offset: const Offset(0, -1),
          ),
        ],
      ),
      padding: EdgeInsets.only(
        top: 8,
        bottom: MediaQuery.of(context).padding.bottom + 6,
        left: 16,
        right: 16,
      ),
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
        behavior: HitTestBehavior.opaque,
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 200),
          curve: Curves.easeInOut,
          padding: const EdgeInsets.symmetric(vertical: 7, horizontal: 6),
          decoration: BoxDecoration(
            // Pill highlight hanya untuk item aktif
            color: isActive
                ? kColorPrimary.withValues(alpha: 0.08)
                : Colors.transparent,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              // Icon dengan animasi scale
              AnimatedScale(
                scale: isActive ? 1.1 : 1.0,
                duration: const Duration(milliseconds: 200),
                child: Icon(
                  icon,
                  size: 22,
                  color: isActive ? kColorPrimary : kColorGrey,
                ),
              ),
              const SizedBox(height: 4),
              // Label
              Text(
                label,
                style: TextStyle(
                  fontSize: 10,
                  fontWeight:
                      isActive ? FontWeight.w700 : FontWeight.w400,
                  color: isActive ? kColorPrimary : kColorGrey,
                  letterSpacing: 0.3,
                ),
                textAlign: TextAlign.center,
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
              const SizedBox(height: 3),
              // Garis indikator bawah — tipis & futuristik
              AnimatedContainer(
                duration: const Duration(milliseconds: 200),
                height: 2,
                width: isActive ? 22 : 0,
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
