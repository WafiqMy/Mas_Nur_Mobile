import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/core/models/app_models.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/features/informasi/edit_profil_masjid_page.dart';
import 'package:mas_nur_flutter/features/informasi/edit_struktur_organisasi_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_drawer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_header.dart';

class InformasiMasjidPage extends StatefulWidget {
  const InformasiMasjidPage({super.key});
  static const routeName = '/informasi-masjid';

  @override
  State<InformasiMasjidPage> createState() => _InformasiMasjidPageState();
}

class _InformasiMasjidPageState extends State<InformasiMasjidPage>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;
  late Future<(ProfilMasjidModel?, StrukturOrganisasiModel?)> _future;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
    _loadData();
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  void _loadData() {
    _future = Future.wait([
      AppApiService.getProfilMasjid(),
      AppApiService.getStrukturOrganisasi(),
    ]).then((results) => (results[0] as ProfilMasjidModel?, results[1] as StrukturOrganisasiModel?));
  }

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) {
        if (!didPop) {
          Navigator.pushReplacementNamed(context, DashboardPage.routeName);
        }
      },
      child: Scaffold(
        backgroundColor: kColorWhite,
        drawer: const AppDrawer(),
        body: Column(
          children: [
            // ── Header ──────────────────────────────────────────────────────
            const AppHeader(showBackButton: true),

            // ── TabBar ──────────────────────────────────────────────────────
            Container(
              color: kColorHeader,
              child: TabBar(
                controller: _tabController,
                labelColor: const Color(0xFF0D47A1),
                unselectedLabelColor: Colors.black54,
                indicatorColor: const Color(0xFF0D47A1),
                tabs: const [
                  Tab(icon: Icon(Icons.mosque), text: 'Profil Masjid'),
                  Tab(icon: Icon(Icons.account_tree), text: 'Struktur Organisasi'),
                ],
              ),
            ),

            // ── Konten ──────────────────────────────────────────────────────
            Expanded(
              child: FutureBuilder<(ProfilMasjidModel?, StrukturOrganisasiModel?)>(
                future: _future,
                builder: (context, snapshot) {
                  if (snapshot.connectionState != ConnectionState.done) {
                    return const Center(child: CircularProgressIndicator());
                  }
                  if (snapshot.hasError) {
                    return Center(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          const Text('Gagal memuat informasi masjid'),
                          const SizedBox(height: 12),
                          OutlinedButton(
                            onPressed: () => setState(_loadData),
                            child: const Text('Coba Lagi'),
                          ),
                        ],
                      ),
                    );
                  }
                  final profil = snapshot.data?.$1;
                  final struktur = snapshot.data?.$2;

                  return TabBarView(
                    controller: _tabController,
                    children: [
                      _ProfilTab(
                        profil: profil,
                        onEdit: () async {
                          final updated = await Navigator.pushNamed(
                            context,
                            EditProfilMasjidPage.routeName,
                            arguments: profil,
                          );
                          if (updated == true && mounted) setState(_loadData);
                        },
                      ),
                      _StrukturTab(
                        struktur: struktur,
                        onEdit: () async {
                          final updated = await Navigator.pushNamed(
                            context,
                            EditStrukturOrganisasiPage.routeName,
                            arguments: struktur,
                          );
                          if (updated == true && mounted) setState(_loadData);
                        },
                      ),
                    ],
                  );
                },
              ),
            ),

            // ── Footer ──────────────────────────────────────────────────────
            const AppFooter(currentIndex: -1),
          ],
        ),
      ),
    );
  }
}

// ——— Tab Profil Masjid ———
class _ProfilTab extends StatelessWidget {
  const _ProfilTab({required this.profil, required this.onEdit});
  final ProfilMasjidModel? profil;
  final VoidCallback onEdit;

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        // Gambar masjid
        if (profil != null && profil!.gambarSejarahMasjid.isNotEmpty)
          ClipRRect(
            borderRadius: BorderRadius.circular(12),
            child: Image.network(
              '${AppApiService.baseUrl}API/uploads/profil_masjid/${profil!.gambarSejarahMasjid}',
              height: 220,
              width: double.infinity,
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) => Container(
                height: 220,
                color: Colors.grey[200],
                child: const Center(child: Icon(Icons.mosque, size: 64, color: Colors.grey)),
              ),
            ),
          )
        else
          Container(
            height: 220,
            decoration: BoxDecoration(
              color: Colors.grey[200],
              borderRadius: BorderRadius.circular(12),
            ),
            child: const Center(child: Icon(Icons.mosque, size: 64, color: Colors.grey)),
          ),
        const SizedBox(height: 16),

        // Judul
        Text(
          profil?.judulSejarah ?? 'Belum ada judul',
          style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 12),

        // Deskripsi
        Text(
          profil?.deskripsiSejarah ?? 'Belum ada deskripsi',
          style: Theme.of(context).textTheme.bodyMedium,
        ),
        const SizedBox(height: 24),

        // Tombol kelola
        FilledButton.icon(
          onPressed: onEdit,
          icon: const Icon(Icons.edit),
          label: const Text('Kelola Profil Masjid'),
        ),
      ],
    );
  }
}

// ——— Tab Struktur Organisasi ———
class _StrukturTab extends StatelessWidget {
  const _StrukturTab({required this.struktur, required this.onEdit});
  final StrukturOrganisasiModel? struktur;
  final VoidCallback onEdit;

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        // Gambar Pengurus
        Text('Struktur Pengurus', style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold)),
        const SizedBox(height: 8),
        _buildGambar(struktur?.gambarStrukturOrganisasi),
        const SizedBox(height: 24),

        // Gambar Remas
        Text('Struktur Remas', style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold)),
        const SizedBox(height: 8),
        _buildGambar(struktur?.gambarStrukturRemas),
        const SizedBox(height: 24),

        // Tombol ubah
        FilledButton.icon(
          onPressed: onEdit,
          icon: const Icon(Icons.edit),
          label: const Text('Ubah Struktur Organisasi'),
        ),
      ],
    );
  }

  Widget _buildGambar(String? fileName) {
    if (fileName == null || fileName.isEmpty) {
      return Container(
        height: 200,
        decoration: BoxDecoration(
          color: Colors.grey[200],
          borderRadius: BorderRadius.circular(8),
        ),
        child: const Center(
          child: Text('Belum ada gambar', style: TextStyle(color: Colors.grey)),
        ),
      );
    }
    return ClipRRect(
      borderRadius: BorderRadius.circular(8),
      child: Image.network(
        '${AppApiService.baseUrl}API/uploads/profil_masjid/$fileName',
        width: double.infinity,
        fit: BoxFit.contain,
        errorBuilder: (context, error, stackTrace) => Container(
          height: 200,
          color: Colors.grey[200],
          child: const Center(child: Icon(Icons.image_not_supported, size: 48, color: Colors.grey)),
        ),
      ),
    );
  }
}

