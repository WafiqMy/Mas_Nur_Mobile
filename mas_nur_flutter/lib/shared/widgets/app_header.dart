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
      decoration: const BoxDecoration(
        gradient: kAppGradient,
        boxShadow: [
          BoxShadow(
            color: Color(0x331B5E20),
            blurRadius: 8,
            offset: Offset(0, 3),
          ),
        ],
      ),
      child: SafeArea(
        bottom: false,
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // Kiri
              if (showBackButton)
                _HeaderIconButton(
                  icon: Icons.arrow_back_ios_new_rounded,
                  onTap: () => Navigator.pushReplacement(
                    context,
                    fadeRoute(const DashboardPage(),
                        name: DashboardPage.routeName),
                  ),
                )
              else
                _HeaderIconButton(
                  icon: Icons.menu_rounded,
                  onTap: () => Scaffold.of(context).openDrawer(),
                ),

              const Spacer(),

              // Logo tengah
              Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Container(
                    width: 32,
                    height: 32,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: kColorWhite.withOpacity(0.15),
                    ),
                    child: const Icon(Icons.mosque,
                        size: 18, color: kColorWhite),
                  ),
                  const SizedBox(width: 8),
                  const Text(
                    'Mas Nur',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      color: kColorWhite,
                      letterSpacing: 0.5,
                    ),
                  ),
                ],
              ),

              const Spacer(),

              // Kanan: notifikasi
              _HeaderIconButton(
                icon: Icons.notifications_outlined,
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

class _HeaderIconButton extends StatelessWidget {
  const _HeaderIconButton({required this.icon, required this.onTap});
  final IconData icon;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: 38,
        height: 38,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: kColorWhite.withOpacity(0.15),
        ),
        child: Icon(icon, size: 20, color: kColorWhite),
      ),
    );
  }
}
