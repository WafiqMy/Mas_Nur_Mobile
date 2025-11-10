package com.example.masnur.Fitur_Masuk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.masnur.R;
import com.example.masnur.Fitur_Halaman_Utama.Halaman_Utama_Activity; // pastikan ini sesuai dengan package dashboard kamu

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MasukActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnMasuk;

    String URL_LOGIN = "http://masnurhuda.atwebpages.com/login_admin1.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnMasuk = findViewById(R.id.btnMasuk);

        btnMasuk.setOnClickListener(view -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password wajib diisi", Toast.LENGTH_SHORT).show();
            } else {
                prosesLogin(username, password);
            }
        });
    }

    private void prosesLogin(String username, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    Log.d("LoginResponse", "Raw response: " + response);

                    String raw = response.trim();
                    int jsonStart = raw.indexOf("{");
                    if (jsonStart != -1) {
                        String cleanJson = raw.substring(jsonStart);
                        try {
                            JSONObject jsonObject = new JSONObject(cleanJson);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                Toast.makeText(this, "Login berhasil sebagai admin", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, Halaman_Utama_Activity.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Login gagal. Hanya admin yang bisa masuk.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Gagal parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Respons tidak valid dari server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("LoginError", "Volley error: " + error.toString());
                    Toast.makeText(this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}