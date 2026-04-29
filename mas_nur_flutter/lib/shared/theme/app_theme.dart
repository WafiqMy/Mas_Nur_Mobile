import 'package:flutter/material.dart';

// ─── Palet Futuristik Minimalis ───────────────────────────────────────────────
// Navy gelap  : #080840  → background utama, header, footer, drawer
// Biru royal  : #1565C0  → aksen, tombol primary
// Biru muda   : #90CAF9  → highlight, border aktif, icon aktif
// Kuning pastel: #FFE082 → aksen sekunder, badge, tombol aksi
// Putih       : #FFFFFF  → teks utama, card surface
// Abu gelap   : #1A1A3A  → card background di atas navy

const Color kColorNavy       = Color(0xFF080840); // background utama
const Color kColorNavyLight  = Color(0xFF0D1257); // card / surface
const Color kColorRoyal      = Color(0xFF1565C0); // tombol primary
const Color kColorSkyBlue    = Color(0xFF90CAF9); // aksen / highlight
const Color kColorYellow     = Color(0xFFFFE082); // aksen kuning
const Color kColorWhite      = Color(0xFFFFFFFF); // teks & icon
const Color kColorWhiteSoft  = Color(0xFFE8EAF6); // teks sekunder
const Color kColorHapus      = Color(0xFFEF5350); // merah hapus
const Color kColorGrey       = Color(0xFF7986CB); // placeholder / hint

// Alias agar kode lama tidak error
const Color kColorBackground = kColorNavy;
const Color kColorHeader     = kColorNavy;
const Color kColorButton     = kColorRoyal;
const Color kColorKembali    = kColorNavyLight;
const Color kColorTextButton = kColorWhite;
const Color kColorInputBg    = kColorNavyLight;
const Color kColorCardBg     = kColorNavyLight;
const Color kColorBlack      = kColorNavy;

// ─── Radius ──────────────────────────────────────────────────────────────────
const double kCardRadius   = 12.0;
const double kButtonRadius = 10.0;
const double kInputRadius  = 10.0;

// ─── Ukuran ──────────────────────────────────────────────────────────────────
const double kInputHeight  = 52.0;
const double kButtonHeight = 52.0;
const double kHeaderHeight = 60.0;
const double kFooterHeight = 60.0;

