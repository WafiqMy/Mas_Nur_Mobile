import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/features/auth/kode_otp_page.dart';

class KonfirmasiEmailPage extends StatefulWidget {
  const KonfirmasiEmailPage({super.key});
  static const routeName = '/konfirmasi-email';

  @override
  State<KonfirmasiEmailPage> createState() => _KonfirmasiEmailPageState();
}

class _KonfirmasiEmailPageState extends State<KonfirmasiEmailPage> {
  final _emailController = TextEditingController();
  bool _loading = false;
  String? _info;

  Future<void> _kirimOtp() async {
    setState(() {
      _loading = true;
      _info = null;
    });
    try {
      final response = await AppApiService.sendOtp(_emailController.text.trim());
      if (!mounted) return;
      if (response.status.toLowerCase() == 'success') {
        Navigator.pushNamed(
          context,
          KodeOtpPage.routeName,
          arguments: _emailController.text.trim(),
        );
      } else {
        setState(() => _info = response.message);
      }
    } catch (_) {
      if (mounted) {
        setState(() => _info = 'Gagal mengirim OTP');
      }
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Konfirmasi Email')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            TextField(
              controller: _emailController,
              decoration: const InputDecoration(labelText: 'Email'),
            ),
            const SizedBox(height: 16),
            if (_info != null) Text(_info!),
            SizedBox(
              width: double.infinity,
              child: FilledButton(
                onPressed: _loading ? null : _kirimOtp,
                child: Text(_loading ? 'Mengirim...' : 'Kirim OTP'),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

