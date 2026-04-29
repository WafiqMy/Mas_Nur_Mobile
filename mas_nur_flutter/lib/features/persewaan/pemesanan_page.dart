import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/persewaan/reservasi_detail_page.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_drawer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_header.dart';
import 'package:mas_nur_flutter/shared/widgets/swipe_page_shell.dart';

class PemesananPage extends StatefulWidget {
  const PemesananPage({super.key});
  static const routeName = '/pemesanan';

  @override
  State<PemesananPage> createState() => _PemesananPageState();
}

class _PemesananPageState extends State<PemesananPage> {
  late Future<List<ReservasiItemModel>> _future;

  @override
  void initState() {
    super.initState();
    _future = AppApiService.getAllReservasi();
  }

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) {
        if (!didPop) {
          Navigator.pushReplacementNamed(context, DashboardPage.routeName);
        }
      },
      child: SwipePageShell(
        currentIndex: 2,
        child: Scaffold(
          backgroundColor: kColorBackground,
          drawer: const AppDrawer(),
          body: Column(
          children: [
            const AppHeader(),
            Expanded(
            child: FutureBuilder<List<ReservasiItemModel>>(
              future: _future,
              builder: (_, snapshot) {
                if (snapshot.connectionState != ConnectionState.done) {
                  return const Center(child: CircularProgressIndicator());
                }
                if (snapshot.hasError) {
                  return const Center(child: Text('Gagal memuat pemesanan'));
                }
                final items = snapshot.data ?? <ReservasiItemModel>[];
                return Column(
                  children: [
                    const Padding(
                      padding: EdgeInsets.fromLTRB(16, 16, 16, 8),
                      child: Align(
                        alignment: Alignment.centerLeft,
                        child: Text(
                          'Data Pemesanan',
                          style: TextStyle(
                              fontSize: 20, fontWeight: FontWeight.bold),
                        ),
                      ),
                    ),
                    if (items.isEmpty)
                      const Expanded(
                          child: Center(child: Text('Belum ada pemesanan')))
                    else
                      Expanded(
                        child: RefreshIndicator(
                          onRefresh: () async {
                            setState(
                                () => _future = AppApiService.getAllReservasi());
                          },
                          child: ListView.builder(
                            padding: const EdgeInsets.symmetric(
                                horizontal: 16, vertical: 4),
                            itemCount: items.length,
                            itemBuilder: (_, index) {
                              final item = items[index];
                              return _PemesananCard(
                                item: item,
                                onLihat: () async {
                                  await Navigator.pushNamed(
                                    context,
                                    ReservasiDetailPage.routeName,
                                    arguments: item.id,
                                  );
                                  setState(() =>
                                      _future = AppApiService.getAllReservasi());
                                },
                              );
                            },
                          ),
                        ),
                      ),
                  ],
                );
              },
            ),
          ),
          const AppFooter(currentIndex: 2),
        ],
      ),
      ),   // SwipePageShell
    ),
  );
  }
}

class _PemesananCard extends StatelessWidget {
  const _PemesananCard({required this.item, required this.onLihat});
  final ReservasiItemModel item;
  final VoidCallback onLihat;

  @override
  Widget build(BuildContext context) {
    final props = item.extendedProps;
    return Card(
      elevation: 4,
      margin: const EdgeInsets.only(bottom: 12),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(kCardRadius)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Jenis Permintaan
            Text(
              'Permintaan Pemesanan ${props.jenis}',
              style: const TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                  color: Colors.black),
            ),
            const SizedBox(height: 4),
            // Nama Pemesan
            Text(
              'Atas Nama: ${props.peminjam}',
              style: const TextStyle(fontSize: 14, color: Color(0xFF444444)),
            ),
            const SizedBox(height: 8),
            // Status + Tombol Lihat
            Row(
              children: [
                Expanded(
                  child: Text(
                    'Status: ${props.status}',
                    style: const TextStyle(fontSize: 14, color: Color(0xFF888888)),
                  ),
                ),
                ElevatedButton(
                  onPressed: onLihat,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: kColorButton,
                    foregroundColor: Colors.white,
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(kButtonRadius)),
                    elevation: 0,
                    padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 6),
                    textStyle: const TextStyle(fontSize: 14),
                  ),
                  child: const Text('Lihat'),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
