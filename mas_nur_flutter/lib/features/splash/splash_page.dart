import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';
import 'package:mas_nur_flutter/features/auth/login_page.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_gradient_background.dart';

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
    await Future.delayed(const Duration(milliseconds: 800));
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
      backgroundColor: Colors.transparent,
      body: AppGradientBackground(
        child: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              // Icon dengan glow effect
              Container(
                width: 100,
                height: 100,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: kColorNavyLight,
                  border: Border.all(color: kColorSkyBlue.withOpacity(0.5), width: 1.5),
                  boxShadow: [
                    BoxShadow(
                      color: kColorSkyBlue.withOpacity(0.25),
                      blurRadius: 24,
                      spreadRadius: 4,
                    ),
                  ],
                ),
                child: const Icon(Icons.mosque, size: 52, color: kColorSkyBlue),
              ),
              const SizedBox(height: 24),
              const Text(
                'Mas Nur',
                style: TextStyle(
                  fontSize: 28,
                  fontWeight: FontWeight.bold,
                  color: kColorWhite,
                  letterSpacing: 2,
                ),
              ),
              const SizedBox(height: 6),
              Text(
                'Admin Panel',
                style: TextStyle(
                  fontSize: 13,
                  color: kColorSkyBlue.withOpacity(0.8),
                  letterSpacing: 1.5,
                ),
              ),
              const SizedBox(height: 40),
              SizedBox(
                width: 24,
                height: 24,
                child: CircularProgressIndicator(
                  strokeWidth: 2,
                  color: kColorSkyBlue.withOpacity(0.7),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
