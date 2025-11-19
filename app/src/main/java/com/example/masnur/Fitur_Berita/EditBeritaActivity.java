package com.example.masnur.Fitur_Berita;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBeritaActivity extends AppCompatActivity {

    private EditText edtJudul, edtIsi;
    private Spinner spinnerTanggal;
    private ImageView imgPreview;
    private Button btnPilihGambar, btnSimpan;
    private ApiService apiService;
    private BeritaModel berita;
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
                        imgPreview.setVisibility(View.VISIBLE); // ✅ Tampilkan gambar setelah dipilih
                        gambarDiubah = true;
                    } catch (IOException e) {
                        Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_berita);

        initViews();
        apiService = ApiClient.getService();

        berita = getIntent().getParcelableExtra("berita");
        if (berita == null) {
            Toast.makeText(this, "Data berita tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupSpinner();
        setupClickListeners();
        loadData();
    }

    private void initViews() {
        edtJudul = findViewById(R.id.edtJudul);
        edtIsi = findViewById(R.id.edtIsi);
        spinnerTanggal = findViewById(R.id.spinnerTanggal);
        imgPreview = findViewById(R.id.imgPreview);
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSimpan = findViewById(R.id.btnSimpan);
        imgPreview.setVisibility(View.GONE); // ✅ Awalnya disembunyikan
    }

    private void setupSpinner() {
        String[] tanggal = {berita.getTanggalBerita()};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tanggal);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTanggal.setAdapter(adapter);
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

    private boolean validateInput() {
        String judul = edtJudul.getText().toString().trim();
        String isi = edtIsi.getText().toString().trim();

        if (judul.isEmpty()) {
            edtJudul.setError("Judul berita wajib diisi");
            return false;
        }
        if (isi.isEmpty()) {
            edtIsi.setError("Isi berita wajib diisi");
            return false;
        }
        return true;
    }

    private void loadData() {
        edtJudul.setText(berita.getJudulBerita());
        edtIsi.setText(berita.getIsiBerita());

        Glide.with(this)
                .load(berita.getFotoBerita())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .centerCrop()
                .into(imgPreview);

        imgPreview.setVisibility(View.VISIBLE); // ✅ Tampilkan gambar awal
    }

    private void simpanPerubahan() {
        if (apiService == null) {
            Toast.makeText(this, "API belum siap, coba lagi", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog dialog = ProgressDialog.show(this, "Menyimpan...", "Mohon tunggu", true);

        RequestBody idBerita = RequestBody.create(berita.getIdBerita(), MediaType.parse("text/plain"));
        RequestBody judul = RequestBody.create(edtJudul.getText().toString(), MediaType.parse("text/plain"));
        RequestBody isi = RequestBody.create(edtIsi.getText().toString(), MediaType.parse("text/plain"));
        RequestBody tanggal = RequestBody.create(berita.getTanggalBerita(), MediaType.parse("text/plain"));
        RequestBody username = RequestBody.create(berita.getUsername(), MediaType.parse("text/plain"));

        MultipartBody.Part filePart = null;

        if (gambarDiubah && bitmap != null) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] byteArray = stream.toByteArray();

                File file = new File(getCacheDir(), "edit_berita.jpg");
                java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                fos.write(byteArray);
                fos.close();

                RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/jpeg"));
                filePart = MultipartBody.Part.createFormData("foto_berita", file.getName(), requestFile);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal proses gambar", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
        }

        Call<BeritaResponse> call = apiService.editBerita(idBerita, judul, isi, tanggal, username, filePart);

        call.enqueue(new Callback<BeritaResponse>() {
            @Override
            public void onResponse(Call<BeritaResponse> call, Response<BeritaResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    BeritaResponse res = response.body();
                    if ("success".equalsIgnoreCase(res.getStatus())) {
                        Toast.makeText(EditBeritaActivity.this, "Berita berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditBeritaActivity.this, "Error: " + res.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    String msg = "Gagal: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            msg += " - " + response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(EditBeritaActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeritaResponse> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(EditBeritaActivity.this, "Error jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}