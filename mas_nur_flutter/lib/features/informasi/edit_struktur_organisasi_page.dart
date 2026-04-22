import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';

class EditStrukturOrganisasiPage extends StatefulWidget {
  const EditStrukturOrganisasiPage({super.key});
  static const routeName = '/edit-struktur-organisasi';

  @override
  State<EditStrukturOrganisasiPage> createState() => _EditStrukturOrganisasiPageState();
}

class _EditStrukturOrganisasiPageState extends State<EditStrukturOrganisasiPage> {
  final _picker = ImagePicker();
  File? _filePengurus;
  File? _fileRemas;
  bool _saving = false;

  @override
  Widget build(BuildContext context) {
    final struktur = ModalRoute.of(context)?.settings.arguments as StrukturOrganisasiModel?;

    return Scaffold(
      appBar: AppBar(title: const Text('Edit Struktur Organisasi')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          // ——— Gambar Pengurus ———
          Text('Gambar Struktur Pengurus', style: Theme.of(context).textTheme.titleMedium),
          const SizedBox(height: 8),
          _ImagePickerCard(
            imageFile: _filePengurus,
            existingUrl: struktur != null && struktur.gambarStrukturOrganisasi.isNotEmpty
                ? '${AppApiService.baseUrl}API/uploads/profil_masjid/${struktur.gambarStrukturOrganisasi}'
                : null,
            onTap: () => _pickImage(isPengurus: true),
          ),
          const SizedBox(height: 8),
          OutlinedButton.icon(
            onPressed: () => _pickImage(isPengurus: true),
            icon: const Icon(Icons.image),
            label: const Text('Pilih Gambar Pengurus'),
          ),
          const SizedBox(height: 24),

          // ——— Gambar Remas ———
          Text('Gambar Struktur Remas', style: Theme.of(context).textTheme.titleMedium),
          const SizedBox(height: 8),
          _ImagePickerCard(
            imageFile: _fileRemas,
            existingUrl: struktur != null && struktur.gambarStrukturRemas.isNotEmpty
                ? '${AppApiService.baseUrl}API/uploads/profil_masjid/${struktur.gambarStrukturRemas}'
                : null,
            onTap: () => _pickImage(isPengurus: false),
          ),
          const SizedBox(height: 8),
          OutlinedButton.icon(
            onPressed: () => _pickImage(isPengurus: false),
            icon: const Icon(Icons.image),
            label: const Text('Pilih Gambar Remas'),
          ),
          const SizedBox(height: 24),

          FilledButton(
            onPressed: _saving ? null : () => _submit(struktur),
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

  Future<void> _pickImage({required bool isPengurus}) async {
    final file = await _picker.pickImage(source: ImageSource.gallery, imageQuality: 80);
    if (file != null) {
      setState(() {
        if (isPengurus) {
          _filePengurus = File(file.path);
        } else {
          _fileRemas = File(file.path);
        }
      });
    }
  }

  Future<void> _submit(StrukturOrganisasiModel? struktur) async {
    if (_filePengurus == null && _fileRemas == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Pilih minimal satu gambar untuk diubah')),
      );
      return;
    }

    setState(() => _saving = true);
    try {
      final result = await AppApiService.updateStrukturOrganisasi(
        gambarPengurusFile: _filePengurus,
        gambarRemasFile: _fileRemas,
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(result.message.isEmpty ? 'Berhasil disimpan' : result.message)),
      );
      Navigator.pop(context, true);
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Gagal menyimpan: $e')));
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }
}

class _ImagePickerCard extends StatelessWidget {
  const _ImagePickerCard({
    required this.imageFile,
    required this.existingUrl,
    required this.onTap,
  });

  final File? imageFile;
  final String? existingUrl;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: 200,
        decoration: BoxDecoration(
          color: Colors.grey[200],
          borderRadius: BorderRadius.circular(8),
          border: Border.all(color: Colors.grey[400]!),
        ),
        child: imageFile != null
            ? ClipRRect(
                borderRadius: BorderRadius.circular(8),
                child: Image.file(imageFile!, fit: BoxFit.cover, width: double.infinity),
              )
            : existingUrl != null
                ? ClipRRect(
                    borderRadius: BorderRadius.circular(8),
                    child: Image.network(
                      existingUrl!,
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
    );
  }
}

