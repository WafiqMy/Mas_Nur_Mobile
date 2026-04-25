import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';
import 'package:mas_nur_flutter/features/auth/login_page.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';

class SplashPage extends StatefulWidget {
  const SplashPage({super.key});
  static const routeName = '/';

  @override
  State<SplashPage> createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage> {
  @override
  void initState() {
    super.initState();
    _checkSession();
  }

  Future<void> _checkSession() async {
    // Beri sedikit delay agar splash terlihat
    await Future.delayed(const Duration(milliseconds: 500));
    if (!mounted) return;

    final username = await AppSession.getUsername();
    if (!mounted) return;

    if (username.isNotEmpty) {
      Navigator.pushReplacementNamed(context, DashboardPage.routeName);
    } else {
      Navigator.pushReplacementNamed(context, LoginPage.routeName);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kColorHeader,
      body: const Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Icons.mosque, size: 72, color: Colors.black87),
            SizedBox(height: 16),
            Text(
              'Mas Nur',
              style: TextStyle(
                fontSize: 28,
                fontWeight: FontWeight.bold,
                color: Colors.black87,
              ),
            ),
            SizedBox(height: 8),
            Text(
              'Admin Panel',
              style: TextStyle(fontSize: 14, color: Colors.black54),
            ),
            SizedBox(height: 32),
            CircularProgressIndicator(color: Colors.black54),
          ],
        ),
      ),
    );
  }
}
