import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/features/persewaan/pemesanan_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/feature_placeholder_page.dart';

/// Index footer:
/// 0 = Beranda, 1 = Infaq, 2 = Pemesanan
class AppFooter extends StatelessWidget {
  const AppFooter({super.key, this.currentIndex = 0});

  final int currentIndex;

  void _navigate(BuildContext context, int index) {
    if (index == currentIndex) return; // sudah di halaman ini

    Widget page;
    switch (index) {
      case 0:
        page = const DashboardPage();
        break;
      case 1:
        page = const FeaturePlaceholderPage(
          title: 'Infaq',
          subtitle: 'Fitur Infaq akan segera hadir.',
        );
        break;
      case 2:
        page = const PemesananPage();
        break;
      default:
        return;
    }

    Navigator.pushAndRemoveUntil(
      context,
      PageRouteBuilder(
        pageBuilder: (context, animation, secondaryAnimation) => page,
        transitionsBuilder: (context, animation, secondaryAnimation, child) {
          // Slide dari kanan jika maju, dari kiri jika mundur
          final isForward = index > currentIndex;
          final begin = Offset(isForward ? 1.0 : -1.0, 0.0);
          const end = Offset.zero;
          final tween = Tween(begin: begin, end: end)
              .chain(CurveTween(curve: Curves.easeInOut));
          return SlideTransition(
            position: animation.drive(tween),
            child: FadeTransition(opacity: animation, child: child),
          );
        },
        transitionDuration: const Duration(milliseconds: 250),
      ),
      (route) => false,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: kColorHeader,
      padding: const EdgeInsets.symmetric(vertical: 6),
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
    const activeColor = Color(0xFF0D47A1);
    const inactiveColor = Colors.black54;

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
                ? Colors.white.withValues(alpha: 0.55)
                : Colors.transparent,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              AnimatedScale(
                scale: isActive ? 1.15 : 1.0,
                duration: const Duration(milliseconds: 200),
                child: Icon(
                  icon,
                  size: 26,
                  color: isActive ? activeColor : inactiveColor,
                ),
              ),
              const SizedBox(height: 3),
              Text(
                label,
                style: TextStyle(
                  fontSize: 11,
                  fontWeight:
                      isActive ? FontWeight.bold : FontWeight.normal,
                  color: isActive ? activeColor : inactiveColor,
                ),
                textAlign: TextAlign.center,
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
              const SizedBox(height: 2),
              // Indikator titik bawah
              AnimatedContainer(
                duration: const Duration(milliseconds: 200),
                height: 3,
                width: isActive ? 24 : 0,
                decoration: BoxDecoration(
                  color: activeColor,
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
