import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';
import 'package:mas_nur_flutter/features/auth/konfirmasi_email_page.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});
  static const routeName = '/login';

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final _usernameCtrl = TextEditingController();
  final _passwordCtrl = TextEditingController();
  bool _obscure = true;
  bool _loading = false;

  @override
  void dispose() {
    _usernameCtrl.dispose();
    _passwordCtrl.dispose();
    super.dispose();
  }

  Future<void> _login() async {
    if (_usernameCtrl.text.trim().isEmpty || _passwordCtrl.text.trim().isEmpty) {
      _showSnack('Username dan kata sandi wajib diisi');
      return;
    }
    setState(() => _loading = true);
    try {
      final result = await AppApiService.loginAdmin(
        _usernameCtrl.text.trim(),
        _passwordCtrl.text.trim(),
      );
      if (!mounted) return;
      if (result.status == 'success') {
        await AppSession.saveUsername(_usernameCtrl.text.trim());
        if (!mounted) return;
        Navigator.pushReplacementNamed(context, DashboardPage.routeName);
      } else {
        _showSnack(result.message.isEmpty ? 'Login gagal' : result.message);
      }
    } catch (_) {
      if (mounted) _showSnack('Gagal terhubung ke server');
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  void _showSnack(String msg) =>
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(msg)));

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        width: double.infinity,
        height: double.infinity,
        // Background biru muda (#98D3F7) seperti colorBackground
        color: kColorBackground,
        child: Column(
          children: [
            // ── Logo Masjid (atas, margin 60dp) ──────────────────────────────
            const SizedBox(height: 60),
            SizedBox(
              height: 150,
              child: Center(
                child: Icon(Icons.mosque, size: 100, color: Colors.white),
              ),
            ),
            const SizedBox(height: 40),

            // ── Form card putih rounded atas (bg_card_top_rounded) ───────────
            Expanded(
              child: Container(
                width: double.infinity,
                decoration: const BoxDecoration(
                  color: kColorWhite,
                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(32),
                    topRight: Radius.circular(32),
                  ),
                ),
                padding: const EdgeInsets.all(32),
                child: SingleChildScrollView(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Judul "Masuk"
                      const Center(
                        child: Text(
                          'Masuk',
                          style: TextStyle(
                            fontSize: 32,
                            fontWeight: FontWeight.bold,
                            color: Colors.black,
                          ),
                        ),
                      ),
                      const SizedBox(height: 24),

                      // Input Username
                      Container(
                        decoration: BoxDecoration(
                          color: kColorInputBg,
                          borderRadius: BorderRadius.circular(kInputRadius),
                        ),
                        child: Row(
                          children: [
                            const Padding(
                              padding: EdgeInsets.only(left: 16),
                              child: Icon(Icons.person, color: Colors.grey, size: 24),
                            ),
                            Expanded(
                              child: TextField(
                                controller: _usernameCtrl,
                                decoration: const InputDecoration(
                                  hintText: 'Nama Pengguna',
                                  hintStyle: TextStyle(color: Colors.grey),
                                  border: InputBorder.none,
                                  contentPadding: EdgeInsets.symmetric(
                                    horizontal: 16, vertical: 14),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 16),

                      // Input Kata Sandi
                      Container(
                        decoration: BoxDecoration(
                          color: kColorInputBg,
                          borderRadius: BorderRadius.circular(kInputRadius),
                        ),
                        child: Row(
                          children: [
                            const Padding(
                              padding: EdgeInsets.only(left: 16),
                              child: Icon(Icons.lock, color: Colors.grey, size: 24),
                            ),
                            Expanded(
                              child: TextField(
                                controller: _passwordCtrl,
                                obscureText: _obscure,
                                decoration: const InputDecoration(
                                  hintText: 'Kata Sandi',
                                  hintStyle: TextStyle(color: Colors.grey),
                                  border: InputBorder.none,
                                  contentPadding: EdgeInsets.symmetric(
                                    horizontal: 16, vertical: 14),
                                ),
                              ),
                            ),
                            IconButton(
                              icon: Icon(
                                _obscure ? Icons.visibility_off : Icons.visibility,
                                color: Colors.grey,
                              ),
                              onPressed: () => setState(() => _obscure = !_obscure),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 16),

                      // Lupa Kata Sandi
                      GestureDetector(
                        onTap: () => Navigator.pushNamed(
                            context, KonfirmasiEmailPage.routeName),
                        child: const Text(
                          'Lupa Kata Sandi?',
                          style: TextStyle(color: Colors.grey),
                        ),
                      ),
                      const SizedBox(height: 24),

                      // Tombol Masuk
                      SizedBox(
                        width: double.infinity,
                        height: 55,
                        child: ElevatedButton(
                          onPressed: _loading ? null : _login,
                          style: ElevatedButton.styleFrom(
                            backgroundColor: kColorButton,
                            foregroundColor: kColorTextButton,
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(kButtonRadius),
                            ),
                            elevation: 0,
                          ),
                          child: _loading
                              ? const SizedBox(
                                  width: 22,
                                  height: 22,
                                  child: CircularProgressIndicator(strokeWidth: 2),
                                )
                              : const Text(
                                  'Masuk',
                                  style: TextStyle(
                                    fontSize: 18,
                                    fontWeight: FontWeight.w500,
                                  ),
                                ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
