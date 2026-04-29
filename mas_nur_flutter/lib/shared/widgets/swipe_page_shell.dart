import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/features/infaq/infaq_page.dart';
import 'package:mas_nur_flutter/features/persewaan/pemesanan_page.dart';
import 'package:mas_nur_flutter/shared/utils/app_navigation.dart';

/// Membungkus halaman footer (index 0-2) agar bisa digeser kiri/kanan
/// untuk berpindah tab, sekaligus tetap mendukung tap tombol footer.
///
/// Urutan tab: 0=Beranda, 1=Infaq, 2=Pemesanan
class SwipePageShell extends StatelessWidget {
  const SwipePageShell({
    super.key,
    required this.currentIndex,
    required this.child,
  });

  final int currentIndex;
  final Widget child;

  // Ambang minimum jarak geser (px) agar dianggap swipe
  static const double _swipeThreshold = 60.0;

  void _goTo(BuildContext context, int index) {
    Widget page;
    switch (index) {
      case 0:
        page = const DashboardPage();
        break;
      case 1:
        page = const InfaqPage();
        break;
      case 2:
        page = const PemesananPage();
        break;
      default:
        return;
    }
    Navigator.pushReplacement(context, fadeRoute(page));
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      // Hanya tangkap swipe horizontal yang cukup jauh
      onHorizontalDragEnd: (details) {
        final velocity = details.primaryVelocity ?? 0;

        // Geser ke kiri (velocity negatif) → tab berikutnya
        if (velocity < -_swipeThreshold && currentIndex < 2) {
          _goTo(context, currentIndex + 1);
        }
        // Geser ke kanan (velocity positif) → tab sebelumnya
        else if (velocity > _swipeThreshold && currentIndex > 0) {
          _goTo(context, currentIndex - 1);
        }
      },
      child: child,
    );
  }
}
