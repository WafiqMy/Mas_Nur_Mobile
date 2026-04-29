import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/features/notifikasi/notifikasi_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/utils/app_navigation.dart';

/// Header utama aplikasi.
/// - [showBackButton] = false (default): tampilkan hamburger menu (buka drawer)
/// - [showBackButton] = true: tampilkan tombol kembali ke Beranda (untuk halaman sidebar)
class AppHeader extends StatelessWidget {
  const AppHeader({super.key, this.showBackButton = false});

  final bool showBackButton;

  @override
  Widget build(BuildContext context) {
    return Container(
      color: kColorHeader,
      child: SafeArea(
        bottom: false,
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // Kiri: hamburger atau tombol kembali
              if (showBackButton)
                GestureDetector(
                  onTap: () => Navigator.pushReplacement(
                    context,
                    fadeRoute(const DashboardPage(),
                        name: DashboardPage.routeName),
                  ),
                  child: const Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(Icons.arrow_back, size: 24, color: Colors.black),
                      SizedBox(height: 2),
                      Text('Kembali',
                          style: TextStyle(fontSize: 10, color: Colors.black)),
                    ],
                  ),
                )
              else
                GestureDetector(
                  onTap: () => Scaffold.of(context).openDrawer(),
                  child: const Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(Icons.menu, size: 24, color: Colors.black),
                      SizedBox(height: 2),
                      Text('Menu',
                          style: TextStyle(fontSize: 10, color: Colors.black)),
                    ],
                  ),
                ),

              const Spacer(),

              // Tengah: logo
              const Text(
                'Mas Nur',
                style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  color: Colors.black,
                ),
              ),

              const Spacer(),

              // Kanan: notifikasi
              GestureDetector(
                onTap: () =>
                    Navigator.pushNamed(context, NotifikasiPage.routeName),
                child: const Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.notifications, size: 24, color: Colors.black),
                    SizedBox(height: 2),
                    Text('Notifikasi',
                        style: TextStyle(fontSize: 10, color: Colors.black)),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
