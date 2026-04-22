import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/acara/acara_form_page.dart';
import 'package:mas_nur_flutter/features/acara/detail_acara_page.dart';

class AcaraPage extends StatefulWidget {
  const AcaraPage({super.key});
  static const routeName = '/acara';

  @override
  State<AcaraPage> createState() => _AcaraPageState();
}

class _AcaraPageState extends State<AcaraPage> {
  late Future<List<AcaraModel>> _future;

  @override
  void initState() {
    super.initState();
    _future = AppApiService.getAcara();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Acara')),
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          final updated = await Navigator.pushNamed(context, AcaraFormPage.routeName);
          if (updated == true && mounted) {
            setState(() => _future = AppApiService.getAcara());
          }
        },
        child: const Icon(Icons.add),
      ),
      body: FutureBuilder<List<AcaraModel>>(
        future: _future,
        builder: (_, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) return const Center(child: Text('Gagal memuat acara'));
          final items = snapshot.data ?? <AcaraModel>[];
          if (items.isEmpty) return const Center(child: Text('Belum ada acara'));
          return ListView.builder(
            itemCount: items.length,
            itemBuilder: (_, index) {
              final item = items[index];
              return Card(
                margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                child: ListTile(
                  title: Text(item.namaEvent),
                  subtitle: Text('${item.tanggalEvent} - ${item.lokasiEvent}'),
                  onTap: () async {
                    final updated = await Navigator.pushNamed(
                      context,
                      DetailAcaraPage.routeName,
                      arguments: item,
                    );
                    if (updated == true && mounted) {
                      setState(() => _future = AppApiService.getAcara());
                    }
                  },
                  trailing: IconButton(
                    icon: const Icon(Icons.delete, color: Colors.red),
                    onPressed: () async {
                      final confirm = await _confirmDeleteAcara(context);
                      if (confirm == true) {
                        await AppApiService.hapusAcara(item.idEvent);
                        if (mounted) {
                          setState(() => _future = AppApiService.getAcara());
                        }
                      }
                    },
                  ),
                ),
              );
            },
          );
        },
      ),
    );
  }
}

Future<bool?> _confirmDeleteAcara(BuildContext context) {
  return showDialog<bool>(
    context: context,
    builder: (_) => AlertDialog(
      title: const Text('Konfirmasi Hapus'),
      content: const Text('Yakin ingin menghapus acara ini?'),
      actions: [
        TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Batal')),
        TextButton(
          onPressed: () => Navigator.pop(context, true),
          child: const Text('Hapus', style: TextStyle(color: Colors.red)),
        ),
      ],
    ),
  );
}

