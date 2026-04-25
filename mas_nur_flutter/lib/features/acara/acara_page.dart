import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/acara/acara_form_page.dart';
import 'package:mas_nur_flutter/features/acara/detail_acara_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_drawer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_header.dart';

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
      backgroundColor: kColorWhite,
      drawer: const AppDrawer(),
      body: Column(
        children: [
          const AppHeader(),
          Expanded(
            child: FutureBuilder<List<AcaraModel>>(
              future: _future,
              builder: (_, snapshot) {
                if (snapshot.connectionState != ConnectionState.done) {
                  return const Center(child: CircularProgressIndicator());
                }
                if (snapshot.hasError) {
                  return const Center(child: Text('Gagal memuat acara'));
                }
                final items = snapshot.data ?? <AcaraModel>[];
                return Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                      child: Row(
                        children: [
                          const Expanded(
                            child: Text(
                              'Kelola Acara',
                              style: TextStyle(
                                  fontSize: 20, fontWeight: FontWeight.bold),
                            ),
                          ),
                          ElevatedButton.icon(
                            onPressed: () async {
                              final updated = await Navigator.pushNamed(
                                  context, AcaraFormPage.routeName);
                              if (updated == true && mounted) {
                                setState(() => _future = AppApiService.getAcara());
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
                          child: Center(child: Text('Belum ada acara')))
                    else
                      Expanded(
                        child: ListView.builder(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 16, vertical: 4),
                          itemCount: items.length,
                          itemBuilder: (_, index) =>
                              _AcaraCard(item: items[index], onRefresh: () {
                            setState(() => _future = AppApiService.getAcara());
                          }),
                        ),
                      ),
                  ],
                );
              },
            ),
          ),
          const AppFooter(currentIndex: 1),
        ],
      ),
    );
  }
}

class _AcaraCard extends StatelessWidget {
  const _AcaraCard({required this.item, required this.onRefresh});
  final AcaraModel item;
  final VoidCallback onRefresh;

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 4,
      margin: const EdgeInsets.only(bottom: 12),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(kCardRadius)),
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                ClipRRect(
                  borderRadius: BorderRadius.circular(4),
                  child: item.gambarEvent.isNotEmpty
                      ? Image.network(
                          '${AppApiService.baseUrl}API/uploads/kegiatan/${item.gambarEvent}',
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
                          child: const Icon(Icons.event, color: Colors.grey),
                        ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        item.tanggalEvent,
                        style: const TextStyle(fontSize: 12, color: Color(0xFF888888)),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        item.namaEvent,
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
          Padding(
            padding: const EdgeInsets.fromLTRB(130, 0, 12, 12),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                SizedBox(
                  width: 105,
                  child: ElevatedButton(
                    onPressed: () async {
                      final updated = await Navigator.pushNamed(
                        context,
                        DetailAcaraPage.routeName,
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
                    ),
                    child: const Text('Edit'),
                  ),
                ),
                const SizedBox(width: 8),
                SizedBox(
                  width: 130,
                  height: 45,
                  child: ElevatedButton(
                    onPressed: () async {
                      final confirm = await _confirmDeleteAcara(context);
                      if (confirm == true) {
                        await AppApiService.hapusAcara(item.idEvent);
                        onRefresh();
                      }
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: kColorHapus,
                      foregroundColor: kColorTextButton,
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(kButtonRadius)),
                      elevation: 0,
                      textStyle: const TextStyle(fontSize: 16),
                    ),
                    child: const Text('Hapus'),
                  ),
                ),
              ],
            ),
          ),
        ],
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
