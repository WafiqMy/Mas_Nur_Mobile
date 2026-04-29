import 'package:flutter/material.dart';

// ─── Palet Blue Royal Modern ──────────────────────────────────────────────────
// Biru gelap    : #0D47A1  → header, drawer (atas gradasi)
// Biru royal    : #1565C0  → tengah gradasi
// Biru cerah    : #1976D2  → tombol primary, aksen utama
// Biru muda     : #42A5F5  → highlight, icon aktif, aksen
// Putih         : #FFFFFF  → background utama, card surface
// Abu terang    : #F4F6FA  → background scaffold
// Teks gelap    : #1A1A2E  → teks utama
// Teks abu      : #6B7280  → teks sekunder / hint
// Merah hapus   : #E53935  → aksi hapus
// Divider       : #E5E7EB  → garis pemisah

const Color kColorPrimary      = Color(0xFF1976D2); // biru cerah (tombol, aksen)
const Color kColorPrimaryDark  = Color(0xFF0D47A1); // biru gelap (header atas)
const Color kColorPrimaryMid   = Color(0xFF1565C0); // biru royal (tengah gradasi)
const Color kColorPrimaryLight = Color(0xFF42A5F5); // biru muda (highlight)
const Color kColorAccent       = Color(0xFFBBDEFB); // biru pastel (background aksen)
const Color kColorGold         = Color(0xFFFFB300); // emas aksen sekunder
const Color kColorWhite        = Color(0xFFFFFFFF); // putih
const Color kColorBackground   = Color(0xFFF4F6FA); // background scaffold
const Color kColorCardBg       = Color(0xFFFFFFFF); // card putih
const Color kColorTextPrimary  = Color(0xFF1A1A2E); // teks utama
const Color kColorTextSecondary= Color(0xFF6B7280); // teks sekunder
const Color kColorHapus        = Color(0xFFE53935); // merah hapus
const Color kColorDivider      = Color(0xFFE5E7EB); // divider

// Alias agar kode lama tidak error
const Color kColorNavy         = kColorPrimaryDark;
const Color kColorNavyLight    = Color(0xFFE3F2FD); // biru sangat muda
const Color kColorRoyal        = kColorPrimary;
const Color kColorSkyBlue      = kColorPrimaryLight;
const Color kColorYellow       = kColorGold;
const Color kColorWhiteSoft    = kColorTextSecondary;
const Color kColorGrey         = Color(0xFF9CA3AF);
const Color kColorHeader       = kColorPrimaryDark;
const Color kColorButton       = kColorPrimary;
const Color kColorKembali      = kColorNavyLight;
const Color kColorTextButton   = kColorWhite;
const Color kColorInputBg      = kColorWhite;
const Color kColorBlack        = kColorTextPrimary;

// ─── Gradasi standar (dipakai di semua header, drawer, splash, banner) ───────
// Biru gelap (#0D47A1) → biru royal (#1565C0) → biru cerah (#1976D2)
const LinearGradient kAppGradient = LinearGradient(
  begin: Alignment.topCenter,
  end: Alignment.bottomCenter,
  colors: [kColorPrimaryDark, kColorPrimaryMid, kColorPrimary],
  stops: [0.0, 0.5, 1.0],
);

// ─── Radius ──────────────────────────────────────────────────────────────────
const double kCardRadius   = 14.0;
const double kButtonRadius = 12.0;
const double kInputRadius  = 12.0;

// ─── Ukuran ──────────────────────────────────────────────────────────────────
const double kInputHeight  = 52.0;
const double kButtonHeight = 52.0;
const double kHeaderHeight = 60.0;
const double kFooterHeight = 60.0;

