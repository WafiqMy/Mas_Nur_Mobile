import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';

class BeritaFormPage extends StatefulWidget {
  const BeritaFormPage({super.key});
  static const routeName = '/berita-form';

  @override
  State<BeritaFormPage> createState() => _BeritaFormPageState();
}

class _BeritaFormPageState extends State<BeritaFormPage> {
  final _judulController = TextEditingController();
  final _isiController = TextEditingController();
  final _picker = ImagePicker();
  File? _imageFile;
  bool _saving = false;
  String _tanggal = DateFormat('yyyy-MM-dd').format(DateTime.now());

  @override
  Widget build(BuildContext context) {
    final berita = ModalRoute.of(context)?.settings.arguments as BeritaModel?;
    final isEdit = berita != null;
    if (_judulController.text.isEmpty && berita != null) {
      _judulController.text = berita.judulBerita;
      _isiController.text = berita.isiBerita;
      _tanggal = berita.tanggalBerita.split(' ').first;
    }

    return Scaffold(
      appBar: AppBar(title: Text(isEdit ? 'Edit Berita' : 'Tambah Berita')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          TextField(controller: _judulController, decoration: const InputDecoration(labelText: 'Judul Berita')),
          const SizedBox(height: 12),
          TextField(
            controller: _isiController,
            decoration: const InputDecoration(labelText: 'Isi Berita'),
            maxLines: 7,
          ),
          const SizedBox(height: 12),
          ListTile(
            contentPadding: EdgeInsets.zero,
            title: const Text('Tanggal'),
            subtitle: Text(_tanggal),
            trailing: IconButton(
              icon: const Icon(Icons.date_range),
              onPressed: _pickDate,
            ),
          ),
          const SizedBox(height: 8),
          OutlinedButton.icon(
            onPressed: _pickImage,
            icon: const Icon(Icons.image),
            label: const Text('Pilih Gambar'),
          ),
          if (_imageFile != null) ...[
            const SizedBox(height: 8),
            ClipRRect(
              borderRadius: BorderRadius.circular(8),
              child: Image.file(_imageFile!, height: 180, fit: BoxFit.cover),
            ),
          ],
          const SizedBox(height: 20),
          FilledButton(
            onPressed: _saving ? null : () => _submit(berita),
            child: Text(_saving ? 'Menyimpan...' : 'Simpan'),
          ),
        ],
      ),
    );
  }

  Future<void> _pickDate() async {
    final now = DateTime.now();
    final selected = await showDatePicker(
      context: context,
      initialDate: DateTime.tryParse(_tanggal) ?? now,
      firstDate: DateTime(2020),
      lastDate: DateTime(2100),
    );
    if (selected != null) {
      setState(() => _tanggal = DateFormat('yyyy-MM-dd').format(selected));
    }
  }

  Future<void> _pickImage() async {
    final file = await _picker.pickImage(source: ImageSource.gallery, imageQuality: 85);
    if (file != null) setState(() => _imageFile = File(file.path));
  }

  Future<void> _submit(BeritaModel? berita) async {
    if (_judulController.text.trim().isEmpty || _isiController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Judul dan isi wajib diisi')));
      return;
    }
    setState(() => _saving = true);
    try {
      final result = berita == null
          ? await AppApiService.tambahBerita(
              judul: _judulController.text.trim(),
              isi: _isiController.text.trim(),
              tanggal: _tanggal,
              username: 'Admin',
              fotoFile: _imageFile,
            )
          : await AppApiService.editBerita(
              idBerita: berita.idBerita,
              judul: _judulController.text.trim(),
              isi: _isiController.text.trim(),
              tanggal: _tanggal,
              username: berita.username.isEmpty ? 'Admin' : berita.username,
              fotoFile: _imageFile,
            );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(result.message.isEmpty ? 'Berhasil disimpan' : result.message)));
      Navigator.pop(context, true);
    } catch (_) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Gagal menyimpan berita')));
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }
}

