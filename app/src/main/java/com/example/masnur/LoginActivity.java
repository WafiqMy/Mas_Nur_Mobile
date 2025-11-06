package com.example.masnur;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Pastikan layout ini sesuai

        // Inisialisasi view
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Event klik tombol login
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validasi input
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Username dan Password wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // URL API PHP di InfinityFree
            String url = "http://masjidnurulhuda.infinityfreeapp.com/login_api.php";

            // Request ke server menggunakan Volley
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getString("status").equals("success")) {
                                JSONObject data = json.getJSONObject("data");
                                String nama = data.getString("username");
                                Toast.makeText(this, "Selamat datang " + nama, Toast.LENGTH_SHORT).show();

                                // Navigasi ke halaman utama
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Respon tidak valid", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(this, "Gagal koneksi ke server", Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };

            // Kirim request
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        });
    }
}