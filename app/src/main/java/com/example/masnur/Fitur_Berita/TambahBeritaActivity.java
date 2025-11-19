package com.example.masnur.Fitur_Berita;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahBeritaActivity extends AppCompatActivity {

    private EditText edtJudul, edtIsi;
    private Spinner spinnerTanggal;
    private ImageView imgPreview;
    private Button btnPilihGambar, btnSimpan;
    private ApiService apiService;
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
                        imgPreview.setVisibility(android.view.View.VISIBLE);
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
        setContentView(R.layout.activity_tambah_berita);

        initViews();
        setupSpinner();
        setupClickListeners();
        apiService = ApiClient.getService();
    }

    private void initViews() {
        edtJudul = findViewById(R.id.edtJudul);
        edtIsi = findViewById(R.id.edtIsi);
        spinnerTanggal = findViewById(R.id.spinnerTanggal);
        imgPreview = findViewById(R.id.imgPreview);
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSimpan = findViewById(R.id.btnSimpan);
        imgPreview.setVisibility(android.view.View.GONE);
    }

    private void setupSpinner() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String[] tanggal = {today};
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
                if (!gambarDiubah) {
                    Toast.makeText(this, "Gambar belum dipilih, akan dikirim tanpa foto", Toast.LENGTH_SHORT).show();
                }
                simpanBerita();
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

    private void simpanBerita() {
        ProgressDialog dialog = ProgressDialog.show(this, "Menyimpan...", "Mohon tunggu", true);

        RequestBody judul = RequestBody.create(edtJudul.getText().toString(), MediaType.get("text/plain"));
        RequestBody isi = RequestBody.create(edtIsi.getText().toString(), MediaType.get("text/plain"));
        RequestBody tanggal = RequestBody.create(spinnerTanggal.getSelectedItem().toString(), MediaType.get("text/plain"));
        RequestBody username = RequestBody.create("admin", MediaType.get("text/plain")); // ganti jika ada login

        MultipartBody.Part filePart = null;

        if (gambarDiubah && bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();

            File file = new File(getCacheDir(), "gambar_berita.jpg");
            try {
                java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                fos.write(byteArray);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/jpeg"));
            filePart = MultipartBody.Part.createFormData("foto_berita", file.getName(), requestFile);
        }

        Call<BeritaResponse> call = apiService.tambahBerita(judul, isi, tanggal, username, filePart);

        call.enqueue(new Callback<BeritaResponse>() {
            @Override
            public void onResponse(Call<BeritaResponse> call, Response<BeritaResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    BeritaResponse res = response.body();
                    if ("success".equalsIgnoreCase(res.getStatus())) {
                        Toast.makeText(TambahBeritaActivity.this, "Berita berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(TambahBeritaActivity.this,
                                res.getMessage() != null ? res.getMessage() : "Gagal menambahkan berita",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TambahBeritaActivity.this, "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeritaResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TambahBeritaActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}