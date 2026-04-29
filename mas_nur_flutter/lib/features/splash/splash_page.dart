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
    await Future.delayed(const Duration(milliseconds: 1000));
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
      backgroundColor: kColorPrimaryDark,
      body: Container(
        decoration: const BoxDecoration(gradient: kAppGradient),
        child: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              // Icon masjid dengan lingkaran putih
              Container(
                width: 110,
                height: 110,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: kColorWhite.withOpacity(0.15),
                  border: Border.all(
                      color: kColorWhite.withOpacity(0.4), width: 2),
                ),
                child: const Icon(Icons.mosque, size: 58, color: kColorWhite),
              ),
              const SizedBox(height: 28),
              const Text(
                'Mas Nur',
                style: TextStyle(
                  fontSize: 30,
                  fontWeight: FontWeight.bold,
                  color: kColorWhite,
                  letterSpacing: 2,
                ),
              ),
              const SizedBox(height: 6),
              Text(
                'Admin Panel',
                style: TextStyle(
                  fontSize: 14,
                  color: kColorWhite.withOpacity(0.75),
                  letterSpacing: 2,
                ),
              ),
              const SizedBox(height: 48),
              SizedBox(
                width: 28,
                height: 28,
                child: CircularProgressIndicator(
                  strokeWidth: 2.5,
                  color: kColorWhite.withOpacity(0.8),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
