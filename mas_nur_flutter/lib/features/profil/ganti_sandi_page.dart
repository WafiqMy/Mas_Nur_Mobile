import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/session/app_session.dart';

class GantiSandiPage extends StatefulWidget {
  const GantiSandiPage({super.key});
  static const routeName = '/ganti-sandi';

  @override
  State<GantiSandiPage> createState() => _GantiSandiPageState();
}

class _GantiSandiPageState extends State<GantiSandiPage> {
  final _lamaController = TextEditingController();
  final _baruController = TextEditingController();
  final _verifController = TextEditingController();
  bool _saving = false;

  Future<void> _submit() async {
    if (_lamaController.text.isEmpty || _baruController.text.isEmpty || _verifController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Semua field wajib diisi')));
      return;
    }
    if (_baruController.text.length < 6) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Password minimal 6 karakter')));
      return;
    }
    if (_baruController.text != _verifController.text) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Verifikasi password tidak sama')));
      return;
    }
    setState(() => _saving = true);
    try {
      final username = await AppSession.getUsername();
      final result = await AppApiService.gantiPassword(
        username: username,
        currentPassword: _lamaController.text,
        newPassword: _baruController.text,
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(result.message)));
      if (result.status.toLowerCase() == 'success') {
        Navigator.pop(context, true);
      }
    } catch (_) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Gagal mengubah password')));
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Ganti Password')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          TextField(controller: _lamaController, decoration: const InputDecoration(labelText: 'Password Lama'), obscureText: true),
          const SizedBox(height: 12),
          TextField(controller: _baruController, decoration: const InputDecoration(labelText: 'Password Baru'), obscureText: true),
          const SizedBox(height: 12),
          TextField(controller: _verifController, decoration: const InputDecoration(labelText: 'Verifikasi Password'), obscureText: true),
          const SizedBox(height: 16),
          FilledButton(onPressed: _saving ? null : _submit, child: Text(_saving ? 'Menyimpan...' : 'Simpan')),
        ],
      ),
    );
  }
}

