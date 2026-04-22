import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';

class NotifikasiPage extends StatelessWidget {
  const NotifikasiPage({super.key});
  static const routeName = '/notifikasi';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Notifikasi')),
      body: FutureBuilder<List<NotificationItem>>(
        future: AppApiService.getNotifications(),
        builder: (_, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) return const Center(child: Text('Gagal memuat notifikasi'));
          final items = snapshot.data ?? <NotificationItem>[];
          if (items.isEmpty) return const Center(child: Text('Belum ada notifikasi'));
          return ListView.builder(
            itemCount: items.length,
            itemBuilder: (_, index) {
              final item = items[index];
              return ListTile(
                title: Text(item.namaPengguna),
                subtitle: Text(item.jenis),
                leading: const Icon(Icons.notification_important),
              );
            },
          );
        },
      ),
    );
  }
}

