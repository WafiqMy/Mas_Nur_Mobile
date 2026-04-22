import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/persewaan/barang_form_page.dart';
import 'package:mas_nur_flutter/features/persewaan/detail_barang_page.dart';
import 'package:mas_nur_flutter/features/persewaan/pemesanan_page.dart';

class PersewaanPage extends StatefulWidget {
  const PersewaanPage({super.key});
  static const routeName = '/persewaan';

  @override
  State<PersewaanPage> createState() => _PersewaanPageState();
}

class _PersewaanPageState extends State<PersewaanPage> {
  late Future<List<BarangModel>> _future;

  @override
  void initState() {
    super.initState();
    _future = AppApiService.getBarang();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Persewaan')),
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          final updated = await Navigator.pushNamed(context, BarangFormPage.routeName);
          if (updated == true && mounted) {
            setState(() => _future = AppApiService.getBarang());
          }
        },
        child: const Icon(Icons.add),
      ),
      body: FutureBuilder<List<BarangModel>>(
        future: _future,
        builder: (_, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) return const Center(child: Text('Gagal memuat barang'));
          final items = snapshot.data ?? <BarangModel>[];
          return Column(
            children: [
              Padding(
                padding: const EdgeInsets.all(12),
                child: SizedBox(
                  width: double.infinity,
                  child: OutlinedButton.icon(
                    onPressed: () => Navigator.pushNamed(context, PemesananPage.routeName),
                    icon: const Icon(Icons.list_alt),
                    label: const Text('Lihat Data Pemesanan'),
                  ),
                ),
              ),
              Expanded(
                child: items.isEmpty
                    ? const Center(child: Text('Belum ada barang'))
                    : ListView.builder(
                        itemCount: items.length,
                        itemBuilder: (_, index) {
                          final item = items[index];
                          return ListTile(
                            title: Text(item.namaBarang),
                            subtitle: Text('Jenis: ${item.jenis} | Stok: ${item.jumlah}'),
                            trailing: Text('Rp ${item.harga}'),
                            onTap: () async {
                              final updated = await Navigator.pushNamed(
                                context,
                                DetailBarangPage.routeName,
                                arguments: item,
                              );
                              if (updated == true && mounted) {
                                setState(() => _future = AppApiService.getBarang());
                              }
                            },
                            leading: IconButton(
                              icon: const Icon(Icons.delete, color: Colors.red),
                              onPressed: () async {
                                final confirm = await _confirmDeleteBarang(context);
                                if (confirm == true && mounted) {
                                  await AppApiService.hapusBarang(item.idPersewaan);
                                  if (mounted) {
                                    setState(() => _future = AppApiService.getBarang());
                                  }
                                }
                              },
                            ),
                          );
                        },
                      ),
              ),
            ],
          );
        },
      ),
    );
  }
}

Future<bool?> _confirmDeleteBarang(BuildContext context) {
  return showDialog<bool>(
    context: context,
    builder: (_) => AlertDialog(
      title: const Text('Konfirmasi Hapus'),
      content: const Text('Yakin ingin menghapus barang ini?'),
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

