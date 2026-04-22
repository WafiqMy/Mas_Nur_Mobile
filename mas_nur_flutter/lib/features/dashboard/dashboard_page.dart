import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/acara/acara_page.dart';
import 'package:mas_nur_flutter/features/berita/berita_page.dart';
import 'package:mas_nur_flutter/features/informasi/informasi_masjid_page.dart';
import 'package:mas_nur_flutter/features/notifikasi/notifikasi_page.dart';
import 'package:mas_nur_flutter/features/persewaan/persewaan_page.dart';
import 'package:mas_nur_flutter/features/profil/profil_admin_page.dart';

class DashboardPage extends StatelessWidget {
  const DashboardPage({super.key});
  static const routeName = '/dashboard';

  @override
  Widget build(BuildContext context) {
    final menus = <_MenuItem>[
      _MenuItem('Berita', BeritaPage.routeName, Icons.article),
      _MenuItem('Acara', AcaraPage.routeName, Icons.event),
      _MenuItem('Persewaan', PersewaanPage.routeName, Icons.inventory_2),
      _MenuItem('Notifikasi', NotifikasiPage.routeName, Icons.notifications),
      _MenuItem('Informasi Masjid', InformasiMasjidPage.routeName, Icons.mosque),
      _MenuItem('Profil Admin', ProfilAdminPage.routeName, Icons.person),
    ];
    return Scaffold(
      appBar: AppBar(title: const Text('Halaman Utama')),
      body: GridView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: menus.length,
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
          childAspectRatio: 1.2,
          mainAxisSpacing: 12,
          crossAxisSpacing: 12,
        ),
        itemBuilder: (_, index) {
          final menu = menus[index];
          return Card(
            child: InkWell(
              onTap: () => Navigator.pushNamed(context, menu.route),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(menu.icon, size: 36),
                  const SizedBox(height: 8),
                  Text(menu.title),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}

class _MenuItem {
  const _MenuItem(this.title, this.route, this.icon);
  final String title;
  final String route;
  final IconData icon;
}

