import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/persewaan/reservasi_detail_page.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_drawer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_header.dart';

class NotifikasiPage extends StatefulWidget {
  const NotifikasiPage({super.key});
  static const routeName = '/notifikasi';

  @override
  State<NotifikasiPage> createState() => _NotifikasiPageState();
}

class _NotifikasiPageState extends State<NotifikasiPage> {
  late Future<List<NotificationItem>> _future;

  @override
  void initState() {
    super.initState();
    _future = AppApiService.getNotifications();
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
      child: Scaffold(
        backgroundColor: kColorWhite,
        drawer: const AppDrawer(),
        body: Column(
          children: [
            const AppHeader(showBackButton: true),
            Expanded(
              child: FutureBuilder<List<NotificationItem>>(
                future: _future,
                builder: (_, snapshot) {
                  if (snapshot.connectionState != ConnectionState.done) {
                    return const Center(child: CircularProgressIndicator());
                  }
                  if (snapshot.hasError) {
                    return const Center(child: Text('Gagal memuat notifikasi'));
                  }
                  final items = snapshot.data ?? <NotificationItem>[];
                  return Column(
                    children: [
                      const Padding(
                        padding: EdgeInsets.fromLTRB(16, 16, 16, 8),
                        child: Align(
                          alignment: Alignment.centerLeft,
                          child: Text(
                            'Notifikasi',
                            style: TextStyle(
                                fontSize: 20, fontWeight: FontWeight.bold),
                          ),
                        ),
                      ),
                      if (items.isEmpty)
                        const Expanded(
                            child: Center(child: Text('Tidak ada notifikasi')))
                      else
                        Expanded(
                          child: ListView.builder(
                            padding: const EdgeInsets.symmetric(vertical: 4),
                            itemCount: items.length,
                            itemBuilder: (_, index) {
                              final item = items[index];
                              return _NotifCard(
                                item: item,
                                onLihat: () {
                                  final id = int.tryParse(item.idReservasi);
                                  if (id != null) {
                                    Navigator.pushNamed(
                                      context,
                                      ReservasiDetailPage.routeName,
                                      arguments: id,
                                    );
                                  }
                                },
                              );
                            },
                          ),
                        ),
                    ],
                  );
                },
              ),
            ),
            const AppFooter(currentIndex: -1),
          ],
        ),
      ),
    );
  }
}

class _NotifCard extends StatelessWidget {
  const _NotifCard({required this.item, required this.onLihat});
  final NotificationItem item;
  final VoidCallback onLihat;

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 4,
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      color: kColorWhite,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Row(
          children: [
            // Teks kiri
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Permintaan Pemesanan ${item.jenis}',
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF0D47A1),
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    'Atas Nama: ${item.namaPengguna}',
                    style: const TextStyle(fontSize: 14, color: Color(0xFF424242)),
                  ),
                ],
              ),
            ),
            const SizedBox(width: 16),
            // Tombol Lihat
            ElevatedButton(
              onPressed: onLihat,
              style: ElevatedButton.styleFrom(
                backgroundColor: kColorButton,
                foregroundColor: const Color(0xFF00695C),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8)),
                elevation: 0,
              ),
              child: const Text('Lihat'),
            ),
          ],
        ),
      ),
    );
  }
}
