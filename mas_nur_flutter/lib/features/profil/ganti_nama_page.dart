import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';

class GantiNamaPage extends StatefulWidget {
  const GantiNamaPage({super.key});
  static const routeName = '/ganti-nama';

  @override
  State<GantiNamaPage> createState() => _GantiNamaPageState();
}

class _GantiNamaPageState extends State<GantiNamaPage> {
  final _namaController = TextEditingController();
  bool _saving = false;

  Future<void> _submit() async {
    if (_namaController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Nama baru tidak boleh kosong')));
      return;
    }
    setState(() => _saving = true);
    try {
      final username = await AppSession.getUsername();
      final result = await AppApiService.gantiNama(username: username, newName: _namaController.text.trim());
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(result.message)));
      if (result.status.toLowerCase() == 'success') {
        Navigator.pop(context, true);
      }
    } catch (_) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Gagal mengubah nama')));
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Ganti Nama')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            TextField(controller: _namaController, decoration: const InputDecoration(labelText: 'Nama Baru')),
            const SizedBox(height: 16),
            SizedBox(
              width: double.infinity,
              child: FilledButton(onPressed: _saving ? null : _submit, child: Text(_saving ? 'Menyimpan...' : 'Simpan')),
            ),
          ],
        ),
      ),
    );
  }
}

