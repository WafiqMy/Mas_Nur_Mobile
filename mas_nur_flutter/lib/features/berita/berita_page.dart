import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/berita/berita_form_page.dart';
import 'package:mas_nur_flutter/features/berita/detail_berita_page.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_drawer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_header.dart';

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
    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) {
        if (!didPop) {
          Navigator.pushReplacementNamed(context, DashboardPage.routeName);
        }
      },
      child: Scaffold(
        backgroundColor: kColorBackground,
        drawer: const AppDrawer(),
        body: Column(
          children: [
          const AppHeader(showBackButton: true),
          Expanded(
            child: FutureBuilder<List<BeritaModel>>(
              future: _future,
              builder: (_, snapshot) {
                if (snapshot.connectionState != ConnectionState.done) {
                  return const Center(child: CircularProgressIndicator());
                }
                if (snapshot.hasError) {
                  return const Center(child: Text('Gagal memuat berita'));
                }
                final items = snapshot.data ?? <BeritaModel>[];
                return Column(
                  children: [
                    // Judul halaman
                    Padding(
                      padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                      child: Row(
                        children: [
                          const Expanded(
                            child: Text(
                              'Kelola Berita',
                              style: TextStyle(
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                          // Tombol Tambah
                          ElevatedButton.icon(
                            onPressed: () async {
                              final updated = await Navigator.pushNamed(
                                  context, BeritaFormPage.routeName);
                              if (updated == true && mounted) {
                                setState(() => _future = AppApiService.getBerita());
                              }
                            },
                            icon: const Icon(Icons.add, size: 18),
                            label: const Text('Tambah'),
                            style: ElevatedButton.styleFrom(
                              backgroundColor: kColorButton,
                              foregroundColor: kColorTextButton,
                              shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(kButtonRadius)),
                              elevation: 0,
                            ),
                          ),
                        ],
                      ),
                    ),
                    if (items.isEmpty)
                      const Expanded(
                          child: Center(child: Text('Belum ada berita')))
                    else
                      Expanded(
                        child: ListView.builder(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 16, vertical: 4),
                          itemCount: items.length,
                          itemBuilder: (_, index) =>
                              _BeritaCard(item: items[index], onRefresh: () {
                            setState(() => _future = AppApiService.getBerita());
                          }),
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

class _BeritaCard extends StatelessWidget {
  const _BeritaCard({required this.item, required this.onRefresh});
  final BeritaModel item;
  final VoidCallback onRefresh;

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 4,
      margin: const EdgeInsets.only(bottom: 12),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(kCardRadius)),
      child: Column(
        children: [
          // Baris gambar + info
          Padding(
            padding: const EdgeInsets.all(12),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Gambar 100x80
                ClipRRect(
                  borderRadius: BorderRadius.circular(4),
                  child: item.fotoBerita.isNotEmpty
                      ? Image.network(
                          '${AppApiService.baseUrl}API/uploads/berita/${item.fotoBerita}',
                          width: 100,
                          height: 80,
                          fit: BoxFit.cover,
                          errorBuilder: (context, error, stackTrace) => Container(
                            width: 100,
                            height: 80,
                            color: Colors.grey[200],
                            child: const Icon(Icons.image, color: Colors.grey),
                          ),
                        )
                      : Container(
                          width: 100,
                          height: 80,
                          color: Colors.grey[200],
                          child: const Icon(Icons.image, color: Colors.grey),
                        ),
                ),
                const SizedBox(width: 12),
                // Info
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        item.tanggalBerita,
                        style: const TextStyle(fontSize: 12, color: Color(0xFF888888)),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        item.judulBerita,
                        style: const TextStyle(
                            fontSize: 16, fontWeight: FontWeight.bold),
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
          // Baris tombol Edit & Hapus
          Padding(
            padding: const EdgeInsets.fromLTRB(12, 0, 12, 12),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                ElevatedButton(
                  onPressed: () async {
                    final updated = await Navigator.pushNamed(
                      context,
                      DetailBeritaPage.routeName,
                      arguments: item,
                    );
                    if (updated == true) onRefresh();
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: kColorButton,
                    foregroundColor: kColorTextButton,
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(kButtonRadius)),
                    elevation: 0,
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  ),
                  child: const Text('Edit', style: TextStyle(fontSize: 14)),
                ),
                const SizedBox(width: 8),
                ElevatedButton(
                  onPressed: () async {
                    final confirm = await _confirmDelete(context, 'berita ini');
                    if (confirm == true) {
                      await AppApiService.hapusBerita(item.idBerita);
                      onRefresh();
                    }
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: kColorButton,
                    foregroundColor: kColorTextButton,
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(kButtonRadius)),
                    elevation: 0,
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  ),
                  child: const Text('Hapus', style: TextStyle(fontSize: 14)),
                ),
              ],
            ),
          ),
        ],
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
        TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('Batal')),
        TextButton(
          onPressed: () => Navigator.pop(context, true),
          child: const Text('Hapus', style: TextStyle(color: Colors.red)),
        ),
      ],
    ),
  );
}
