import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/features/food_court/food_court_form_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_header.dart';

class FoodCourtPage extends StatefulWidget {
  const FoodCourtPage({super.key});
  static const routeName = '/food-court';

  @override
  State<FoodCourtPage> createState() => _FoodCourtPageState();
}

class _FoodCourtPageState extends State<FoodCourtPage> {
  late Future<List<FoodCourtModel>> _future;

  @override
  void initState() {
    super.initState();
    _future = AppApiService.getFoodCourt();
  }

  void _refresh() => setState(() => _future = AppApiService.getFoodCourt());

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
        body: Column(
          children: [
            const AppHeader(showBackButton: true),
            Expanded(
              child: FutureBuilder<List<FoodCourtModel>>(
                future: _future,
                builder: (_, snapshot) {
                  if (snapshot.connectionState != ConnectionState.done) {
                    return const Center(child: CircularProgressIndicator());
                  }
                  if (snapshot.hasError) {
                    return Center(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          const Text('Gagal memuat data food court'),
                          const SizedBox(height: 12),
                          OutlinedButton(
                            onPressed: _refresh,
                            child: const Text('Coba Lagi'),
                          ),
                        ],
                      ),
                    );
                  }
                  final items = snapshot.data ?? <FoodCourtModel>[];
                  return Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                        child: Row(
                          children: [
                            const Expanded(
                              child: Text(
                                'Food Court',
                                style: TextStyle(
                                    fontSize: 20,
                                    fontWeight: FontWeight.bold),
                              ),
                            ),
                            ElevatedButton.icon(
                              onPressed: () async {
                                final updated = await Navigator.pushNamed(
                                    context, FoodCourtFormPage.routeName);
                                if (updated == true && mounted) _refresh();
                              },
                              icon: const Icon(Icons.add, size: 18),
                              label: const Text('Tambah'),
                              style: ElevatedButton.styleFrom(
                                backgroundColor: kColorButton,
                                foregroundColor: kColorTextButton,
                                shape: RoundedRectangleBorder(
                                    borderRadius:
                                        BorderRadius.circular(kButtonRadius)),
                                elevation: 0,
                              ),
                            ),
                          ],
                        ),
                      ),
                      if (items.isEmpty)
                        const Expanded(
                          child: Center(
                            child: Column(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Icon(Icons.restaurant_menu,
                                    size: 64, color: Color(0xFFBDBDBD)),
                                SizedBox(height: 16),
                                Text(
                                  'Belum ada menu food court',
                                  style: TextStyle(
                                      fontSize: 16, color: Color(0xFF9E9E9E)),
                                ),
                              ],
                            ),
                          ),
                        )
                      else
                        Expanded(
                          child: ListView.builder(
                            padding: const EdgeInsets.symmetric(
                                horizontal: 16, vertical: 4),
                            itemCount: items.length,
                            itemBuilder: (_, index) => _FoodCourtCard(
                              item: items[index],
                              onRefresh: _refresh,
                            ),
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

class _FoodCourtCard extends StatelessWidget {
  const _FoodCourtCard({required this.item, required this.onRefresh});
  final FoodCourtModel item;
  final VoidCallback onRefresh;

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 4,
      margin: const EdgeInsets.only(bottom: 12),
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(kCardRadius)),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Foto
          ClipRRect(
            borderRadius: const BorderRadius.vertical(top: Radius.circular(kCardRadius)),
            child: item.foto.isNotEmpty
                ? Image.network(
                    '${AppApiService.baseUrl}API/uploads/food_court/${item.foto}',
                    width: double.infinity,
                    height: 180,
                    fit: BoxFit.cover,
                    errorBuilder: (_, __, ___) => _placeholderImage(),
                  )
                : _placeholderImage(),
          ),

          // Info
          Padding(
            padding: const EdgeInsets.fromLTRB(12, 10, 12, 4),
            child: Text(
              item.namaMenu,
              style: const TextStyle(
                  fontSize: 16, fontWeight: FontWeight.bold),
            ),
          ),
          if (item.deskripsi.isNotEmpty)
            Padding(
              padding: const EdgeInsets.fromLTRB(12, 0, 12, 8),
              child: Text(
                item.deskripsi,
                style: const TextStyle(
                    fontSize: 13, color: Color(0xFF666666)),
                maxLines: 3,
                overflow: TextOverflow.ellipsis,
              ),
            ),

          // Tombol
          Padding(
            padding: const EdgeInsets.fromLTRB(12, 0, 12, 12),
            child: Row(
              children: [
                Expanded(
                  child: ElevatedButton(
                    onPressed: () async {
                      final updated = await Navigator.pushNamed(
                        context,
                        FoodCourtFormPage.routeName,
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
                Expanded(
                  child: ElevatedButton(
                    onPressed: () async {
                      final confirm = await _confirmHapus(context);
                      if (confirm == true) {
                        await AppApiService.hapusFoodCourt(item.idFoodCourt);
                        onRefresh();
                      }
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: kColorHapus,
                      foregroundColor: kColorTextButton,
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(kButtonRadius)),
                      elevation: 0,
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

  Widget _placeholderImage() {
    return Container(
      width: double.infinity,
      height: 180,
      color: const Color(0xFFEEEEEE),
      child: const Center(
        child: Icon(Icons.restaurant_menu, size: 56, color: Colors.grey),
      ),
    );
  }
}

Future<bool?> _confirmHapus(BuildContext context) {
  return showDialog<bool>(
    context: context,
    builder: (_) => AlertDialog(
      title: const Text('Konfirmasi Hapus'),
      content: const Text('Yakin ingin menghapus menu ini?'),
      actions: [
        TextButton(
          onPressed: () => Navigator.pop(context, false),
          child: const Text('Batal'),
        ),
        TextButton(
          onPressed: () => Navigator.pop(context, true),
          child: const Text('Hapus', style: TextStyle(color: Colors.red)),
        ),
      ],
    ),
  );
}
