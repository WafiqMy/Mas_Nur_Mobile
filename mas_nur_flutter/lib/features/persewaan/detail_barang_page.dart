import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/persewaan/barang_form_page.dart';

class DetailBarangPage extends StatelessWidget {
  const DetailBarangPage({super.key});
  static const routeName = '/detail-barang';

  @override
  Widget build(BuildContext context) {
    final barang = ModalRoute.of(context)?.settings.arguments as BarangModel?;

    if (barang == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Detail Barang')),
        body: const Center(child: Text('Data barang tidak ditemukan')),
      );
    }

    final hargaFormatted = NumberFormat.currency(
      locale: 'id_ID',
      symbol: 'Rp ',
      decimalDigits: 0,
    ).format(barang.harga);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Detail Barang'),
        actions: [
          IconButton(
            icon: const Icon(Icons.edit),
            tooltip: 'Edit Barang',
            onPressed: () async {
              final updated = await Navigator.pushNamed(
                context,
                BarangFormPage.routeName,
                arguments: barang,
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
          // Gambar barang
          ClipRRect(
            borderRadius: BorderRadius.circular(12),
            child: Image.network(
              '${AppApiService.baseUrl}API/get_gambar.php?file=${barang.gambar}',
              height: 220,
              width: double.infinity,
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) => Container(
                height: 220,
                color: Colors.grey[200],
                child: const Center(child: Icon(Icons.inventory_2, size: 64, color: Colors.grey)),
              ),
            ),
          ),
          const SizedBox(height: 16),

          // Nama barang
          Text(
            barang.namaBarang,
            style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),

          // Info utama
          _InfoCard(children: [
            _InfoRow(label: 'Jenis', value: barang.jenis),
            _InfoRow(label: 'Harga', value: hargaFormatted),
            _InfoRow(label: 'Stok', value: '${barang.jumlah} unit'),
          ]),
          const SizedBox(height: 16),

          // Deskripsi
          if (barang.deskripsi.isNotEmpty) ...[
            _SectionTitle('Deskripsi'),
            const SizedBox(height: 6),
            Text(barang.deskripsi, style: Theme.of(context).textTheme.bodyMedium?.copyWith(height: 1.5)),
            const SizedBox(height: 16),
          ],

          // Spesifikasi
          if (barang.spesifikasi.isNotEmpty) ...[
            _SectionTitle('Spesifikasi'),
            const SizedBox(height: 6),
            Text(barang.spesifikasi, style: Theme.of(context).textTheme.bodyMedium?.copyWith(height: 1.5)),
            const SizedBox(height: 16),
          ],

          // Fasilitas
          if (barang.fasilitas.isNotEmpty) ...[
            _SectionTitle('Fasilitas'),
            const SizedBox(height: 6),
            Text(barang.fasilitas, style: Theme.of(context).textTheme.bodyMedium?.copyWith(height: 1.5)),
            const SizedBox(height: 16),
          ],

          // Tombol edit
          FilledButton.icon(
            onPressed: () async {
              final updated = await Navigator.pushNamed(
                context,
                BarangFormPage.routeName,
                arguments: barang,
              );
              if (updated == true && context.mounted) {
                Navigator.pop(context, true);
              }
            },
            icon: const Icon(Icons.edit),
            label: const Text('Edit Barang'),
          ),
          const SizedBox(height: 24),
        ],
      ),
    );
  }
}

class _SectionTitle extends StatelessWidget {
  const _SectionTitle(this.title);
  final String title;

  @override
  Widget build(BuildContext context) {
    return Text(
      title,
      style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold),
    );
  }
}

class _InfoCard extends StatelessWidget {
  const _InfoCard({required this.children});
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: children,
        ),
      ),
    );
  }
}

class _InfoRow extends StatelessWidget {
  const _InfoRow({required this.label, required this.value});
  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 90,
            child: Text(
              label,
              style: const TextStyle(fontWeight: FontWeight.w600, color: Colors.grey),
            ),
          ),
          const Text(': '),
          Expanded(child: Text(value.isNotEmpty ? value : '-')),
        ],
      ),
    );
  }
}

