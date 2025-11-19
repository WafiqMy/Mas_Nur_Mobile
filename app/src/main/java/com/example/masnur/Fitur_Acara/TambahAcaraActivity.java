package com.example.masnur.Fitur_Acara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;
import com.example.masnur.Fitur_Acara.AcaraResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahAcaraActivity extends AppCompatActivity {

    private EditText etJudul, etDeskripsi;
    private ImageView imgPoster;
    private Button btnUnggah;
    private ApiService apiService;
    private Bitmap bitmap;
    private boolean gambarDiubah = false;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imgPoster.setImageBitmap(bitmap);
                        imgPoster.setVisibility(ImageView.VISIBLE);
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
        setContentView(R.layout.activity_tambah_acara);

        etJudul = findViewById(R.id.et_judul);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        imgPoster = findViewById(R.id.layout_upload_poster).findViewById(R.id.layout_upload_poster);
        btnUnggah = findViewById(R.id.btnUnggah);

        imgPoster.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        apiService = ApiClient.getService();

        btnUnggah.setOnClickListener(v -> {
            if (validateInput()) {
                simpanAcara();
            }
        });
    }

    private boolean validateInput() {
        if (etJudul.getText().toString().trim().isEmpty()) {
            etJudul.setError("Judul wajib diisi");
            return false;
        }
        if (etDeskripsi.getText().toString().trim().isEmpty()) {
            etDeskripsi.setError("Deskripsi wajib diisi");
            return false;
        }
        return true;
    }

    private void simpanAcara() {
        ProgressDialog dialog = ProgressDialog.show(this, "Menyimpan...", "Mohon tunggu", true);

        RequestBody namaEvent = RequestBody.create(etJudul.getText().toString(), MediaType.get("text/plain"));
        RequestBody deskripsiEvent = RequestBody.create(etDeskripsi.getText().toString(), MediaType.get("text/plain"));
        RequestBody tanggalEvent = RequestBody.create("2025-12-01", MediaType.get("text/plain")); // ganti sesuai kebutuhan
        RequestBody lokasiEvent = RequestBody.create("Masjid Nurul Huda", MediaType.get("text/plain"));
        RequestBody username = RequestBody.create("admin", MediaType.get("text/plain"));

        MultipartBody.Part filePart = null;
        if (gambarDiubah && bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            File file = new File(getCacheDir(), "acara_temp.jpg");
            try {
                java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                fos.write(stream.toByteArray());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/jpeg"));
            filePart = MultipartBody.Part.createFormData("gambar_event", file.getName(), requestFile);
        }

        Call<AcaraResponse> call = apiService.tambahAcara(
                namaEvent, deskripsiEvent, tanggalEvent, lokasiEvent, username, filePart
        );

        call.enqueue(new Callback<AcaraResponse>() {
            @Override
            public void onResponse(Call<AcaraResponse> call, Response<AcaraResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    Toast.makeText(TambahAcaraActivity.this, "Acara berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(TambahAcaraActivity.this, "Gagal: " + (response.body() != null ? response.body().getMessage() : "Unknown"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AcaraResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TambahAcaraActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}