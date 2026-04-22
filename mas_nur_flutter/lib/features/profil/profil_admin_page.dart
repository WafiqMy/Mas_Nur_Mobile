import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';
import 'package:mas_nur_flutter/features/auth/login_page.dart';
import 'package:mas_nur_flutter/features/profil/ganti_nama_page.dart';
import 'package:mas_nur_flutter/features/profil/ganti_sandi_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';

class ProfilAdminPage extends StatefulWidget {
  const ProfilAdminPage({super.key});
  static const routeName = '/profil-admin';

  @override
  State<ProfilAdminPage> createState() => _ProfilAdminPageState();
}

class _ProfilAdminPageState extends State<ProfilAdminPage> {
  late Future<UserProfileModel?> _future;

  @override
  void initState() {
    super.initState();
    _loadProfil();
  }

  void _loadProfil() {
    _future = AppSession.getUsername().then((u) => AppApiService.getUserProfile(u));
  }

  Future<void> _logout() async {
    await AppSession.clear();
    if (!mounted) return;
    Navigator.pushNamedAndRemoveUntil(context, LoginPage.routeName, (_) => false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kColorWhite,
      body: FutureBuilder<UserProfileModel?>(
        future: _future,
        builder: (_, snapshot) {
          final profil = snapshot.data;
          return Column(
            children: [
              // ── Gambar header (group1) ──────────────────────────────────────
              SizedBox(
                height: 200,
                width: double.infinity,
                child: Container(
                  color: kColorBackground,
                  child: const Center(
                    child: Icon(Icons.mosque, size: 80, color: Colors.white),
                  ),
                ),
              ),

              // ── Kartu profil (overlap -60dp) ────────────────────────────────
              Transform.translate(
                offset: const Offset(0, -60),
                child: Card(
                  elevation: 4,
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12)),
                  child: Padding(
                    padding: const EdgeInsets.all(10),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        // Logo masjid
                        SizedBox(
                          width: 200,
                          height: 80,
                          child: Center(
                            child: Icon(Icons.mosque,
                                size: 60, color: kColorBackground),
                          ),
                        ),
                        const SizedBox(height: 16),
                        // Nama
                        Text(
                          snapshot.connectionState == ConnectionState.done
                              ? (profil?.nama ?? '-')
                              : '...',
                          style: const TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            color: Colors.black,
                          ),
                        ),
                        const SizedBox(height: 4),
                        // Role
                        Text(
                          snapshot.connectionState == ConnectionState.done
                              ? (profil?.email ?? '-')
                              : '',
                          style: const TextStyle(
                              fontSize: 14, color: Colors.grey),
                        ),
                      ],
                    ),
                  ),
                ),
              ),

              // ── Tombol-tombol ───────────────────────────────────────────────
              Expanded(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(32, 0, 32, 0),
                  child: Column(
                    children: [
                      // Ganti Nama
                      SizedBox(
                        width: double.infinity,
                        child: ElevatedButton(
                          onPressed: () async {
                            await Navigator.pushNamed(
                                context, GantiNamaPage.routeName);
                            setState(_loadProfil);
                          },
                          style: kPrimaryButtonStyle,
                          child: const Text('Ganti Nama'),
                        ),
                      ),
                      const SizedBox(height: 16),
                      // Ganti Kata Sandi
                      SizedBox(
                        width: double.infinity,
                        child: ElevatedButton(
                          onPressed: () =>
                              Navigator.pushNamed(context, GantiSandiPage.routeName),
                          style: kSecondaryButtonStyle,
                          child: const Text('Ganti Kata Sandi'),
                        ),
                      ),
                      const SizedBox(height: 16),
                      // Keluar
                      SizedBox(
                        width: double.infinity,
                        child: ElevatedButton(
                          onPressed: _logout,
                          style: kDangerButtonStyle,
                          child: const Text('Keluar'),
                        ),
                      ),
                    ],
                  ),
                ),
              ),

              // ── Footer ──────────────────────────────────────────────────────
              const AppFooter(),
            ],
          );
        },
      ),
    );
  }
}
