package com.example.masnur.Fitur_Informasi_Masjid;

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
    private Button btnPilihGambar, btnSimpan, btnBatal;
    private ApiService apiService;
    private ProfilMasjidModel profil;
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
        setContentView(R.layout.activity_edit_profil_masjid);

        initViews();
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

    private void loadData() {
        edtJudul.setText(profil.getJudulSejarah());
        edtDeskripsi.setText(profil.getDeskripsiSejarah());

        String url = "http://masnurhuda.atwebpages.com/API/api_gambar_profil_masjid.php?file_name=" +
                (profil.getGambarSejarahMasjid() != null ? profil.getGambarSejarahMasjid() : "default_placeholder.png");

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.ic_launcher_background)
                .into(imgPreview);
    }

    private void simpanPerubahan() {
        String judul = edtJudul.getText().toString().trim();
        String deskripsi = edtDeskripsi.getText().toString().trim();
        String username = "admin"; // SESUAIKAN

        if (judul.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(this, "Judul dan deskripsi wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog dialog = ProgressDialog.show(this, "Menyimpan...", "Mohon tunggu", true);

        RequestBody reqJudul = RequestBody.create(judul, MediaType.get("text/plain"));
        RequestBody reqDeskripsi = RequestBody.create(deskripsi, MediaType.get("text/plain"));
        RequestBody reqUsername = RequestBody.create(username, MediaType.get("text/plain"));

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

            RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/jpeg"));
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
                    Toast.makeText(EditProfilMasjidActivity.this, msg != null ? msg : "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditProfilMasjidActivity.this, "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfilMasjidResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditProfilMasjidActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}