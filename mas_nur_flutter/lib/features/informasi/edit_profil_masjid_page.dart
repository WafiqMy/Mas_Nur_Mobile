import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';

class EditProfilMasjidPage extends StatefulWidget {
  const EditProfilMasjidPage({super.key});
  static const routeName = '/edit-profil-masjid';

  @override
  State<EditProfilMasjidPage> createState() => _EditProfilMasjidPageState();
}

class _EditProfilMasjidPageState extends State<EditProfilMasjidPage> {
  static const int _maxChar = 13000;

  final _judulController = TextEditingController();
  final _deskripsiController = TextEditingController();
  final _picker = ImagePicker();

  File? _imageFile;
  bool _saving = false;
  int _charCount = 0;

  @override
  void initState() {
    super.initState();
    _deskripsiController.addListener(() {
      setState(() => _charCount = _deskripsiController.text.length);
    });
  }

  @override
  void dispose() {
    _judulController.dispose();
    _deskripsiController.dispose();
    super.dispose();
  }

  Color get _counterColor {
    if (_charCount > _maxChar) return Colors.red;
    if (_charCount > _maxChar * 0.9) return Colors.orange;
    return Colors.grey;
  }

  @override
  Widget build(BuildContext context) {
    final profil = ModalRoute.of(context)?.settings.arguments as ProfilMasjidModel?;

    // Pre-fill sekali saat pertama kali
    if (_judulController.text.isEmpty && profil != null) {
      _judulController.text = profil.judulSejarah;
      _deskripsiController.text = profil.deskripsiSejarah;
      _charCount = profil.deskripsiSejarah.length;
    }

    return Scaffold(
      appBar: AppBar(title: const Text('Edit Profil Masjid')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          // Preview gambar
          GestureDetector(
            onTap: _pickImage,
            child: Container(
              height: 200,
              decoration: BoxDecoration(
                color: Colors.grey[200],
                borderRadius: BorderRadius.circular(8),
                border: Border.all(color: Colors.grey[400]!),
              ),
              child: _imageFile != null
                  ? ClipRRect(
                      borderRadius: BorderRadius.circular(8),
                      child: Image.file(_imageFile!, fit: BoxFit.cover, width: double.infinity),
                    )
                  : profil != null && profil.gambarSejarahMasjid.isNotEmpty
                      ? ClipRRect(
                          borderRadius: BorderRadius.circular(8),
                          child: Image.network(
                            '${AppApiService.baseUrl}API/uploads/profil_masjid/${profil.gambarSejarahMasjid}',
                            fit: BoxFit.cover,
                            width: double.infinity,
                            errorBuilder: (context, error, stackTrace) => const Center(
                              child: Icon(Icons.image, size: 60, color: Colors.grey),
                            ),
                          ),
                        )
                      : const Center(
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Icon(Icons.add_photo_alternate, size: 48, color: Colors.grey),
                              SizedBox(height: 8),
                              Text('Tap untuk pilih gambar', style: TextStyle(color: Colors.grey)),
                            ],
                          ),
                        ),
            ),
          ),
          const SizedBox(height: 8),
          OutlinedButton.icon(
            onPressed: _pickImage,
            icon: const Icon(Icons.image),
            label: const Text('Pilih Gambar'),
          ),
          const SizedBox(height: 16),

          // Judul
          TextField(
            controller: _judulController,
            decoration: const InputDecoration(
              labelText: 'Judul Sejarah',
              border: OutlineInputBorder(),
            ),
          ),
          const SizedBox(height: 16),

          // Deskripsi + counter
          TextField(
            controller: _deskripsiController,
            decoration: InputDecoration(
              labelText: 'Deskripsi Sejarah',
              border: const OutlineInputBorder(),
              helperText: '$_charCount/$_maxChar',
              helperStyle: TextStyle(color: _counterColor),
              counterText: '',
            ),
            maxLines: 10,
            maxLength: _maxChar + 100, // biarkan ketik lebih, validasi saat simpan
          ),
          const SizedBox(height: 24),

          FilledButton(
            onPressed: _saving ? null : () => _submit(profil),
            child: Text(_saving ? 'Menyimpan...' : 'Simpan'),
          ),
          const SizedBox(height: 8),
          OutlinedButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Batal'),
          ),
        ],
      ),
    );
  }

  Future<void> _pickImage() async {
    final file = await _picker.pickImage(source: ImageSource.gallery, imageQuality: 80);
    if (file != null) setState(() => _imageFile = File(file.path));
  }

  Future<void> _submit(ProfilMasjidModel? profil) async {
    final judul = _judulController.text.trim();
    final deskripsi = _deskripsiController.text.trim();

    if (judul.isEmpty) {
      _showSnack('Judul wajib diisi');
      return;
    }
    if (deskripsi.isEmpty) {
      _showSnack('Deskripsi wajib diisi');
      return;
    }
    if (deskripsi.length > _maxChar) {
      _showSnack('Deskripsi maksimal $_maxChar karakter');
      return;
    }

    setState(() => _saving = true);
    try {
      final result = await AppApiService.updateProfilMasjid(
        judul: judul,
        deskripsi: deskripsi,
        gambarFile: _imageFile,
      );
      if (!mounted) return;
      _showSnack(result.message.isEmpty ? 'Berhasil disimpan' : result.message);
      Navigator.pop(context, true);
    } catch (e) {
      if (!mounted) return;
      _showSnack('Gagal menyimpan: $e');
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  void _showSnack(String msg) {
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(msg)));
  }
}

