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
    final username = _usernameCtrl.text.trim();
    final password = _passwordCtrl.text;

    if (username.isEmpty || password.isEmpty) {
      _toast('Username dan password harus diisi');
      return;
    }

    setState(() => _loading = true);
    try {
      final result = await AppApiService.loginAdmin(username, password);
      if (!mounted) return;
      if (result.status == 'success') {
        await AppSession.saveUsername(username);
        if (!mounted) return;
        Navigator.pushReplacementNamed(context, DashboardPage.routeName);
      } else {
        _toast(result.message.isNotEmpty ? result.message : 'Login gagal');
      }
    } on Exception catch (e) {
      if (!mounted) return;
      final msg = e.toString();
      if (msg.contains('SocketException') || msg.contains('UnknownHost')) {
        _toast('Domain tidak ditemukan. Periksa URL server.');
      } else if (msg.contains('timeout')) {
        _toast('Waktu koneksi habis. Periksa server atau jaringan.');
      } else {
        _toast('Gagal terhubung ke server');
      }
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  void _toast(String msg) {
    ScaffoldMessenger.of(context)
      ..hideCurrentSnackBar()
      ..showSnackBar(SnackBar(content: Text(msg)));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kColorNavy,
      body: SafeArea(
        child: SingleChildScrollView(
          child: SizedBox(
            height: MediaQuery.of(context).size.height -
                MediaQuery.of(context).padding.top,
            child: Column(
              children: [
                // ── Logo area ──────────────────────────────────────────────
                const Spacer(flex: 2),
                Container(
                  width: 90,
                  height: 90,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: kColorNavyLight,
                    border: Border.all(
                        color: kColorSkyBlue.withOpacity(0.5), width: 1.5),
                    boxShadow: [
                      BoxShadow(
                        color: kColorSkyBlue.withOpacity(0.2),
                        blurRadius: 20,
                        spreadRadius: 2,
                      ),
                    ],
                  ),
                  child: const Icon(Icons.mosque,
                      size: 46, color: kColorSkyBlue),
                ),
                const SizedBox(height: 16),
                const Text(
                  'Mas Nur',
                  style: TextStyle(
                    fontSize: 26,
                    fontWeight: FontWeight.bold,
                    color: kColorWhite,
                    letterSpacing: 2,
                  ),
                ),
                const SizedBox(height: 4),
                Text(
                  'Admin Panel',
                  style: TextStyle(
                    fontSize: 12,
                    color: kColorSkyBlue.withOpacity(0.8),
                    letterSpacing: 1.5,
                  ),
                ),
                const Spacer(flex: 2),

                // ── Form card ──────────────────────────────────────────────
                Container(
                  margin: const EdgeInsets.symmetric(horizontal: 24),
                  padding: const EdgeInsets.all(24),
                  decoration: BoxDecoration(
                    color: kColorNavyLight,
                    borderRadius: BorderRadius.circular(20),
                    border: Border.all(
                        color: kColorSkyBlue.withOpacity(0.2), width: 1),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withOpacity(0.3),
                        blurRadius: 20,
                        offset: const Offset(0, 8),
                      ),
                    ],
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        'Masuk',
                        style: TextStyle(
                          fontSize: 22,
                          fontWeight: FontWeight.bold,
                          color: kColorWhite,
                        ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        'Selamat datang kembali',
                        style: TextStyle(
                            fontSize: 13,
                            color: kColorSkyBlue.withOpacity(0.7)),
                      ),
                      const SizedBox(height: 24),

                      // Username
                      _buildInput(
                        controller: _usernameCtrl,
                        hint: 'Nama Pengguna',
                        icon: Icons.person_outline,
                        textInputAction: TextInputAction.next,
                      ),
                      const SizedBox(height: 14),

                      // Password
                      _buildPasswordInput(),
                      const SizedBox(height: 12),

                      // Lupa sandi
                      GestureDetector(
                        onTap: () => Navigator.pushNamed(
                            context, KonfirmasiEmailPage.routeName),
                        child: Text(
                          'Lupa Kata Sandi?',
                          style: TextStyle(
                            color: kColorYellow.withOpacity(0.85),
                            fontSize: 13,
                          ),
                        ),
                      ),
                      const SizedBox(height: 24),

                      // Tombol masuk
                      SizedBox(
                        width: double.infinity,
                        height: 50,
                        child: ElevatedButton(
                          onPressed: _loading ? null : _login,
                          style: ElevatedButton.styleFrom(
                            backgroundColor: kColorRoyal,
                            foregroundColor: kColorWhite,
                            shape: RoundedRectangleBorder(
                                borderRadius:
                                    BorderRadius.circular(kButtonRadius)),
                            elevation: 0,
                          ),
                          child: _loading
                              ? const SizedBox(
                                  width: 22,
                                  height: 22,
                                  child: CircularProgressIndicator(
                                      strokeWidth: 2, color: kColorWhite),
                                )
                              : const Text(
                                  'Masuk',
                                  style: TextStyle(
                                      fontSize: 16,
                                      fontWeight: FontWeight.w600),
                                ),
                        ),
                      ),
                    ],
                  ),
                ),
                const Spacer(flex: 3),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildInput({
    required TextEditingController controller,
    required String hint,
    required IconData icon,
    TextInputAction textInputAction = TextInputAction.done,
  }) {
    return TextField(
      controller: controller,
      textInputAction: textInputAction,
      style: const TextStyle(color: kColorWhite, fontSize: 15),
      decoration: InputDecoration(
        hintText: hint,
        hintStyle: TextStyle(color: kColorGrey.withOpacity(0.6)),
        prefixIcon: Icon(icon, color: kColorSkyBlue, size: 20),
        filled: true,
        fillColor: kColorNavy.withOpacity(0.5),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: BorderSide(color: kColorSkyBlue.withOpacity(0.2)),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: BorderSide(color: kColorSkyBlue.withOpacity(0.2)),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: const BorderSide(color: kColorSkyBlue, width: 1.5),
        ),
        contentPadding:
            const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      ),
    );
  }

  Widget _buildPasswordInput() {
    return TextField(
      controller: _passwordCtrl,
      obscureText: _obscure,
      textInputAction: TextInputAction.done,
      onSubmitted: (_) => _login(),
      style: const TextStyle(color: kColorWhite, fontSize: 15),
      decoration: InputDecoration(
        hintText: 'Kata Sandi',
        hintStyle: TextStyle(color: kColorGrey.withOpacity(0.6)),
        prefixIcon:
            const Icon(Icons.lock_outline, color: kColorSkyBlue, size: 20),
        suffixIcon: IconButton(
          icon: Icon(
            _obscure ? Icons.visibility_off_outlined : Icons.visibility_outlined,
            color: kColorGrey,
            size: 20,
          ),
          onPressed: () => setState(() => _obscure = !_obscure),
        ),
        filled: true,
        fillColor: kColorNavy.withOpacity(0.5),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: BorderSide(color: kColorSkyBlue.withOpacity(0.2)),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: BorderSide(color: kColorSkyBlue.withOpacity(0.2)),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: const BorderSide(color: kColorSkyBlue, width: 1.5),
        ),
        contentPadding:
            const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      ),
    );
  }
}
