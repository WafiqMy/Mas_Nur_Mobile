import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';

class BarangFormPage extends StatefulWidget {
  const BarangFormPage({super.key});
  static const routeName = '/barang-form';

  @override
  State<BarangFormPage> createState() => _BarangFormPageState();
}

class _BarangFormPageState extends State<BarangFormPage> {
  final _namaController = TextEditingController();
  final _hargaController = TextEditingController();
  final _jumlahController = TextEditingController();
  final _deskripsiController = TextEditingController();
  final _spesifikasiController = TextEditingController();
  final _fasilitasController = TextEditingController();
  final _picker = ImagePicker();
  final _jenisOptions = const ['Gedung', 'Alat Multimedia', 'Alat Musik'];
  String _jenis = 'Gedung';
  File? _imageFile;
  bool _saving = false;

  @override
  Widget build(BuildContext context) {
    final barang = ModalRoute.of(context)?.settings.arguments as BarangModel?;
    final isEdit = barang != null;
    if (_namaController.text.isEmpty && barang != null) {
      _namaController.text = barang.namaBarang;
      _hargaController.text = barang.harga.toString();
      _jumlahController.text = barang.jumlah.toString();
      _deskripsiController.text = barang.deskripsi;
      _spesifikasiController.text = barang.spesifikasi;
      _fasilitasController.text = barang.fasilitas;
      _jenis = barang.jenis.isNotEmpty ? barang.jenis : _jenis;
    }
    return Scaffold(
      appBar: AppBar(title: Text(isEdit ? 'Edit Barang' : 'Tambah Barang')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          TextField(controller: _namaController, decoration: const InputDecoration(labelText: 'Nama Barang')),
          const SizedBox(height: 12),
          DropdownButtonFormField<String>(
            initialValue: _jenisOptions.contains(_jenis) ? _jenis : _jenisOptions.first,
            items: _jenisOptions.map((e) => DropdownMenuItem(value: e, child: Text(e))).toList(),
            onChanged: (value) => setState(() => _jenis = value ?? _jenisOptions.first),
            decoration: const InputDecoration(labelText: 'Jenis'),
          ),
          const SizedBox(height: 12),
          TextField(controller: _hargaController, decoration: const InputDecoration(labelText: 'Harga'), keyboardType: TextInputType.number),
          const SizedBox(height: 12),
          TextField(controller: _jumlahController, decoration: const InputDecoration(labelText: 'Jumlah'), keyboardType: TextInputType.number),
          const SizedBox(height: 12),
          TextField(controller: _deskripsiController, decoration: const InputDecoration(labelText: 'Deskripsi'), maxLines: 3),
          const SizedBox(height: 12),
          TextField(controller: _spesifikasiController, decoration: const InputDecoration(labelText: 'Spesifikasi'), maxLines: 3),
          const SizedBox(height: 12),
          TextField(controller: _fasilitasController, decoration: const InputDecoration(labelText: 'Fasilitas'), maxLines: 3),
          const SizedBox(height: 12),
          OutlinedButton.icon(onPressed: _pickImage, icon: const Icon(Icons.image), label: const Text('Pilih Gambar')),
          if (_imageFile != null)
            Padding(
              padding: const EdgeInsets.only(top: 8),
              child: Image.file(_imageFile!, height: 180, fit: BoxFit.cover),
            ),
          const SizedBox(height: 20),
          FilledButton(onPressed: _saving ? null : () => _submit(barang), child: Text(_saving ? 'Menyimpan...' : 'Simpan')),
        ],
      ),
    );
  }

  Future<void> _pickImage() async {
    final file = await _picker.pickImage(source: ImageSource.gallery, imageQuality: 85);
    if (file != null) setState(() => _imageFile = File(file.path));
  }

  Future<void> _submit(BarangModel? barang) async {
    if (_namaController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Nama wajib diisi')));
      return;
    }
    setState(() => _saving = true);
    try {
      final result = barang == null
          ? await AppApiService.tambahBarang(
              namaBarang: _namaController.text.trim(),
              jenis: _jenis,
              harga: _hargaController.text.trim(),
              jumlah: _jumlahController.text.trim(),
              deskripsi: _deskripsiController.text.trim(),
              spesifikasi: _spesifikasiController.text.trim(),
              fasilitas: _fasilitasController.text.trim(),
              gambarFile: _imageFile,
            )
          : await AppApiService.editBarang(
              idPersewaan: barang.idPersewaan,
              namaBarang: _namaController.text.trim(),
              jenis: _jenis,
              harga: _hargaController.text.trim(),
              jumlah: _jumlahController.text.trim(),
              deskripsi: _deskripsiController.text.trim(),
              spesifikasi: _spesifikasiController.text.trim(),
              fasilitas: _fasilitasController.text.trim(),
              gambarFile: _imageFile,
            );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(result.message.isEmpty ? 'Berhasil disimpan' : result.message)));
      Navigator.pop(context, true);
    } catch (_) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Gagal menyimpan barang')));
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }
}

