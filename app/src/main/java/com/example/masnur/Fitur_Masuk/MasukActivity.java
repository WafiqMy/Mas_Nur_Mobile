package com.example.masnur.Fitur_Masuk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.Fitur_Halaman_Utama.Halaman_Utama_Activity;
import com.example.masnur.Fitur_Masuk.LoginResponse;
import com.example.masnur.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasukActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnMasuk;
    TextView tvLupaPassword;
    ImageButton btnTogglePassword; // ✅ tambahkan ini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnMasuk = findViewById(R.id.btnMasuk);
        tvLupaPassword = findViewById(R.id.tvLupaSandi);
        btnTogglePassword = findViewById(R.id.btnTogglePassword); // ✅ inisialisasi

        // ✅ Toggle password visibility
        btnTogglePassword.setOnClickListener(v -> {
            boolean isVisible = edtPassword.getTransformationMethod() == null;
            if (isVisible) {
                // Sembunyikan password
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                btnTogglePassword.setImageResource(R.drawable.ic_eye_off);
            } else {
                // Tampilkan password
                edtPassword.setTransformationMethod(null);
                btnTogglePassword.setImageResource(R.drawable.ic_eye);
            }
            // Pindahkan kursor ke akhir
            edtPassword.setSelection(edtPassword.getText().length());
        });

        btnMasuk.setOnClickListener(view -> {
            if (!isNetworkAvailable()) {
                Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
                return;
            }
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString(); // no trim for password
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }
            prosesLogin(username, password);
        });

        tvLupaPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MasukActivity.this, KonfirmasiEmail.class);
            startActivity(intent);
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    private void prosesLogin(String username, String password) {
        btnMasuk.setEnabled(false);
        btnMasuk.setText("Memproses...");

        ApiService apiService = ApiClient.getService();
        Call<LoginResponse> call = apiService.loginAdmin(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnMasuk.setEnabled(true);
                btnMasuk.setText("Masuk");

                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().getStatus();
                    if ("success".equals(status)) {
                        // ✅ Simpan username ke SharedPreferences
                        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                        prefs.edit().putString("username", username).apply();

                        Toast.makeText(MasukActivity.this, "Login berhasil sebagai admin", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MasukActivity.this, Halaman_Utama_Activity.class));
                        finish();
                    } else {
                        String msg = response.body().getMessage() != null ? response.body().getMessage() : "Login gagal";
                        Toast.makeText(MasukActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMsg = "HTTP " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ": " + response.errorBody().string();
                        }
                    } catch (Exception ignored) {}
                    Log.e("LoginError", "Server error: " + errorMsg);
                    Toast.makeText(MasukActivity.this, "Error server: " + errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnMasuk.setEnabled(true);
                btnMasuk.setText("Masuk");

                Log.e("LoginError", "Network error", t);
                String msg = "Gagal terhubung ke server";
                if (t instanceof java.net.SocketTimeoutException) {
                    msg = "Waktu koneksi habis. Periksa server atau jaringan.";
                } else if (t instanceof java.net.UnknownHostException) {
                    msg = "Domain tidak ditemukan. Periksa URL server.";
                }
                Toast.makeText(MasukActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}