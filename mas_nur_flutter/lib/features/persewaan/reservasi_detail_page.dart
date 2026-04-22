import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';

class ReservasiDetailPage extends StatefulWidget {
  const ReservasiDetailPage({super.key});
  static const routeName = '/reservasi-detail';

  @override
  State<ReservasiDetailPage> createState() => _ReservasiDetailPageState();
}

class _ReservasiDetailPageState extends State<ReservasiDetailPage> {
  bool _loading = false;

  @override
  Widget build(BuildContext context) {
    final id = (ModalRoute.of(context)?.settings.arguments as int?) ?? 0;
    return Scaffold(
      appBar: AppBar(title: const Text('Detail Reservasi')),
      body: FutureBuilder<ReservasiDetailModel?>(
        future: AppApiService.getReservasiDetail(id),
        builder: (context, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }
          final data = snapshot.data;
          if (data == null) return const Center(child: Text('Data tidak ditemukan'));
          final isFinal = data.statusReservasi == 'Disetujui' || data.statusReservasi == 'Ditolak';
          return ListView(
            padding: const EdgeInsets.all(16),
            children: [
              _item('Nama Peminjam', data.namaPengguna),
              _item('Telepon', data.noTlpPengguna),
              _item('Email', data.emailPengguna),
              _item('Barang', data.namaBarang),
              _item('Jenis', data.jenis),
              _item('Jumlah', '${data.totalPeminjaman}'),
              _item('Harga', NumberFormat.currency(locale: 'id_ID', symbol: 'Rp ').format(data.totalHarga)),
              _item('Keperluan', data.keperluan),
              _item('Tanggal Mulai', data.tanggalMulaiReservasi),
              _item('Tanggal Selesai', data.tanggalSelesaiReservasi),
              _item('Status', data.statusReservasi),
              _item('Catatan', data.notes.isEmpty ? '-' : data.notes),
              const SizedBox(height: 16),
              Row(
                children: [
                  Expanded(
                    child: FilledButton(
                      onPressed: isFinal || _loading ? null : () => _updateStatus(id, 'Disetujui'),
                      child: const Text('Terima'),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: FilledButton.tonal(
                      onPressed: isFinal || _loading ? null : () => _showRejectDialog(id),
                      child: const Text('Tolak'),
                    ),
                  ),
                ],
              ),
            ],
          );
        },
      ),
    );
  }

  Widget _item(String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Text('$label: $value'),
    );
  }

  Future<void> _showRejectDialog(int id) async {
    final controller = TextEditingController();
    final ok = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Alasan Penolakan'),
        content: TextField(controller: controller, maxLines: 3, decoration: const InputDecoration(hintText: 'Masukkan alasan...')),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Batal')),
          FilledButton(onPressed: () => Navigator.pop(context, true), child: const Text('Kirim')),
        ],
      ),
    );
    if (ok == true) {
      await _updateStatus(id, 'Ditolak', notes: controller.text.trim());
    }
  }

  Future<void> _updateStatus(int id, String status, {String? notes}) async {
    setState(() => _loading = true);
    try {
      final result = await AppApiService.updateStatusReservasi(id: id, status: status, notes: notes);
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(result.message)));
      if (result.status.toLowerCase() == 'success') {
        Navigator.pop(context, true);
      }
    } catch (_) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Gagal update status')));
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }
}

