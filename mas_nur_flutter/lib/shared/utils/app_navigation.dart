import 'package:flutter/material.dart';

/// Transisi fade smooth untuk perpindahan antar halaman utama.
extension AppNavigation on BuildContext {
  /// Ganti halaman saat ini dengan fade (tidak menumpuk di stack).
  Future<T?> fadeReplaceTo<T extends Object?>(
    String routeName, {
    Object? arguments,
  }) {
    return Navigator.of(this).pushReplacementNamed<T, Object?>(
      routeName,
      arguments: arguments,
      result: null,
    );
  }
}

/// PageRoute dengan animasi fade — dipakai oleh AppFooter dan AppDrawer.
Route<T> fadeRoute<T>(Widget page, {String? name}) {
  return PageRouteBuilder<T>(
    settings: RouteSettings(name: name),
    pageBuilder: (_, __, ___) => page,
    transitionsBuilder: (_, animation, __, child) => FadeTransition(
      opacity: CurvedAnimation(parent: animation, curve: Curves.easeInOut),
      child: child,
    ),
    transitionDuration: const Duration(milliseconds: 220),
  );
}
