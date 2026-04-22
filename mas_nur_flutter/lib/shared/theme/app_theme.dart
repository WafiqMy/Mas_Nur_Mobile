import 'package:flutter/material.dart';

// ─── Warna dari colors.xml ───────────────────────────────────────────────────
const Color kColorBackground = Color(0xFF98D3F7);   // colorBackground / header bg
const Color kColorHeader     = Color(0xFF99D5F9);   // header & footer
const Color kColorButton     = Color(0xFF99D5F9);   // bg_button_login (teal muda)
const Color kColorKembali    = Color(0xFFE6D7C3);   // bg_button_kembali (krem)
const Color kColorHapus      = Color(0xFFF6C8C8);   // bg_hapus (merah muda)
const Color kColorTextButton = Color(0xFF686565);   // colortextButton
const Color kColorWhite      = Colors.white;
const Color kColorBlack      = Colors.black;
const Color kColorGrey       = Color(0xFF888888);
const Color kColorInputBg    = Color(0xFFF5F5F5);   // bg_input_field (abu terang)
const Color kColorCardBg     = Colors.white;

// ─── Radius ──────────────────────────────────────────────────────────────────
const double kCardRadius   = 8.0;
const double kButtonRadius = 8.0;
const double kInputRadius  = 8.0;

// ─── Ukuran ──────────────────────────────────────────────────────────────────
const double kInputHeight  = 50.0;
const double kButtonHeight = 50.0;
const double kHeaderHeight = 60.0;
const double kFooterHeight = 60.0;

// ─── Widget helpers ──────────────────────────────────────────────────────────

/// Input field bergaya bg_input_field (abu terang, rounded)
InputDecoration kInputDecoration(String hint, {IconData? icon}) {
  return InputDecoration(
    hintText: hint,
    hintStyle: const TextStyle(color: kColorGrey),
    prefixIcon: icon != null ? Icon(icon, color: kColorGrey, size: 22) : null,
    filled: true,
    fillColor: kColorInputBg,
    contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
    border: OutlineInputBorder(
      borderRadius: BorderRadius.circular(kInputRadius),
      borderSide: BorderSide.none,
    ),
    enabledBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(kInputRadius),
      borderSide: BorderSide.none,
    ),
    focusedBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(kInputRadius),
      borderSide: const BorderSide(color: kColorBackground, width: 1.5),
    ),
  );
}

/// Tombol primary (bg_button_login) — biru muda
ButtonStyle kPrimaryButtonStyle = ElevatedButton.styleFrom(
  backgroundColor: kColorButton,
  foregroundColor: kColorTextButton,
  minimumSize: const Size(double.infinity, kButtonHeight),
  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(kButtonRadius)),
  elevation: 0,
  textStyle: const TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
);

/// Tombol secondary (bg_button_kembali) — krem
ButtonStyle kSecondaryButtonStyle = ElevatedButton.styleFrom(
  backgroundColor: kColorKembali,
  foregroundColor: kColorTextButton,
  minimumSize: const Size(double.infinity, kButtonHeight),
  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(kButtonRadius)),
  elevation: 0,
  textStyle: const TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
);

/// Tombol hapus/tolak (bg_hapus) — merah muda
ButtonStyle kDangerButtonStyle = ElevatedButton.styleFrom(
  backgroundColor: kColorHapus,
  foregroundColor: kColorTextButton,
  minimumSize: const Size(double.infinity, kButtonHeight),
  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(kButtonRadius)),
  elevation: 0,
  textStyle: const TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
);

/// Card standar
BoxDecoration kCardDecoration = BoxDecoration(
  color: kColorCardBg,
  borderRadius: BorderRadius.circular(kCardRadius),
  boxShadow: const [
    BoxShadow(color: Color(0x22000000), blurRadius: 4, offset: Offset(0, 2)),
  ],
);
