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
        color: kColorNavy,
        border: Border(
          top: BorderSide(color: kColorSkyBlue.withOpacity(0.2), width: 1),
        ),
      ),
      padding: const EdgeInsets.symmetric(vertical: 4),
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
          margin: const EdgeInsets.symmetric(horizontal: 6, vertical: 4),
          padding: const EdgeInsets.symmetric(vertical: 6),
          decoration: BoxDecoration(
            color: isActive
                ? kColorRoyal.withOpacity(0.18)
                : Colors.transparent,
            borderRadius: BorderRadius.circular(10),
            border: isActive
                ? Border.all(
                    color: kColorSkyBlue.withOpacity(0.35), width: 1)
                : null,
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              AnimatedScale(
                scale: isActive ? 1.1 : 1.0,
                duration: const Duration(milliseconds: 200),
                child: Icon(
                  icon,
                  size: 22,
                  color: isActive ? kColorSkyBlue : kColorGrey,
                ),
              ),
              const SizedBox(height: 3),
              Text(
                label,
                style: TextStyle(
                  fontSize: 10,
                  fontWeight:
                      isActive ? FontWeight.w600 : FontWeight.normal,
                  color: isActive ? kColorSkyBlue : kColorGrey,
                  letterSpacing: 0.3,
                ),
                textAlign: TextAlign.center,
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
              const SizedBox(height: 2),
              AnimatedContainer(
                duration: const Duration(milliseconds: 200),
                height: 2,
                width: isActive ? 20 : 0,
                decoration: BoxDecoration(
                  color: kColorSkyBlue,
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
