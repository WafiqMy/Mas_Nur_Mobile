import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';

class AcaraFormPage extends StatefulWidget {
  const AcaraFormPage({super.key});
  static const routeName = '/acara-form';

  @override
  State<AcaraFormPage> createState() => _AcaraFormPageState();
}

class _AcaraFormPageState extends State<AcaraFormPage> {
  final _namaController = TextEditingController();
  final _lokasiController = TextEditingController();
  final _deskripsiController = TextEditingController();
  final _picker = ImagePicker();
  File? _imageFile;
  bool _saving = false;
  String _tanggal = DateFormat('yyyy-MM-dd').format(DateTime.now());

  @override
  Widget build(BuildContext context) {
    final acara = ModalRoute.of(context)?.settings.arguments as AcaraModel?;
    final isEdit = acara != null;
    if (_namaController.text.isEmpty && acara != null) {
      _namaController.text = acara.namaEvent;
      _lokasiController.text = acara.lokasiEvent;
      _deskripsiController.text = acara.deskripsiEvent;
      _tanggal = acara.tanggalEvent.split(' ').first;
    }
    return Scaffold(
      appBar: AppBar(title: Text(isEdit ? 'Edit Acara' : 'Tambah Acara')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          TextField(controller: _namaController, decoration: const InputDecoration(labelText: 'Nama Acara')),
          const SizedBox(height: 12),
          TextField(controller: _lokasiController, decoration: const InputDecoration(labelText: 'Lokasi')),
          const SizedBox(height: 12),
          TextField(controller: _deskripsiController, decoration: const InputDecoration(labelText: 'Deskripsi'), maxLines: 5),
          ListTile(
            contentPadding: EdgeInsets.zero,
            title: const Text('Tanggal'),
            subtitle: Text(_tanggal),
            trailing: IconButton(onPressed: _pickDate, icon: const Icon(Icons.date_range)),
          ),
          OutlinedButton.icon(onPressed: _pickImage, icon: const Icon(Icons.image), label: const Text('Pilih Gambar')),
          if (_imageFile != null)
            Padding(
              padding: const EdgeInsets.only(top: 8),
              child: Image.file(_imageFile!, height: 180, fit: BoxFit.cover),
            ),
          const SizedBox(height: 20),
          FilledButton(onPressed: _saving ? null : () => _submit(acara), child: Text(_saving ? 'Menyimpan...' : 'Simpan')),
        ],
      ),
    );
  }

  Future<void> _pickDate() async {
    final selected = await showDatePicker(
      context: context,
      initialDate: DateTime.tryParse(_tanggal) ?? DateTime.now(),
      firstDate: DateTime(2020),
      lastDate: DateTime(2100),
    );
    if (selected != null) setState(() => _tanggal = DateFormat('yyyy-MM-dd').format(selected));
  }

  Future<void> _pickImage() async {
    final file = await _picker.pickImage(source: ImageSource.gallery, imageQuality: 85);
    if (file != null) setState(() => _imageFile = File(file.path));
  }

  Future<void> _submit(AcaraModel? acara) async {
    if (_namaController.text.trim().isEmpty || _lokasiController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Nama dan lokasi wajib diisi')));
      return;
    }
    setState(() => _saving = true);
    try {
      final result = acara == null
          ? await AppApiService.tambahAcara(
              namaEvent: _namaController.text.trim(),
              tanggalEvent: _tanggal,
              deskripsiEvent: _deskripsiController.text.trim(),
              lokasiEvent: _lokasiController.text.trim(),
              username: 'admin',
              gambarFile: _imageFile,
            )
          : await AppApiService.editAcara(
              idEvent: acara.idEvent,
              namaEvent: _namaController.text.trim(),
              tanggalEvent: _tanggal,
              deskripsiEvent: _deskripsiController.text.trim(),
              lokasiEvent: _lokasiController.text.trim(),
              username: acara.username.isEmpty ? 'admin' : acara.username,
              gambarFile: _imageFile,
            );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(result.message.isEmpty ? 'Berhasil disimpan' : result.message)));
      Navigator.pop(context, true);
    } catch (_) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Gagal menyimpan acara')));
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }
}

