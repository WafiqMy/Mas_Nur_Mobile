import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/core/api/app_api_service.dart';
import 'package:mas_nur_flutter/features/auth/sandi_baru_page.dart';

class KodeOtpPage extends StatefulWidget {
  const KodeOtpPage({super.key});
  static const routeName = '/kode-otp';

  @override
  State<KodeOtpPage> createState() => _KodeOtpPageState();
}

class _KodeOtpPageState extends State<KodeOtpPage> {
  final _otpController = TextEditingController();
  bool _loading = false;
  String? _error;

  Future<void> _verifikasi(String email) async {
    setState(() {
      _loading = true;
      _error = null;
    });
    try {
      final response = await AppApiService.verifyOtp(email, _otpController.text.trim());
      if (!mounted) return;
      if (response.status.toLowerCase() == 'success') {
        Navigator.pushNamed(context, SandiBaruPage.routeName, arguments: email);
      } else {
        setState(() => _error = response.message);
      }
    } catch (_) {
      if (mounted) setState(() => _error = 'OTP tidak valid');
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final email = (ModalRoute.of(context)?.settings.arguments as String?) ?? '';
    return Scaffold(
      appBar: AppBar(title: const Text('Verifikasi OTP')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Text('Kode OTP dikirim ke: $email'),
            const SizedBox(height: 12),
            TextField(
              controller: _otpController,
              decoration: const InputDecoration(labelText: 'Kode OTP'),
            ),
            const SizedBox(height: 16),
            if (_error != null)
              Text(_error!, style: const TextStyle(color: Colors.red)),
            SizedBox(
              width: double.infinity,
              child: FilledButton(
                onPressed: _loading ? null : () => _verifikasi(email),
                child: Text(_loading ? 'Memverifikasi...' : 'Lanjut'),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

