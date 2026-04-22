import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/berita/berita_form_page.dart';

class DetailBeritaPage extends StatelessWidget {
  const DetailBeritaPage({super.key});
  static const routeName = '/detail-berita';

  @override
  Widget build(BuildContext context) {
    final berita = ModalRoute.of(context)?.settings.arguments as BeritaModel?;

    if (berita == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Detail Berita')),
        body: const Center(child: Text('Data berita tidak ditemukan')),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text('Detail Berita'),
        actions: [
          IconButton(
            icon: const Icon(Icons.edit),
            tooltip: 'Edit Berita',
            onPressed: () async {
              final updated = await Navigator.pushNamed(
                context,
                BeritaFormPage.routeName,
                arguments: berita,
              );
              if (updated == true && context.mounted) {
                Navigator.pop(context, true);
              }
            },
          ),
        ],
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          // Gambar berita
          if (berita.fotoBerita.isNotEmpty)
            ClipRRect(
              borderRadius: BorderRadius.circular(12),
              child: Image.network(
                '${AppApiService.baseUrl}API/uploads/berita/${berita.fotoBerita}',
                height: 220,
                width: double.infinity,
                fit: BoxFit.cover,
                errorBuilder: (context, error, stackTrace) => Container(
                  height: 220,
                  color: Colors.grey[200],
                  child: const Center(child: Icon(Icons.article, size: 64, color: Colors.grey)),
                ),
              ),
            )
          else
            Container(
              height: 220,
              decoration: BoxDecoration(
                color: Colors.grey[200],
                borderRadius: BorderRadius.circular(12),
              ),
              child: const Center(child: Icon(Icons.article, size: 64, color: Colors.grey)),
            ),
          const SizedBox(height: 16),

          // Judul
          Text(
            berita.judulBerita,
            style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 6),

          // Tanggal
          Row(
            children: [
              const Icon(Icons.calendar_today, size: 14, color: Colors.grey),
              const SizedBox(width: 4),
              Text(
                berita.tanggalBerita,
                style: Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.grey),
              ),
              if (berita.username.isNotEmpty) ...[
                const SizedBox(width: 12),
                const Icon(Icons.person, size: 14, color: Colors.grey),
                const SizedBox(width: 4),
                Text(
                  berita.username,
                  style: Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.grey),
                ),
              ],
            ],
          ),
          const Divider(height: 24),

          // Isi berita
          Text(
            berita.isiBerita,
            style: Theme.of(context).textTheme.bodyMedium?.copyWith(height: 1.6),
          ),
          const SizedBox(height: 24),
        ],
      ),
    );
  }
}

