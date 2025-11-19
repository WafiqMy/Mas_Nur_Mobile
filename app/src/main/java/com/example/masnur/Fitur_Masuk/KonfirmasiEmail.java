package com.example.masnur.Fitur_Masuk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.R;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.Fitur_Masuk.OtpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KonfirmasiEmail extends AppCompatActivity {

    EditText edtEmail;
    Button buttonLanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_email);

        edtEmail = findViewById(R.id.edtEmail);
        buttonLanjut = findViewById(R.id.buttonLanjut);

        buttonLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(KonfirmasiEmail.this, "Masukkan email terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(KonfirmasiEmail.this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
                } else {
                    // Kirim OTP ke email
                    kirimOtp(email);
                }
            }
        });
    }

    private void kirimOtp(String email) {
        ApiService apiService = ApiClient.getService();
        Call<OtpResponse> call = apiService.sendOtp(email);

        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equals(response.body().getStatus())) {
                        Toast.makeText(KonfirmasiEmail.this, "Kode verifikasi telah dikirim ke email: " + email, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(KonfirmasiEmail.this, KodeOtp.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(KonfirmasiEmail.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(KonfirmasiEmail.this, "Respons tidak valid dari server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText(KonfirmasiEmail.this, "Gagal terhubung ke server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}