import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/features/auth/login_page.dart';

class SandiBaruPage extends StatefulWidget {
  const SandiBaruPage({super.key});
  static const routeName = '/sandi-baru';

  @override
  State<SandiBaruPage> createState() => _SandiBaruPageState();
}

class _SandiBaruPageState extends State<SandiBaruPage> {
  final _passwordController = TextEditingController();
  bool _loading = false;
  String? _info;

  Future<void> _simpan(String email) async {
    setState(() {
      _loading = true;
      _info = null;
    });
    try {
      final response = await AppApiService.resetPassword(email, _passwordController.text);
      if (!mounted) return;
      setState(() => _info = response.message);
      if (response.status.toLowerCase() == 'success') {
        Navigator.pushNamedAndRemoveUntil(
          context,
          LoginPage.routeName,
          (route) => false,
        );
      }
    } catch (_) {
      if (mounted) setState(() => _info = 'Gagal menyimpan password baru');
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final email = (ModalRoute.of(context)?.settings.arguments as String?) ?? '';
    return Scaffold(
      appBar: AppBar(title: const Text('Password Baru')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Text('Email: $email'),
            const SizedBox(height: 12),
            TextField(
              controller: _passwordController,
              decoration: const InputDecoration(labelText: 'Password Baru'),
              obscureText: true,
            ),
            const SizedBox(height: 16),
            if (_info != null) Text(_info!),
            SizedBox(
              width: double.infinity,
              child: FilledButton(
                onPressed: _loading ? null : () => _simpan(email),
                child: Text(_loading ? 'Menyimpan...' : 'Simpan'),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