// ─── MaterialApp ThemeData ────────────────────────────────────────────────────
ThemeData buildAppTheme() {
  return ThemeData(
    useMaterial3: true,
    colorScheme: ColorScheme.light(
      primary: kColorPrimary,
      secondary: kColorGold,
      surface: kColorWhite,
      onPrimary: kColorWhite,
      onSecondary: kColorTextPrimary,
      onSurface: kColorTextPrimary,
      error: kColorHapus,
    ),
    scaffoldBackgroundColor: kColorBackground,
    cardColor: kColorCardBg,
    cardTheme: CardThemeData(
      color: kColorCardBg,
      elevation: 2,
      shadowColor: Colors.black.withValues(alpha: 0.08),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(kCardRadius),
      ),
    ),
    appBarTheme: const AppBarTheme(
      backgroundColor: kColorPrimaryDark,
      foregroundColor: kColorWhite,
      elevation: 0,
      centerTitle: true,
      titleTextStyle: TextStyle(
        color: kColorWhite,
        fontSize: 18,
        fontWeight: FontWeight.w600,
        letterSpacing: 0.5,
      ),
      iconTheme: IconThemeData(color: kColorWhite),
    ),
    elevatedButtonTheme: ElevatedButtonThemeData(
      style: ElevatedButton.styleFrom(
        backgroundColor: kColorPrimary,
        foregroundColor: kColorWhite,
        elevation: 0,
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(kButtonRadius)),
        textStyle:
            const TextStyle(fontSize: 14, fontWeight: FontWeight.w600),
      ),
    ),
    filledButtonTheme: FilledButtonThemeData(
      style: FilledButton.styleFrom(
        backgroundColor: kColorPrimary,
        foregroundColor: kColorWhite,
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(kButtonRadius)),
        textStyle:
            const TextStyle(fontSize: 14, fontWeight: FontWeight.w600),
      ),
    ),
    textButtonTheme: TextButtonThemeData(
      style: TextButton.styleFrom(foregroundColor: kColorPrimary),
    ),
    inputDecorationTheme: InputDecorationTheme(
      filled: true,
      fillColor: kColorWhite,
      hintStyle: const TextStyle(color: kColorGrey),
      labelStyle: const TextStyle(color: kColorPrimary),
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
    textTheme: const TextTheme(
      bodyLarge: TextStyle(color: kColorTextPrimary),
      bodyMedium: TextStyle(color: kColorTextSecondary),
      bodySmall: TextStyle(color: kColorGrey),
      titleLarge: TextStyle(
          color: kColorTextPrimary, fontWeight: FontWeight.bold),
      titleMedium: TextStyle(
          color: kColorTextPrimary, fontWeight: FontWeight.w600),
      titleSmall: TextStyle(color: kColorTextSecondary),
    ),
    dividerTheme: const DividerThemeData(
      color: kColorDivider,
      thickness: 1,
    ),
    tabBarTheme: const TabBarThemeData(
      labelColor: kColorPrimary,
      unselectedLabelColor: kColorGrey,
      indicatorColor: kColorPrimary,
    ),
    dialogTheme: DialogThemeData(
      backgroundColor: kColorWhite,
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16)),
      titleTextStyle: const TextStyle(
          color: kColorTextPrimary, fontSize: 18, fontWeight: FontWeight.bold),
      contentTextStyle:
          const TextStyle(color: kColorTextSecondary, fontSize: 14),
    ),
    snackBarTheme: SnackBarThemeData(
      backgroundColor: kColorPrimaryDark,
      contentTextStyle: const TextStyle(color: kColorWhite),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
      behavior: SnackBarBehavior.floating,
    ),
    progressIndicatorTheme:
        const ProgressIndicatorThemeData(color: kColorPrimary),
    checkboxTheme: CheckboxThemeData(
      fillColor: WidgetStateProperty.all(kColorPrimary),
    ),
    switchTheme: SwitchThemeData(
      thumbColor: WidgetStateProperty.all(kColorPrimary),
      trackColor: WidgetStateProperty.all(kColorAccent),
    ),
  );
}

// ─── Input decoration helper ──────────────────────────────────────────────────
InputDecoration kInputDecoration(String hint, {IconData? icon}) {
  return InputDecoration(
    hintText: hint,
    hintStyle: const TextStyle(color: kColorGrey),
    prefixIcon:
        icon != null ? Icon(icon, color: kColorPrimary, size: 22) : null,
    filled: true,
    fillColor: kColorWhite,
    contentPadding:
        const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
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
  );
}

// ─── Button styles ────────────────────────────────────────────────────────────
ButtonStyle kPrimaryButtonStyle = ElevatedButton.styleFrom(
  backgroundColor: kColorPrimary,
  foregroundColor: kColorWhite,
  minimumSize: const Size(double.infinity, kButtonHeight),
  shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(kButtonRadius)),
  elevation: 0,
  textStyle:
      const TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
);

ButtonStyle kSecondaryButtonStyle = ElevatedButton.styleFrom(
  backgroundColor: kColorWhite,
  foregroundColor: kColorPrimary,
  minimumSize: const Size(double.infinity, kButtonHeight),
  shape: RoundedRectangleBorder(
    borderRadius: BorderRadius.circular(kButtonRadius),
    side: const BorderSide(color: kColorPrimary, width: 1.5),
  ),
  elevation: 0,
  textStyle:
      const TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
);

ButtonStyle kDangerButtonStyle = ElevatedButton.styleFrom(
  backgroundColor: kColorHapus,
  foregroundColor: kColorWhite,
  minimumSize: const Size(double.infinity, kButtonHeight),
  shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(kButtonRadius)),
  elevation: 0,
  textStyle:
      const TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
);

BoxDecoration kCardDecoration = BoxDecoration(
  color: kColorCardBg,
  borderRadius: BorderRadius.circular(kCardRadius),
  boxShadow: [
    BoxShadow(
      color: Colors.black.withValues(alpha: 0.06),
      blurRadius: 8,
      offset: const Offset(0, 2),
    ),
  ],
);
