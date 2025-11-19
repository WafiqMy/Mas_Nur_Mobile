package com.example.masnur.Fitur_Masuk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;
import com.example.masnur.Fitur_Masuk.OtpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KodeOtp extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4;
    private Button buttonLanjut;
    private String email;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kode_otp);

        // Ambil email dari intent
        email = getIntent().getStringExtra("email");
        apiService = ApiClient.getService();

        // Inisialisasi komponen
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        buttonLanjut = findViewById(R.id.buttonLanjut);

        buttonLanjut.setOnClickListener(v -> {
            String kodeOTP = otp1.getText().toString().trim() +
                    otp2.getText().toString().trim() +
                    otp3.getText().toString().trim() +
                    otp4.getText().toString().trim();

            if (kodeOTP.length() < 4) {
                Toast.makeText(this, "Lengkapi semua kolom OTP!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Panggil API untuk verifikasi OTP
            verifyOtp(email, kodeOTP);
        });
    }

    private void verifyOtp(String email, String kodeOTP) {
        Call<OtpResponse> call = apiService.verifyOtp(email, kodeOTP);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("valid".equals(response.body().getStatus())) {
                        Toast.makeText(KodeOtp.this, "Verifikasi berhasil!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(KodeOtp.this, SandiBaru.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(KodeOtp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(KodeOtp.this, "Respons tidak valid dari server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText(KodeOtp.this, "Gagal verifikasi OTP: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}