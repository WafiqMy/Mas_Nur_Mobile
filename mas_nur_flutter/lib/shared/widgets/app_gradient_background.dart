import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';

/// Wrapper gradasi standar — pakai kAppGradient dari app_theme.
class AppGradientBackground extends StatelessWidget {
  const AppGradientBackground({super.key, required this.child});
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(gradient: kAppGradient),
      child: child,
    );
  }
}
