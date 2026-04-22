import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';
import 'package:mas_nur_flutter/features/auth/konfirmasi_email_page.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});
  static const routeName = '/login';

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  bool _loading = false;
  String? _error;

  Future<void> _submit() async {
    setState(() {
      _loading = true;
      _error = null;
    });
    try {
      final response = await AppApiService.loginAdmin(
        _usernameController.text.trim(),
        _passwordController.text,
      );
      if (!mounted) return;
      if (response.status.toLowerCase() == 'success') {
        await AppSession.saveUsername(_usernameController.text.trim());
        if (!mounted) return;
        Navigator.pushReplacementNamed(context, DashboardPage.routeName);
      } else {
        setState(() => _error = response.message.isEmpty ? 'Login gagal' : response.message);
      }
    } catch (_) {
      if (mounted) {
        setState(() => _error = 'Tidak dapat menghubungi server');
      }
    } finally {
      if (mounted) {
        setState(() => _loading = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Masuk')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            TextField(
              controller: _usernameController,
              decoration: const InputDecoration(labelText: 'Username'),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: _passwordController,
              decoration: const InputDecoration(labelText: 'Password'),
              obscureText: true,
            ),
            const SizedBox(height: 16),
            if (_error != null)
              Text(_error!, style: const TextStyle(color: Colors.red)),
            SizedBox(
              width: double.infinity,
              child: FilledButton(
                onPressed: _loading ? null : _submit,
                child: Text(_loading ? 'Memproses...' : 'Masuk'),
              ),
            ),
            TextButton(
              onPressed: () {
                Navigator.pushNamed(context, KonfirmasiEmailPage.routeName);
              },
              child: const Text('Lupa Password'),
            ),
          ],
        ),
      ),
    );
  }
}

