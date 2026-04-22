import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';
import 'package:mas_nur_flutter/features/auth/konfirmasi_email_page.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';

// ─── Warna persis dari colors.xml ────────────────────────────────────────────
const _bgColor     = Color(0xFF98D3F7); // colorBackground
const _btnColor    = Color(0xFF99D5F9); // bg_button_login
const _inputBg     = Color(0xFFF0F5F5); // bg_input_field (abu sangat terang)

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

  // ─── Login logic persis MasukActivity.java ──────────────────────────────────
  Future<void> _login() async {
    final username = _usernameCtrl.text.trim();
    final password = _passwordCtrl.text; // no trim — sama seperti Java

    if (username.isEmpty || password.isEmpty) {
      _toast('Username dan password harus diisi');
      return;
    }

    setState(() => _loading = true);

    try {
      final result = await AppApiService.loginAdmin(username, password);

      if (!mounted) return;

      if (result.status == 'success') {
        // Simpan username ke session (SharedPreferences)
        await AppSession.saveUsername(username);
        if (!mounted) return;
        _toast('Login berhasil sebagai admin');
        Navigator.pushReplacementNamed(context, DashboardPage.routeName);
      } else {
        final msg = result.message.isNotEmpty ? result.message : 'Login gagal';
        _toast(msg);
      }
    } on Exception catch (e) {
      if (!mounted) return;
      final msg = e.toString();
      if (msg.contains('SocketException') || msg.contains('UnknownHost')) {
        _toast('Domain tidak ditemukan. Periksa URL server.');
      } else if (msg.contains('timeout') || msg.contains('TimeoutException')) {
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
      ..showSnackBar(SnackBar(
        content: Text(msg),
        behavior: SnackBarBehavior.floating,
        duration: const Duration(seconds: 3),
      ));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Tidak ada AppBar — full screen seperti Java
      body: Container(
        width: double.infinity,
        height: double.infinity,
        color: _bgColor, // background biru muda
        child: Column(
          children: [
            // ── Logo Masjid ─────────────────────────────────────────────────
            // marginTop 60dp, height 150dp, marginStart/End 32dp
            const SizedBox(height: 60),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32),
              child: SizedBox(
                height: 150,
                child: Center(
                  child: Icon(
                    Icons.mosque,
                    size: 110,
                    color: Colors.white,
                  ),
                ),
              ),
            ),
            // marginTop 40dp sebelum card
            const SizedBox(height: 40),

            // ── Card putih rounded atas (bg_card_top_rounded) ───────────────
            Expanded(
              child: Container(
                width: double.infinity,
                decoration: const BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(32),
                    topRight: Radius.circular(32),
                  ),
                ),
                child: SingleChildScrollView(
                  padding: const EdgeInsets.all(32),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // ── Judul "Masuk" ──────────────────────────────────────
                      // textSize 32sp, bold, center, marginBottom 24dp
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

                      // ── Input Username ─────────────────────────────────────
                      // bg_input_field: rounded, abu terang
                      // icon person (24dp, grey) + EditText height 50dp
                      _InputField(
                        controller: _usernameCtrl,
                        hint: 'Nama Pengguna',
                        icon: Icons.person,
                        keyboardType: TextInputType.text,
                        textInputAction: TextInputAction.next,
                      ),
                      const SizedBox(height: 16),

                      // ── Input Kata Sandi ───────────────────────────────────
                      // icon lock + toggle eye kanan (40dp)
                      _PasswordField(
                        controller: _passwordCtrl,
                        obscure: _obscure,
                        onToggle: () => setState(() => _obscure = !_obscure),
                        onSubmitted: (_) => _login(),
                      ),
                      const SizedBox(height: 16),

                      // ── Lupa Kata Sandi ────────────────────────────────────
                      // textColor darker_gray, align start, marginBottom 24dp
                      GestureDetector(
                        onTap: () => Navigator.pushNamed(
                            context, KonfirmasiEmailPage.routeName),
                        child: const Text(
                          'Lupa Kata Sandi?',
                          style: TextStyle(
                            color: Colors.grey,
                            fontSize: 14,
                          ),
                        ),
                      ),
                      const SizedBox(height: 24),

                      // ── Tombol Masuk ───────────────────────────────────────
                      // width match_parent, height 55dp, bg_button_login
                      // textColor white, textSize 18sp, textAllCaps false
                      SizedBox(
                        width: double.infinity,
                        height: 55,
                        child: ElevatedButton(
                          onPressed: _loading ? null : _login,
                          style: ElevatedButton.styleFrom(
                            backgroundColor: _btnColor,
                            foregroundColor: Colors.white,
                            disabledBackgroundColor: _btnColor.withAlpha(153),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(8),
                            ),
                            elevation: 0,
                          ),
                          child: _loading
                              ? const SizedBox(
                                  width: 24,
                                  height: 24,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2.5,
                                    color: Colors.white,
                                  ),
                                )
                              : const Text(
                                  'Masuk',
                                  style: TextStyle(
                                    fontSize: 18,
                                    fontWeight: FontWeight.w500,
                                    color: Colors.white,
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

// ─── Widget input field bergaya bg_input_field ────────────────────────────────
class _InputField extends StatelessWidget {
  const _InputField({
    required this.controller,
    required this.hint,
    required this.icon,
    this.keyboardType = TextInputType.text,
    this.textInputAction = TextInputAction.done,
  });

  final TextEditingController controller;
  final String hint;
  final IconData icon;
  final TextInputType keyboardType;
  final TextInputAction textInputAction;

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 50,
      decoration: BoxDecoration(
        color: _inputBg,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        children: [
          // Icon 24dp, grey, marginStart 16dp
          const SizedBox(width: 16),
          Icon(icon, size: 24, color: Colors.grey),
          // EditText: transparent bg, paddingStart/End 16dp
          Expanded(
            child: TextField(
              controller: controller,
              keyboardType: keyboardType,
              textInputAction: textInputAction,
              style: const TextStyle(fontSize: 15, color: Colors.black),
              decoration: InputDecoration(
                hintText: hint,
                hintStyle: const TextStyle(color: Colors.grey),
                border: InputBorder.none,
                contentPadding: const EdgeInsets.symmetric(
                  horizontal: 16,
                  vertical: 0,
                ),
                isDense: true,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// ─── Widget password field dengan toggle eye ──────────────────────────────────
class _PasswordField extends StatelessWidget {
  const _PasswordField({
    required this.controller,
    required this.obscure,
    required this.onToggle,
    this.onSubmitted,
  });

  final TextEditingController controller;
  final bool obscure;
  final VoidCallback onToggle;
  final ValueChanged<String>? onSubmitted;

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 50,
      decoration: BoxDecoration(
        color: _inputBg,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        children: [
          // Icon lock 24dp, grey, marginStart 16dp
          const SizedBox(width: 16),
          const Icon(Icons.lock, size: 24, color: Colors.grey),
          // EditText password
          Expanded(
            child: TextField(
              controller: controller,
              obscureText: obscure,
              textInputAction: TextInputAction.done,
              onSubmitted: onSubmitted,
              style: const TextStyle(fontSize: 15, color: Colors.black),
              decoration: const InputDecoration(
                hintText: 'Kata Sandi',
                hintStyle: TextStyle(color: Colors.grey),
                border: InputBorder.none,
                contentPadding: EdgeInsets.symmetric(
                  horizontal: 16,
                  vertical: 0,
                ),
                isDense: true,
              ),
            ),
          ),
          // Toggle eye button — 40dp, marginEnd 12dp
          SizedBox(
            width: 40,
            height: 40,
            child: IconButton(
              padding: EdgeInsets.zero,
              icon: Icon(
                obscure ? Icons.visibility_off : Icons.visibility,
                color: Colors.grey,
                size: 22,
              ),
              onPressed: onToggle,
            ),
          ),
          const SizedBox(width: 4),
        ],
      ),
    );
  }
}