// ─── MaterialApp ThemeData ────────────────────────────────────────────────────
ThemeData buildAppTheme() {
  return ThemeData(
    useMaterial3: true,
    colorScheme: ColorScheme.dark(
      primary: kColorSkyBlue,
      secondary: kColorYellow,
      surface: kColorNavyLight,
      onPrimary: kColorNavy,
      onSecondary: kColorNavy,
      onSurface: kColorWhite,
      error: kColorHapus,
    ),
    scaffoldBackgroundColor: kColorNavy,
    cardColor: kColorNavyLight,
    cardTheme: CardThemeData(
      color: kColorNavyLight,
      elevation: 0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(kCardRadius),
        side: BorderSide(color: kColorSkyBlue.withOpacity(0.18), width: 1),
      ),
    ),
    appBarTheme: const AppBarTheme(
      backgroundColor: kColorNavy,
      foregroundColor: kColorWhite,
      elevation: 0,
      centerTitle: true,
      titleTextStyle: TextStyle(
        color: kColorWhite,
        fontSize: 18,
        fontWeight: FontWeight.w600,
        letterSpacing: 0.5,
      ),
      iconTheme: IconThemeData(color: kColorSkyBlue),
    ),
    elevatedButtonTheme: ElevatedButtonThemeData(
      style: ElevatedButton.styleFrom(
        backgroundColor: kColorRoyal,
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
        backgroundColor: kColorRoyal,
        foregroundColor: kColorWhite,
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(kButtonRadius)),
        textStyle:
            const TextStyle(fontSize: 14, fontWeight: FontWeight.w600),
      ),
    ),
    textButtonTheme: TextButtonThemeData(
      style: TextButton.styleFrom(foregroundColor: kColorSkyBlue),
    ),
    inputDecorationTheme: InputDecorationTheme(
      filled: true,
      fillColor: kColorNavyLight,
      hintStyle: TextStyle(color: kColorGrey.withOpacity(0.7)),
      labelStyle: const TextStyle(color: kColorSkyBlue),
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(kInputRadius),
        borderSide: BorderSide(color: kColorSkyBlue.withOpacity(0.3)),
      ),
      enabledBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(kInputRadius),
        borderSide: BorderSide(color: kColorSkyBlue.withOpacity(0.2)),
      ),
      focusedBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(kInputRadius),
        borderSide: const BorderSide(color: kColorSkyBlue, width: 1.5),
      ),
      contentPadding:
          const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
    ),
    textTheme: const TextTheme(
      bodyLarge: TextStyle(color: kColorWhite),
      bodyMedium: TextStyle(color: kColorWhiteSoft),
      bodySmall: TextStyle(color: kColorGrey),
      titleLarge: TextStyle(
          color: kColorWhite, fontWeight: FontWeight.bold),
      titleMedium: TextStyle(
          color: kColorWhite, fontWeight: FontWeight.w600),
      titleSmall: TextStyle(color: kColorWhiteSoft),
    ),
    dividerTheme: DividerThemeData(
      color: kColorSkyBlue.withOpacity(0.15),
      thickness: 1,
    ),
    tabBarTheme: const TabBarThemeData(
      labelColor: kColorSkyBlue,
      unselectedLabelColor: kColorGrey,
      indicatorColor: kColorSkyBlue,
    ),
    dialogTheme: DialogThemeData(
      backgroundColor: kColorNavyLight,
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16)),
      titleTextStyle: const TextStyle(
          color: kColorWhite, fontSize: 18, fontWeight: FontWeight.bold),
      contentTextStyle:
          const TextStyle(color: kColorWhiteSoft, fontSize: 14),
    ),
    snackBarTheme: SnackBarThemeData(
      backgroundColor: kColorNavyLight,
      contentTextStyle: const TextStyle(color: kColorWhite),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
      behavior: SnackBarBehavior.floating,
    ),
    progressIndicatorTheme:
        const ProgressIndicatorThemeData(color: kColorSkyBlue),
    checkboxTheme: CheckboxThemeData(
      fillColor: WidgetStateProperty.all(kColorRoyal),
    ),
    switchTheme: SwitchThemeData(
      thumbColor: WidgetStateProperty.all(kColorSkyBlue),
      trackColor: WidgetStateProperty.all(kColorRoyal.withOpacity(0.4)),
    ),
  );
}

// ─── Input decoration helper ──────────────────────────────────────────────────
InputDecoration kInputDecoration(String hint, {IconData? icon}) {
  return InputDecoration(
    hintText: hint,
    hintStyle: TextStyle(color: kColorGrey.withOpacity(0.7)),
    prefixIcon:
        icon != null ? Icon(icon, color: kColorGrey, size: 22) : null,
    filled: true,
    fillColor: kColorNavyLight,
    contentPadding:
        const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
    border: OutlineInputBorder(
      borderRadius: BorderRadius.circular(kInputRadius),
      borderSide: BorderSide(color: kColorSkyBlue.withOpacity(0.2)),
    ),
    enabledBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(kInputRadius),
      borderSide: BorderSide(color: kColorSkyBlue.withOpacity(0.2)),
    ),
    focusedBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(kInputRadius),
      borderSide: const BorderSide(color: kColorSkyBlue, width: 1.5),
    ),
  );
}

// ─── Button styles ────────────────────────────────────────────────────────────
ButtonStyle kPrimaryButtonStyle = ElevatedButton.styleFrom(
  backgroundColor: kColorRoyal,
  foregroundColor: kColorWhite,
  minimumSize: const Size(double.infinity, kButtonHeight),
  shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(kButtonRadius)),
  elevation: 0,
  textStyle:
      const TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
);

ButtonStyle kSecondaryButtonStyle = ElevatedButton.styleFrom(
  backgroundColor: kColorNavyLight,
  foregroundColor: kColorSkyBlue,
  minimumSize: const Size(double.infinity, kButtonHeight),
  shape: RoundedRectangleBorder(
    borderRadius: BorderRadius.circular(kButtonRadius),
    side: const BorderSide(color: kColorSkyBlue, width: 1),
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
  color: kColorNavyLight,
  borderRadius: BorderRadius.circular(kCardRadius),
  border: Border.all(color: kColorSkyBlue.withOpacity(0.18)),
);
