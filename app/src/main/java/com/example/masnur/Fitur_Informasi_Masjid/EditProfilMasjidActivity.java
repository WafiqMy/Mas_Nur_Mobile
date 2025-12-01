package com.example.masnur.Fitur_Informasi_Masjid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfilMasjidActivity extends AppCompatActivity {

    private ImageView imgPreview;
    private EditText edtJudul, edtDeskripsi;
    private TextView tvCounter; // ✅ Counter
    private Button btnPilihGambar, btnSimpan, btnBatal;
    private ApiService apiService;
    private ProfilMasjidModel profil;
    private Bitmap bitmap;
    private boolean gambarDiubah = false;

    private static final int MAX_CHAR = 13000; // ✅ Batas 13.000 karakter

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
        setContentView(R.layout.activity_edit_profil_masjid);

        initViews();
        setupCharacterCounter(); // ✅ Aktifkan counter
        apiService = ApiClient.getService();

        profil = getIntent().getParcelableExtra("profil");
        if (profil == null) {
            Toast.makeText(this, "Data profil tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadData();
    }

    private void initViews() {
        imgPreview = findViewById(R.id.imgPreview);
        edtJudul = findViewById(R.id.edtJudul);
        edtDeskripsi = findViewById(R.id.edtDeskripsi);
        tvCounter = findViewById(R.id.tvCounter); // ✅ Wajib
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatal);

        btnPilihGambar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSimpan.setOnClickListener(v -> simpanPerubahan());
        btnBatal.setOnClickListener(v -> finish());
    }

    // ✅ Counter real-time dengan warna
    private void setupCharacterCounter() {
        tvCounter.setText("0/" + MAX_CHAR);
        tvCounter.setTextColor(getResources().getColor(android.R.color.darker_gray));

        edtDeskripsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                tvCounter.setText(length + "/" + MAX_CHAR);

                if (length > MAX_CHAR) {
                    tvCounter.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else if (length > MAX_CHAR * 0.9) { // > 11.700 karakter
                    tvCounter.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                } else {
                    tvCounter.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });
    }

    private void loadData() {
        edtJudul.setText(profil.getJudulSejarah());
        edtDeskripsi.setText(profil.getDeskripsiSejarah());
        updateCounterDisplay(); // ✅ Set awal

        String url = profil.getGambarSejarahMasjidUrl();
        if (url == null || url.trim().isEmpty()) {
            String fileName = profil.getGambarSejarahMasjid();
            if (fileName != null && !fileName.trim().isEmpty()) {
                url = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/" + fileName.trim();
            }
        }
        if (url == null || url.trim().isEmpty()) {
            url = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/default_placeholder.png";
        }

        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imgPreview);
    }

    private void updateCounterDisplay() {
        int length = edtDeskripsi.getText().toString().length();
        tvCounter.setText(length + "/" + MAX_CHAR);
        if (length > MAX_CHAR) {
            tvCounter.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else if (length > MAX_CHAR * 0.9) {
            tvCounter.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            tvCounter.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void simpanPerubahan() {
        String judul = edtJudul.getText().toString().trim();
        String deskripsi = edtDeskripsi.getText().toString().trim();
        String username = "admin";

        // ✅ Validasi input & batas karakter
        if (judul.isEmpty()) {
            Toast.makeText(this, "Judul wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (deskripsi.isEmpty()) {
            Toast.makeText(this, "Deskripsi wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (deskripsi.length() > MAX_CHAR) {
            Toast.makeText(this, "Deskripsi maksimal " + MAX_CHAR + " karakter", Toast.LENGTH_LONG).show();
            return; // ✅ Blokir simpan
        }

        ProgressDialog dialog = ProgressDialog.show(this, "Menyimpan...", "Mohon tunggu", true);

        RequestBody reqJudul = RequestBody.create(MediaType.parse("text/plain"), judul);
        RequestBody reqDeskripsi = RequestBody.create(MediaType.parse("text/plain"), deskripsi);
        RequestBody reqUsername = RequestBody.create(MediaType.parse("text/plain"), username);

        MultipartBody.Part filePart = null;

        if (gambarDiubah && bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();

            File file = new File(getCacheDir(), "gambar_sejarah.jpg");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(byteArray);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            filePart = MultipartBody.Part.createFormData("gambar_sejarah_masjid", file.getName(), requestFile);
        }

        Call<ProfilMasjidResponse> call = apiService.updateProfilMasjid(
                reqJudul, reqDeskripsi, reqUsername, filePart
        );

        call.enqueue(new Callback<ProfilMasjidResponse>() {
            @Override
            public void onResponse(Call<ProfilMasjidResponse> call, Response<ProfilMasjidResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    String msg = response.body().getMessage();
                    Toast.makeText(EditProfilMasjidActivity.this,
                            msg != null ? "✓ " + msg : "✓ Deskripsi berhasil disimpan", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    ProfilMasjidModel updated = new ProfilMasjidModel();
                    updated.setJudulSejarah(judul);
                    updated.setDeskripsiSejarah(deskripsi);
                    updated.setUsername(username);
                    updated.setGambarSejarahMasjid(profil.getGambarSejarahMasjid());
                    updated.setGambarSejarahMasjidUrl(profil.getGambarSejarahMasjidUrl());
                    resultIntent.putExtra("updated_profil", updated);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(EditProfilMasjidActivity.this, "✗ Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfilMasjidResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditProfilMasjidActivity.this, "⚠️ Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}