import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';
import 'package:mas_nur_flutter/features/auth/login_page.dart';
import 'package:mas_nur_flutter/features/profil/ganti_nama_page.dart';
import 'package:mas_nur_flutter/features/profil/ganti_sandi_page.dart';

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
    _future = _load();
  }

  Future<UserProfileModel?> _load() async {
    final username = await AppSession.getUsername();
    if (username.isEmpty) return null;
    return AppApiService.getUserProfile(username);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Profil Admin')),
      body: FutureBuilder<UserProfileModel?>(
        future: _future,
        builder: (context, snapshot) {
          final profile = snapshot.data;
          return ListView(
            padding: const EdgeInsets.all(16),
            children: [
              Text(profile?.nama ?? '-', style: Theme.of(context).textTheme.headlineSmall),
              const SizedBox(height: 4),
              Text(profile?.email ?? '-'),
              const SizedBox(height: 20),
              FilledButton(
                onPressed: () async {
                  final updated = await Navigator.pushNamed(context, GantiNamaPage.routeName);
                  if (updated == true && mounted) {
                    setState(() => _future = _load());
                  }
                },
                child: const Text('Ganti Nama'),
              ),
              const SizedBox(height: 8),
              FilledButton(
                onPressed: () async {
                  await Navigator.pushNamed(context, GantiSandiPage.routeName);
                },
                child: const Text('Ganti Password'),
              ),
              const SizedBox(height: 8),
              OutlinedButton(
                onPressed: () async {
                  await AppSession.clear();
                  if (!context.mounted) return;
                  Navigator.pushNamedAndRemoveUntil(context, LoginPage.routeName, (route) => false);
                },
                child: const Text('Keluar'),
              ),
            ],
          );
        },
      ),
    );
  }
}

