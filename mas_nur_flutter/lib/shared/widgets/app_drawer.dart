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
  final String? currentRoute;

  @override
  Widget build(BuildContext context) {
    final active =
        currentRoute ?? ModalRoute.of(context)?.settings.name;

    return Drawer(
      backgroundColor: kColorWhite,
      child: Column(
        children: [
          // ── Header drawer ──────────────────────────────────────────────
          Container(
            width: double.infinity,
            decoration: const BoxDecoration(
              gradient: kAppGradient,
            ),
            padding: const EdgeInsets.fromLTRB(20, 52, 20, 24),
            child: Row(
              children: [
                Container(
                  width: 52,
                  height: 52,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: kColorWhite.withValues(alpha: 0.2),
                    border: Border.all(
                        color: kColorWhite.withValues(alpha: 0.4), width: 1.5),
                  ),
                  child: const Icon(Icons.mosque,
                      size: 28, color: kColorWhite),
                ),
                const SizedBox(width: 14),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      'Mas Nur',
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                        color: kColorWhite,
                        letterSpacing: 0.5,
                      ),
                    ),
                    Text(
                      'Admin Panel',
                      style: TextStyle(
                          fontSize: 12,
                          color: kColorWhite.withValues(alpha: 0.75)),
                    ),
                  ],
                ),
              ],
            ),
          ),

          // ── Menu items ─────────────────────────────────────────────────
          Expanded(
            child: ListView(
              padding: const EdgeInsets.symmetric(vertical: 12),
              children: [
                _DrawerItem(
                  icon: Icons.person_outline_rounded,
                  label: 'Profil',
                  isActive: active == ProfilAdminPage.routeName,
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushReplacement(context,
                        fadeRoute(const ProfilAdminPage(),
                            name: ProfilAdminPage.routeName));
                  },
                ),
                _DrawerItem(
                  icon: Icons.calendar_today_outlined,
                  label: 'Acara',
                  isActive: active == AcaraPage.routeName,
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushReplacement(context,
                        fadeRoute(const AcaraPage(),
                            name: AcaraPage.routeName));
                  },
                ),
                _DrawerItem(
                  icon: Icons.mosque_outlined,
                  label: 'Informasi Masjid',
                  isActive: active == InformasiMasjidPage.routeName,
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushReplacement(context,
                        fadeRoute(const InformasiMasjidPage(),
                            name: InformasiMasjidPage.routeName));
                  },
                ),
                _DrawerItem(
                  icon: Icons.article_outlined,
                  label: 'Berita',
                  isActive: active == BeritaPage.routeName,
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushReplacement(context,
                        fadeRoute(const BeritaPage(),
                            name: BeritaPage.routeName));
                  },
                ),
                _DrawerItem(
                  icon: Icons.restaurant_menu_outlined,
                  label: 'Food Court',
                  isActive: active == FoodCourtPage.routeName,
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.pushReplacement(context,
                        fadeRoute(const FoodCourtPage(),
                            name: FoodCourtPage.routeName));
                  },
                ),
                const Padding(
                  padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  child: Divider(color: kColorDivider, height: 1),
                ),
                _DrawerItem(
                  icon: Icons.logout_rounded,
                  label: 'Keluar',
                  iconColor: kColorHapus,
                  labelColor: kColorHapus,
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
    final resolvedIcon =
        iconColor ?? (isActive ? kColorPrimary : kColorGrey);
    final resolvedLabel =
        labelColor ?? (isActive ? kColorPrimary : kColorTextPrimary);

    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 2),
      decoration: BoxDecoration(
        color: isActive
            ? kColorPrimary.withValues(alpha: 0.08)
            : Colors.transparent,
        borderRadius: BorderRadius.circular(10),
      ),
      child: ListTile(
        dense: true,
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(10)),
        leading: Container(
          width: 36,
          height: 36,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: isActive
                ? kColorPrimary.withValues(alpha: 0.12)
                : kColorBackground,
          ),
          child: Icon(icon, color: resolvedIcon, size: 20),
        ),
        title: Text(
          label,
          style: TextStyle(
            fontSize: 14,
            color: resolvedLabel,
            fontWeight:
                isActive ? FontWeight.w600 : FontWeight.normal,
          ),
        ),
        trailing: isActive
            ? Container(
                width: 4,
                height: 24,
                decoration: BoxDecoration(
                  color: kColorPrimary,
                  borderRadius: BorderRadius.circular(2),
                ),
              )
            : null,
        onTap: onTap,
      ),
    );
  }
}
