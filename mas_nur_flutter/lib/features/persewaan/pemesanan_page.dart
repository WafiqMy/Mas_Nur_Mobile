import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/persewaan/reservasi_detail_page.dart';

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
    return Scaffold(
      appBar: AppBar(title: const Text('Data Pemesanan')),
      body: FutureBuilder<List<ReservasiItemModel>>(
        future: _future,
        builder: (context, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            return const Center(child: Text('Gagal memuat pemesanan'));
          }
          final items = snapshot.data ?? [];
          if (items.isEmpty) return const Center(child: Text('Tidak ada pemesanan'));
          return RefreshIndicator(
            onRefresh: () async {
              setState(() => _future = AppApiService.getAllReservasi());
            },
            child: ListView.builder(
              itemCount: items.length,
              itemBuilder: (context, index) {
                final item = items[index];
                return ListTile(
                  title: Text(item.title.isEmpty ? item.extendedProps.peminjam : item.title),
                  subtitle: Text('${item.extendedProps.barang} • ${item.extendedProps.status}'),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () async {
                    final updated = await Navigator.pushNamed(
                      context,
                      ReservasiDetailPage.routeName,
                      arguments: item.id,
                    );
                    if (updated == true && mounted) {
                      setState(() => _future = AppApiService.getAllReservasi());
                    }
                  },
                );
              },
            ),
          );
        },
      ),
    );
  }
}

