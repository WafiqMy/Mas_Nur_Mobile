import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';
import 'package:mas_nur_flutter/features/acara/acara_page.dart';
import 'package:mas_nur_flutter/features/auth/login_page.dart';
import 'package:mas_nur_flutter/features/berita/berita_page.dart';
import 'package:mas_nur_flutter/features/food_court/food_court_page.dart';
import 'package:mas_nur_flutter/features/informasi/informasi_masjid_page.dart';
import 'package:mas_nur_flutter/features/profil/profil_admin_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/utils/app_navigation.dart';

class AppDrawer extends StatelessWidget {
  const AppDrawer({super.key, this.currentRoute});

  /// Isi dengan routeName halaman aktif, misal: AcaraPage.routeName
  final String? currentRoute;

  @override
  Widget build(BuildContext context) {
    final active = currentRoute ?? ModalRoute.of(context)?.settings.name;

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
                  isActive: active == ProfilAdminPage.routeName,
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushReplacement(
                        context, fadeRoute(const ProfilAdminPage(), name: ProfilAdminPage.routeName));
                  },
                ),
                _DrawerItem(
                  icon: Icons.calendar_today,
                  label: 'Acara',
                  isActive: active == AcaraPage.routeName,
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushReplacement(
                        context, fadeRoute(const AcaraPage(), name: AcaraPage.routeName));
                  },
                ),
                _DrawerItem(
                  icon: Icons.mosque,
                  label: 'Informasi Masjid',
                  isActive: active == InformasiMasjidPage.routeName,
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushReplacement(
                        context, fadeRoute(const InformasiMasjidPage(), name: InformasiMasjidPage.routeName));
                  },
                ),
                _DrawerItem(
                  icon: Icons.article_outlined,
                  label: 'Berita',
                  isActive: active == BeritaPage.routeName,
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushReplacement(
                        context, fadeRoute(const BeritaPage(), name: BeritaPage.routeName));
                  },
                ),
                _DrawerItem(
                  icon: Icons.restaurant_menu,
                  label: 'Food Court',
                  isActive: active == FoodCourtPage.routeName,
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushReplacement(
                        context, fadeRoute(const FoodCourtPage(), name: FoodCourtPage.routeName));
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
    this.isActive = false,
    this.iconColor,
    this.labelColor,
  });

  final IconData icon;
  final String label;
  final VoidCallback onTap;
  final bool isActive;
  final Color? iconColor;
  final Color? labelColor;

  @override
  Widget build(BuildContext context) {
    final activeColor = Theme.of(context).colorScheme.primary;
    final resolvedIconColor = iconColor ?? (isActive ? activeColor : Colors.black87);
    final resolvedLabelColor = labelColor ?? (isActive ? activeColor : Colors.black87);

    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
      decoration: BoxDecoration(
        color: isActive ? activeColor.withOpacity(0.1) : Colors.transparent,
        borderRadius: BorderRadius.circular(10),
      ),
      child: ListTile(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
        leading: Icon(icon, color: resolvedIconColor),
        title: Text(
          label,
          style: TextStyle(
            fontSize: 15,
            color: resolvedLabelColor,
            fontWeight: isActive ? FontWeight.bold : FontWeight.normal,
          ),
        ),
        trailing: isActive
            ? Container(
                width: 4,
                height: 24,
                decoration: BoxDecoration(
                  color: activeColor,
                  borderRadius: BorderRadius.circular(2),
                ),
              )
            : null,
        onTap: onTap,
      ),
    );
  }
}
