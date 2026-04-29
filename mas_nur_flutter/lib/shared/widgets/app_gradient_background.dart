import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';

/// Gradasi background futuristik:
/// Navy gelap (#080840) → Biru royal (#1565C0) → Biru muda (#90CAF9) → Kuning pastel (#FFE082)
/// Dipakai sebagai pengganti backgroundColor di semua Scaffold.
class AppGradientBackground extends StatelessWidget {
  const AppGradientBackground({super.key, required this.child});
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [
            kColorNavy,          // #080840 — atas
            Color(0xFF0D2B6B),   // transisi navy → royal
            kColorRoyal,         // #1565C0 — tengah atas
            kColorSkyBlue,       // #90CAF9 — tengah bawah
            kColorYellow,        // #FFE082 — bawah
          ],
          stops: [0.0, 0.25, 0.5, 0.78, 1.0],
        ),
      ),
      child: child,
    );
  }
}
