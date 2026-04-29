import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/features/notifikasi/notifikasi_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/utils/app_navigation.dart';

class AppHeader extends StatelessWidget {
  const AppHeader({super.key, this.showBackButton = false});
  final bool showBackButton;

  @override
  Widget build(BuildContext context) {
    return Container(
      color: kColorNavy,
      child: SafeArea(
        bottom: false,
        child: Container(
          decoration: BoxDecoration(
            color: kColorNavy,
            border: Border(
              bottom: BorderSide(
                  color: kColorSkyBlue.withOpacity(0.2), width: 1),
            ),
          ),
          padding:
              const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // Kiri
              if (showBackButton)
                _HeaderButton(
                  icon: Icons.arrow_back_ios_new_rounded,
                  label: 'Kembali',
                  onTap: () => Navigator.pushReplacement(
                    context,
                    fadeRoute(const DashboardPage(),
                        name: DashboardPage.routeName),
                  ),
                )
              else
                _HeaderButton(
                  icon: Icons.menu_rounded,
                  label: 'Menu',
                  onTap: () => Scaffold.of(context).openDrawer(),
                ),

              const Spacer(),

              // Logo
              Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(Icons.mosque,
                      size: 18,
                      color: kColorSkyBlue.withOpacity(0.8)),
                  const SizedBox(width: 6),
                  const Text(
                    'Mas Nur',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      color: kColorWhite,
                      letterSpacing: 1,
                    ),
                  ),
                ],
              ),

              const Spacer(),

              // Kanan: notifikasi
              _HeaderButton(
                icon: Icons.notifications_outlined,
                label: 'Notifikasi',
                onTap: () =>
                    Navigator.pushNamed(context, NotifikasiPage.routeName),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _HeaderButton extends StatelessWidget {
  const _HeaderButton({
      required this.icon, required this.label, required this.onTap});
  final IconData icon;
  final String label;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 22, color: kColorSkyBlue),
          const SizedBox(height: 2),
          Text(label,
              style: TextStyle(
                  fontSize: 9,
                  color: kColorSkyBlue.withOpacity(0.8),
                  letterSpacing: 0.3)),
        ],
      ),
    );
  }
}
