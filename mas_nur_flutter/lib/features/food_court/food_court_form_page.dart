import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';

class FoodCourtFormPage extends StatefulWidget {
  const FoodCourtFormPage({super.key});
  static const routeName = '/food-court-form';

  @override
  State<FoodCourtFormPage> createState() => _FoodCourtFormPageState();
}

class _FoodCourtFormPageState extends State<FoodCourtFormPage> {
  final _namaController = TextEditingController();
  final _deskripsiController = TextEditingController();
  final _picker = ImagePicker();
  File? _imageFile;
  bool _saving = false;
  bool _initialized = false;

  @override
  void dispose() {
    _namaController.dispose();
    _deskripsiController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final item =
        ModalRoute.of(context)?.settings.arguments as FoodCourtModel?;
    final isEdit = item != null;

    // Isi form saat edit — hanya sekali
    if (!_initialized && item != null) {
      _namaController.text = item.namaMenu;
      _deskripsiController.text = item.deskripsi;
      _initialized = true;
    }

    return Scaffold(
      appBar: AppBar(
        title: Text(isEdit ? 'Edit Menu' : 'Tambah Menu'),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          // Foto
          GestureDetector(
            onTap: _pickImage,
            child: Container(
              height: 200,
              decoration: BoxDecoration(
                color: const Color(0xFFF5F5F5),
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: const Color(0xFFDDDDDD)),
              ),
              child: _imageFile != null
                  ? ClipRRect(
                      borderRadius: BorderRadius.circular(12),
                      child: Image.file(_imageFile!,
                          fit: BoxFit.cover, width: double.infinity),
                    )
                  : (isEdit && item.foto.isNotEmpty)
                      ? ClipRRect(
                          borderRadius: BorderRadius.circular(12),
                          child: Image.network(
                            '${AppApiService.baseUrl}API/uploads/food_court/${item.foto}',
                            fit: BoxFit.cover,
                            width: double.infinity,
                            errorBuilder: (_, __, ___) =>
                                _photoPlaceholder(),
                          ),
                        )
                      : _photoPlaceholder(),
            ),
          ),
          const SizedBox(height: 8),
          Center(
            child: TextButton.icon(
              onPressed: _pickImage,
              icon: const Icon(Icons.photo_library_outlined),
              label: const Text('Pilih Foto'),
            ),
          ),
          const SizedBox(height: 16),

          // Nama menu
          TextField(
            controller: _namaController,
            decoration: InputDecoration(
              labelText: 'Nama Menu',
              border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(8)),
              filled: true,
              fillColor: const Color(0xFFF5F5F5),
            ),
            textCapitalization: TextCapitalization.words,
          ),
          const SizedBox(height: 16),

          // Deskripsi
          TextField(
            controller: _deskripsiController,
            decoration: InputDecoration(
              labelText: 'Deskripsi',
              alignLabelWithHint: true,
              border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(8)),
              filled: true,
              fillColor: const Color(0xFFF5F5F5),
            ),
            maxLines: 5,
            textCapitalization: TextCapitalization.sentences,
          ),
          const SizedBox(height: 24),

          // Tombol simpan
          SizedBox(
            width: double.infinity,
            height: 50,
            child: FilledButton(
              onPressed: _saving ? null : () => _submit(item),
              child: Text(_saving ? 'Menyimpan...' : 'Simpan'),
            ),
          ),
        ],
      ),
    );
  }

  Widget _photoPlaceholder() {
    return const Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(Icons.add_photo_alternate_outlined,
            size: 48, color: Colors.grey),
        SizedBox(height: 8),
        Text('Ketuk untuk pilih foto',
            style: TextStyle(color: Colors.grey, fontSize: 13)),
      ],
    );
  }

  Future<void> _pickImage() async {
    final file = await _picker.pickImage(
        source: ImageSource.gallery, imageQuality: 85);
    if (file != null) setState(() => _imageFile = File(file.path));
  }

  Future<void> _submit(FoodCourtModel? item) async {
    final nama = _namaController.text.trim();
    final deskripsi = _deskripsiController.text.trim();

    if (nama.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Nama menu wajib diisi')),
      );
      return;
    }

    setState(() => _saving = true);
    try {
      final result = item == null
          ? await AppApiService.tambahFoodCourt(
              namaMenu: nama,
              deskripsi: deskripsi,
              fotoFile: _imageFile,
            )
          : await AppApiService.editFoodCourt(
              idFoodCourt: item.idFoodCourt,
              namaMenu: nama,
              deskripsi: deskripsi,
              fotoFile: _imageFile,
            );

      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(result.message.isNotEmpty
              ? result.message
              : 'Berhasil disimpan'),
        ),
      );
      Navigator.pop(context, true);
    } catch (_) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Gagal menyimpan menu')),
      );
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }
}
