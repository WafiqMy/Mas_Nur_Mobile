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

import com.bumptech.glide.Glide;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;
import com.example.masnur.Fitur_Persewaan.BarangModel;
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

public class EditBarangActivity extends AppCompatActivity {

    private EditText edtNama, edtHarga, edtJumlah;
    private Spinner spinnerJenis;
    private ImageView imgPreview;
    private Button btnPilihGambar, btnSimpan;
    private ApiService apiService;
    private BarangModel barang;
    private Bitmap bitmap;
    private boolean gambarDiubah = false;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        imgPreview.setImageBitmap(bitmap);
                        gambarDiubah = true;
                    } catch (IOException e) {
                        Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_barang);

        initViews();
        apiService = ApiClient.getService();

        // Ambil data dari intent
        barang = getIntent().getParcelableExtra("barang");
        if (barang == null) {
            Toast.makeText(this, "Data barang tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadData();
    }

    private void initViews() {
        edtNama = findViewById(R.id.edtNama);
        edtHarga = findViewById(R.id.edtHarga);
        edtJumlah = findViewById(R.id.edtJumlah);
        spinnerJenis = findViewById(R.id.spinnerJenis);
        imgPreview = findViewById(R.id.imgPreview);
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSimpan = findViewById(R.id.btnSimpan);

        setupSpinner();
        setupClickListeners();
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
                simpanPerubahan();
            }
        });
    }

    private void loadData() {
        edtNama.setText(barang.getNamaBarang());
        edtHarga.setText(String.valueOf(barang.getHarga()));
        edtJumlah.setText(String.valueOf(barang.getJumlah()));

        // Set spinner
        String[] jenis = {"Gedung", "Alat Multimedia", "Alat Musik"};
        for (int i = 0; i < jenis.length; i++) {
            if (barang.getJenis().toLowerCase().contains(jenis[i].toLowerCase())) {
                spinnerJenis.setSelection(i);
                break;
            }
        }

        // Load gambar
        String url = "http://masnurhuda.atwebpages.com/API/get_gambar.php?file=" + barang.getGambar();
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imgPreview);
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

    private void simpanPerubahan() {
        ProgressDialog dialog = ProgressDialog.show(this, "Menyimpan...", "Mohon tunggu", true);

        RequestBody idPersewaan = RequestBody.create(String.valueOf(barang.getIdPersewaan()), MediaType.get("text/plain"));
        RequestBody namaBarang = RequestBody.create(edtNama.getText().toString(), MediaType.get("text/plain"));
        RequestBody jenis = RequestBody.create(spinnerJenis.getSelectedItem().toString(), MediaType.get("text/plain"));
        RequestBody harga = RequestBody.create(edtHarga.getText().toString(), MediaType.get("text/plain"));
        RequestBody jumlah = RequestBody.create(edtJumlah.getText().toString(), MediaType.get("text/plain"));

        MultipartBody.Part filePart = null;

        if (gambarDiubah && bitmap != null) {
            // Kompres ke JPEG
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();

            File file = new File(getCacheDir(), "gambar_edit.jpg");
            try {
                java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                fos.write(byteArray);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/jpeg"));
            filePart = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);
        }

        Call<ReservasiResponse> call = apiService.editBarang(
                idPersewaan, namaBarang, jenis, harga, jumlah, filePart
        );

        call.enqueue(new Callback<ReservasiResponse>() {
            @Override
            public void onResponse(Call<ReservasiResponse> call, Response<ReservasiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    ReservasiResponse res = response.body();
                    if ("success".equals(res.getStatus())) {
                        Toast.makeText(EditBarangActivity.this, "Barang berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditBarangActivity.this, res.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EditBarangActivity.this, "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReservasiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditBarangActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}