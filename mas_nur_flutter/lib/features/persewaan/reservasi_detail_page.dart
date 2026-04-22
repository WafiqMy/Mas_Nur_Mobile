import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';

class ReservasiDetailPage extends StatefulWidget {
  const ReservasiDetailPage({super.key});
  static const routeName = '/reservasi-detail';

  @override
  State<ReservasiDetailPage> createState() => _ReservasiDetailPageState();
}

class _ReservasiDetailPageState extends State<ReservasiDetailPage> {
  late Future<ReservasiDetailModel?> _future;
  bool _submitting = false;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final id = ModalRoute.of(context)?.settings.arguments as int?;
    if (id != null) {
      _future = AppApiService.getReservasiDetail(id);
    }
  }

  Future<void> _updateStatus(int id, String status, {String? notes}) async {
    setState(() => _submitting = true);
    try {
      final result = await AppApiService.updateStatusReservasi(
          id: id, status: status, notes: notes);
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(result.message.isEmpty ? 'Berhasil' : result.message)));
      Navigator.pop(context);
    } catch (_) {
      if (mounted) {
        ScaffoldMessenger.of(context)
            .showSnackBar(const SnackBar(content: Text('Gagal memperbarui status')));
      }
    } finally {
      if (mounted) setState(() => _submitting = false);
    }
  }

  Future<void> _showTolakDialog(int id) async {
    final notesCtrl = TextEditingController();
    final confirm = await showDialog<bool>(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text('Alasan Penolakan'),
        content: TextField(
          controller: notesCtrl,
          decoration: const InputDecoration(hintText: 'Masukkan alasan...'),
          maxLines: 3,
        ),
        actions: [
          TextButton(
              onPressed: () => Navigator.pop(context, false),
              child: const Text('Batal')),
          TextButton(
            onPressed: () => Navigator.pop(context, true),
            child: const Text('Tolak', style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
    if (confirm == true && mounted) {
      await _updateStatus(id, 'ditolak', notes: notesCtrl.text.trim());
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF5F5F5),
      appBar: AppBar(
        title: const Text('Detail Permintaan'),
        backgroundColor: kColorHeader,
        foregroundColor: Colors.black,
        elevation: 0,
      ),
      body: FutureBuilder<ReservasiDetailModel?>(
        future: _future,
        builder: (_, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }
          final detail = snapshot.data;
          if (detail == null) {
            return const Center(child: Text('Data tidak ditemukan'));
          }
          final isFinal = detail.statusReservasi == 'diterima' ||
              detail.statusReservasi == 'ditolak';

          return SingleChildScrollView(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Judul
                const Center(
                  child: Text(
                    'Detail Permintaan',
                    style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                  ),
                ),
                const SizedBox(height: 24),

                _SectionLabel('Peminjam'),
                _InfoText(detail.namaPengguna),
                _InfoText(detail.noTlpPengguna, size: 15),
                _InfoText(detail.emailPengguna, size: 15),

                _SectionLabel('Barang yang Dipinjam'),
                _InfoText(detail.namaBarang),
                _InfoText(detail.jenis, size: 15),
                _InfoText('Jumlah: ${detail.totalPeminjaman}', size: 15),
                _InfoText('Harga: Rp ${detail.totalHarga}', size: 15),

                _SectionLabel('Keperluan'),
                _InfoText(detail.keperluan),

                _SectionLabel('Tanggal Reservasi'),
                _InfoText('Mulai: ${detail.tanggalMulaiReservasi}'),
                _InfoText('Selesai: ${detail.tanggalSelesaiReservasi}', size: 15),

                _SectionLabel('Status dan Catatan'),
                _InfoText('Status: ${detail.statusReservasi}',
                    color: const Color(0xFF555555)),
                _InfoText('Catatan: ${detail.notes.isEmpty ? '-' : detail.notes}',
                    size: 15, color: const Color(0xFF888888)),

                const SizedBox(height: 32),

                // Tombol Tolak & Terima
                Row(
                  children: [
                    Expanded(
                      child: SizedBox(
                        height: 45,
                        child: ElevatedButton(
                          onPressed: isFinal || _submitting
                              ? null
                              : () => _showTolakDialog(detail.idReservasi),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: kColorHapus,
                            foregroundColor: kColorTextButton,
                            disabledBackgroundColor: Colors.grey[300],
                            shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(kButtonRadius)),
                            elevation: 0,
                            textStyle: const TextStyle(fontSize: 16),
                          ),
                          child: const Text('Tolak'),
                        ),
                      ),
                    ),
                    const SizedBox(width: 16),
                    Expanded(
                      child: SizedBox(
                        height: 45,
                        child: ElevatedButton(
                          onPressed: isFinal || _submitting
                              ? null
                              : () => _updateStatus(detail.idReservasi, 'diterima'),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: kColorButton,
                            foregroundColor: kColorTextButton,
                            disabledBackgroundColor: Colors.grey[300],
                            shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(kButtonRadius)),
                            elevation: 0,
                            textStyle: const TextStyle(fontSize: 16),
                          ),
                          child: const Text('Terima'),
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 16),
              ],
            ),
          );
        },
      ),
    );
  }
}

class _SectionLabel extends StatelessWidget {
  const _SectionLabel(this.text);
  final String text;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 20, bottom: 4),
      child: Text(
        text,
        style: const TextStyle(
            fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF333333)),
      ),
    );
  }
}

class _InfoText extends StatelessWidget {
  const _InfoText(this.text, {this.size = 16, this.color = const Color(0xFF222222)});
  final String text;
  final double size;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 4),
      child: Text(text, style: TextStyle(fontSize: size, color: color)),
    );
  }
}
