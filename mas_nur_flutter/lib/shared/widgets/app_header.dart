import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/notifikasi/notifikasi_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';

/// Header dengan hamburger menu di kiri, logo di tengah, notifikasi di kanan.
class AppHeader extends StatelessWidget implements PreferredSizeWidget {
  const AppHeader({super.key});

  @override
  Size get preferredSize => const Size.fromHeight(kHeaderHeight);

  @override
  Widget build(BuildContext context) {
    return Container(
      color: kColorHeader,
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          // Hamburger menu (buka drawer)
          GestureDetector(
            onTap: () => Scaffold.of(context).openDrawer(),
            child: const Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Icon(Icons.menu, size: 25, color: Colors.black),
                SizedBox(height: 2),
                Text('Menu', style: TextStyle(fontSize: 10, color: Colors.black)),
              ],
            ),
          ),
          const Spacer(),
          // Logo
          const Text(
            'Mas Nur',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
              color: Colors.black,
            ),
          ),
          const Spacer(),
          // Notifikasi
          GestureDetector(
            onTap: () => Navigator.pushNamed(context, NotifikasiPage.routeName),
            child: const Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Icon(Icons.notifications, size: 25, color: Colors.black),
                SizedBox(height: 2),
                Text('Notifikasi', style: TextStyle(fontSize: 10, color: Colors.black)),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
