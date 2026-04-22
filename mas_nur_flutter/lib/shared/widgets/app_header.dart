import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/notifikasi/notifikasi_page.dart';
import 'package:mas_nur_flutter/features/profil/profil_admin_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';

/// Header persis seperti header.xml:
/// [Profil] ---- [Logo Masjid] ---- [Notifikasi]
/// Background: #99D5F9, padding 12dp
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
          // Profil
          GestureDetector(
            onTap: () => Navigator.pushNamed(context, ProfilAdminPage.routeName),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: const [
                Icon(Icons.person, size: 25, color: Colors.black),
                SizedBox(height: 2),
                Text('Profil', style: TextStyle(fontSize: 10, color: Colors.black)),
              ],
            ),
          ),
          const Spacer(),
          // Logo Masjid (teks sebagai pengganti gambar)
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
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: const [
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
