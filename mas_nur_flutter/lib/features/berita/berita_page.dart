import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/berita/berita_form_page.dart';
import 'package:mas_nur_flutter/features/berita/detail_berita_page.dart';

class BeritaPage extends StatefulWidget {
  const BeritaPage({super.key});
  static const routeName = '/berita';

  @override
  State<BeritaPage> createState() => _BeritaPageState();
}

class _BeritaPageState extends State<BeritaPage> {
  late Future<List<BeritaModel>> _future;

  @override
  void initState() {
    super.initState();
    _future = AppApiService.getBerita();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Berita')),
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          final updated = await Navigator.pushNamed(context, BeritaFormPage.routeName);
          if (updated == true) {
            setState(() => _future = AppApiService.getBerita());
          }
        },
        child: const Icon(Icons.add),
      ),
      body: FutureBuilder<List<BeritaModel>>(
        future: _future,
        builder: (_, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) return const Center(child: Text('Gagal memuat berita'));
          final items = snapshot.data ?? <BeritaModel>[];
          if (items.isEmpty) return const Center(child: Text('Belum ada berita'));
          return ListView.separated(
            itemCount: items.length,
            separatorBuilder: (context, _) => const Divider(height: 1),
            itemBuilder: (_, index) {
              final item = items[index];
              return ListTile(
                title: Text(item.judulBerita),
                subtitle: Text(item.tanggalBerita),
                onTap: () async {
                  final updated = await Navigator.pushNamed(
                    context,
                    DetailBeritaPage.routeName,
                    arguments: item,
                  );
                  if (updated == true) {
                    setState(() => _future = AppApiService.getBerita());
                  }
                },
                trailing: IconButton(
                  icon: const Icon(Icons.delete, color: Colors.red),
                  onPressed: () async {
                    final confirm = await _confirmDelete(context, 'berita ini');
                    if (confirm == true) {
                      await AppApiService.hapusBerita(item.idBerita);
                      if (!mounted) return;
                      setState(() => _future = AppApiService.getBerita());
                    }
                  },
                ),
              );
            },
          );
        },
      ),
    );
  }
}

Future<bool?> _confirmDelete(BuildContext context, String target) {
  return showDialog<bool>(
    context: context,
    builder: (_) => AlertDialog(
      title: const Text('Konfirmasi Hapus'),
      content: Text('Yakin ingin menghapus $target?'),
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

