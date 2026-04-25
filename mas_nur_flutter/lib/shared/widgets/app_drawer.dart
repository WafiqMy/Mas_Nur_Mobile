import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';
import 'package:mas_nur_flutter/features/acara/acara_page.dart';
import 'package:mas_nur_flutter/features/auth/login_page.dart';
import 'package:mas_nur_flutter/features/berita/berita_page.dart';
import 'package:mas_nur_flutter/features/informasi/informasi_masjid_page.dart';
import 'package:mas_nur_flutter/features/profil/profil_admin_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';

class AppDrawer extends StatelessWidget {
  const AppDrawer({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      backgroundColor: kColorWhite,
      child: Column(
        children: [
          // Header drawer
          Container(
            width: double.infinity,
            color: kColorHeader,
            padding: const EdgeInsets.fromLTRB(16, 48, 16, 20),
            child: const Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Icon(Icons.mosque, size: 48, color: Colors.black87),
                SizedBox(height: 8),
                Text(
                  'Mas Nur',
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                    color: Colors.black87,
                  ),
                ),
                Text(
                  'Admin Panel',
                  style: TextStyle(fontSize: 13, color: Colors.black54),
                ),
              ],
            ),
          ),

          // Menu items
          Expanded(
            child: ListView(
              padding: EdgeInsets.zero,
              children: [
                _DrawerItem(
                  icon: Icons.person,
                  label: 'Profil',
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushNamed(context, ProfilAdminPage.routeName);
                  },
                ),
                _DrawerItem(
                  icon: Icons.calendar_today,
                  label: 'Acara',
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushNamed(context, AcaraPage.routeName);
                  },
                ),
                _DrawerItem(
                  icon: Icons.mosque,
                  label: 'Informasi Masjid',
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushNamed(context, InformasiMasjidPage.routeName);
                  },
                ),
                _DrawerItem(
                  icon: Icons.article_outlined,
                  label: 'Berita',
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushNamed(context, BeritaPage.routeName);
                  },
                ),
                const Divider(height: 1),
                _DrawerItem(
                  icon: Icons.logout,
                  label: 'Logout',
                  iconColor: Colors.red,
                  labelColor: Colors.red,
                  onTap: () async {
                    Navigator.pop(context);
                    await AppSession.clear();
                    if (context.mounted) {
                      Navigator.pushNamedAndRemoveUntil(
                        context,
                        LoginPage.routeName,
                        (route) => false,
                      );
                    }
                  },
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _DrawerItem extends StatelessWidget {
  const _DrawerItem({
    required this.icon,
    required this.label,
    required this.onTap,
    this.iconColor = Colors.black87,
    this.labelColor = Colors.black87,
  });

  final IconData icon;
  final String label;
  final VoidCallback onTap;
  final Color iconColor;
  final Color labelColor;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(icon, color: iconColor),
      title: Text(
        label,
        style: TextStyle(fontSize: 15, color: labelColor),
      ),
      onTap: onTap,
    );
  }
}
