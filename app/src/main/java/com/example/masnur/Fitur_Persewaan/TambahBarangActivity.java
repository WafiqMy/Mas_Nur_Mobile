package com.example.masnur.Fitur_Persewaan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;
import com.example.masnur.Fitur_Persewaan.ReservasiResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahBarangActivity extends AppCompatActivity {

    private EditText edtNama, edtHarga, edtJumlah;
    private Spinner spinnerJenis;
    private ImageView imgPreview;
    private Button btnPilihGambar, btnSimpan;
    private ApiService apiService;
    private Bitmap bitmap;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        imgPreview.setImageBitmap(bitmap);
                        imgPreview.setVisibility(android.view.View.VISIBLE);
                    } catch (IOException e) {
                        Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_barang_persewaan);

        initViews();
        setupSpinner();
        setupClickListeners();
        apiService = ApiClient.getService();
    }

    private void initViews() {
        edtNama = findViewById(R.id.edtNama);
        edtHarga = findViewById(R.id.edtHarga);
        edtJumlah = findViewById(R.id.edtJumlah);
        spinnerJenis = findViewById(R.id.spinnerJenis);
        imgPreview = findViewById(R.id.imgPreview);
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSimpan = findViewById(R.id.btnSimpan);
        imgPreview.setVisibility(android.view.View.GONE);
    }

    private void setupSpinner() {
        String[] jenis = {"Gedung", "Alat Multimedia", "Alat Musik"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenis);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenis.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnPilihGambar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSimpan.setOnClickListener(v -> {
            if (validateInput()) {
                simpanBarang();
            }
        });
    }

    private boolean validateInput() {
        String nama = edtNama.getText().toString().trim();
        String harga = edtHarga.getText().toString().trim();
        String jumlah = edtJumlah.getText().toString().trim();

        if (nama.isEmpty()) {
            edtNama.setError("Nama barang wajib diisi");
            return false;
        }
        if (harga.isEmpty() || Integer.parseInt(harga) < 0) {
            edtHarga.setError("Harga harus angka >= 0");
            return false;
        }
        if (jumlah.isEmpty() || Integer.parseInt(jumlah) < 1) {
            edtJumlah.setError("Jumlah harus >= 1");
            return false;
        }
        return true;
    }

    private void simpanBarang() {
        ProgressDialog dialog = ProgressDialog.show(this, "Menyimpan...", "Mohon tunggu", true);

        // Siapkan data teks
        RequestBody namaBarang = RequestBody.create(edtNama.getText().toString(), MediaType.get("text/plain"));
        RequestBody jenis = RequestBody.create(spinnerJenis.getSelectedItem().toString(), MediaType.get("text/plain"));
        RequestBody harga = RequestBody.create(edtHarga.getText().toString(), MediaType.get("text/plain"));
        RequestBody jumlah = RequestBody.create(edtJumlah.getText().toString(), MediaType.get("text/plain"));

        // Siapkan file gambar (jika ada)
        MultipartBody.Part filePart = null;

        if (bitmap != null) {
            // Kompres gambar ke PNG
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            File file = new File(getCacheDir(), "gambar_temp.png");
            try {
                java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                fos.write(byteArray);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/png"));
            filePart = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);
        }

        // ✅ KIRIM HANYA filePart (tanpa gambarBody)
        Call<ReservasiResponse> call = apiService.tambahBarang(
                namaBarang, jenis, harga, jumlah, filePart
        );

        call.enqueue(new Callback<ReservasiResponse>() {
            @Override
            public void onResponse(Call<ReservasiResponse> call, Response<ReservasiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    ReservasiResponse res = response.body();
                    if ("success".equals(res.getStatus())) {
                        Toast.makeText(TambahBarangActivity.this, "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(TambahBarangActivity.this, res.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TambahBarangActivity.this, "Gagal: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReservasiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TambahBarangActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}