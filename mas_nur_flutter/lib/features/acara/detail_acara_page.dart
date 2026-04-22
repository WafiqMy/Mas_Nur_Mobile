import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/acara/acara_form_page.dart';

class DetailAcaraPage extends StatelessWidget {
  const DetailAcaraPage({super.key});
  static const routeName = '/detail-acara';

  @override
  Widget build(BuildContext context) {
    final acara = ModalRoute.of(context)?.settings.arguments as AcaraModel?;

    if (acara == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Detail Acara')),
        body: const Center(child: Text('Data acara tidak ditemukan')),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text('Detail Acara'),
        actions: [
          IconButton(
            icon: const Icon(Icons.edit),
            tooltip: 'Edit Acara',
            onPressed: () async {
              final updated = await Navigator.pushNamed(
                context,
                AcaraFormPage.routeName,
                arguments: acara,
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
          // Gambar acara
          if (acara.gambarEvent.isNotEmpty)
            ClipRRect(
              borderRadius: BorderRadius.circular(12),
              child: Image.network(
                '${AppApiService.baseUrl}API/uploads/kegiatan/${acara.gambarEvent}',
                height: 220,
                width: double.infinity,
                fit: BoxFit.cover,
                errorBuilder: (context, error, stackTrace) => Container(
                  height: 220,
                  color: Colors.grey[200],
                  child: const Center(child: Icon(Icons.event, size: 64, color: Colors.grey)),
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
              child: const Center(child: Icon(Icons.event, size: 64, color: Colors.grey)),
            ),
          const SizedBox(height: 16),

          // Nama acara
          Text(
            acara.namaEvent,
            style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),

          // Info tanggal & lokasi
          _InfoRow(icon: Icons.calendar_today, label: 'Tanggal', value: acara.tanggalEvent),
          const SizedBox(height: 8),
          _InfoRow(icon: Icons.location_on, label: 'Lokasi', value: acara.lokasiEvent),
          const Divider(height: 24),

          // Deskripsi
          Text(
            'Deskripsi',
            style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 8),
          Text(
            acara.deskripsiEvent.isNotEmpty ? acara.deskripsiEvent : '-',
            style: Theme.of(context).textTheme.bodyMedium?.copyWith(height: 1.6),
          ),
          const SizedBox(height: 24),
        ],
      ),
    );
  }
}

class _InfoRow extends StatelessWidget {
  const _InfoRow({required this.icon, required this.label, required this.value});
  final IconData icon;
  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Icon(icon, size: 18, color: Theme.of(context).colorScheme.primary),
        const SizedBox(width: 8),
        Expanded(
          child: RichText(
            text: TextSpan(
              style: Theme.of(context).textTheme.bodyMedium,
              children: [
                TextSpan(text: '$label: ', style: const TextStyle(fontWeight: FontWeight.w600)),
                TextSpan(text: value.isNotEmpty ? value : '-'),
              ],
            ),
          ),
        ),
      ],
    );
  }
}

