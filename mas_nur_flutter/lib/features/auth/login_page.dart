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
      backgroundColor: kColorBackground,
      body: Column(
        children: [
          // ── Header hijau melengkung ────────────────────────────────────
          ClipPath(
            clipper: _CurvedBottomClipper(),
            child: Container(
              height: 280,
              decoration: const BoxDecoration(gradient: kAppGradient),
              child: SafeArea(
                child: Center(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Container(
                        width: 80,
                        height: 80,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          color: kColorWhite.withValues(alpha: 0.2),
                          border: Border.all(
                              color: kColorWhite.withValues(alpha: 0.5), width: 2),
                        ),
                        child: const Icon(Icons.mosque,
                            size: 42, color: kColorWhite),
                      ),
                      const SizedBox(height: 14),
                      const Text(
                        'Mas Nur',
                        style: TextStyle(
                          fontSize: 26,
                          fontWeight: FontWeight.bold,
                          color: kColorWhite,
                          letterSpacing: 1.5,
                        ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        'Admin Panel',
                        style: TextStyle(
                          fontSize: 13,
                          color: kColorWhite.withValues(alpha: 0.8),
                          letterSpacing: 1.5,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),

          // ── Form ──────────────────────────────────────────────────────
          Expanded(
            child: SingleChildScrollView(
              padding: const EdgeInsets.fromLTRB(24, 32, 24, 24),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    'Selamat Datang',
                    style: TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      color: kColorTextPrimary,
                    ),
                  ),
                  const SizedBox(height: 4),
                  const Text(
                    'Masuk ke akun admin Anda',
                    style: TextStyle(
                        fontSize: 14, color: kColorTextSecondary),
                  ),
                  const SizedBox(height: 28),

                  // Username
                  _buildInput(
                    controller: _usernameCtrl,
                    hint: 'Nama Pengguna',
                    icon: Icons.person_outline,
                    textInputAction: TextInputAction.next,
                  ),
                  const SizedBox(height: 16),

                  // Password
                  _buildPasswordInput(),
                  const SizedBox(height: 12),

                  // Lupa sandi
                  Align(
                    alignment: Alignment.centerRight,
                    child: GestureDetector(
                      onTap: () => Navigator.pushNamed(
                          context, KonfirmasiEmailPage.routeName),
                      child: const Text(
                        'Lupa Kata Sandi?',
                        style: TextStyle(
                          color: kColorPrimary,
                          fontSize: 13,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 28),

                  // Tombol masuk
                  SizedBox(
                    width: double.infinity,
                    height: 52,
                    child: ElevatedButton(
                      onPressed: _loading ? null : _login,
                      style: ElevatedButton.styleFrom(
                        backgroundColor: kColorPrimary,
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
          ),
        ],
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
      style: const TextStyle(color: kColorTextPrimary, fontSize: 15),
      decoration: InputDecoration(
        hintText: hint,
        hintStyle: const TextStyle(color: kColorGrey),
        prefixIcon: Icon(icon, color: kColorPrimary, size: 22),
        filled: true,
        fillColor: kColorWhite,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: const BorderSide(color: kColorDivider),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: const BorderSide(color: kColorDivider),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: const BorderSide(color: kColorPrimary, width: 1.5),
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
      style: const TextStyle(color: kColorTextPrimary, fontSize: 15),
      decoration: InputDecoration(
        hintText: 'Kata Sandi',
        hintStyle: const TextStyle(color: kColorGrey),
        prefixIcon:
            const Icon(Icons.lock_outline, color: kColorPrimary, size: 22),
        suffixIcon: IconButton(
          icon: Icon(
            _obscure
                ? Icons.visibility_off_outlined
                : Icons.visibility_outlined,
            color: kColorGrey,
            size: 20,
          ),
          onPressed: () => setState(() => _obscure = !_obscure),
        ),
        filled: true,
        fillColor: kColorWhite,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: const BorderSide(color: kColorDivider),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: const BorderSide(color: kColorDivider),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(kInputRadius),
          borderSide: const BorderSide(color: kColorPrimary, width: 1.5),
        ),
        contentPadding:
            const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      ),
    );
  }
}

/// Clipper untuk efek lengkung bawah header
class _CurvedBottomClipper extends CustomClipper<Path> {
  @override
  Path getClip(Size size) {
    final path = Path();
    path.lineTo(0, size.height - 40);
    path.quadraticBezierTo(
        size.width / 2, size.height + 20, size.width, size.height - 40);
    path.lineTo(size.width, 0);
    path.close();
    return path;
  }

  @override
  bool shouldReclip(_CurvedBottomClipper oldClipper) => false;
}
