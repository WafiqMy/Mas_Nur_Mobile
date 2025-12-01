package com.example.masnur.Fitur_Informasi_Masjid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
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

public class EditStrukturOrganisasiActivity extends AppCompatActivity {

    private ImageView imgPengurus, imgRemas;
    private Button btnPilihPengurus, btnPilihRemas, btnSimpan, btnBatal;
    private ApiService apiService;
    private StrukturOrganisasiModel struktur;
    private Bitmap bitmapPengurus, bitmapRemas;
    private boolean pengurusDiubah = false, remasDiubah = false;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        if (pengurusDiubah) {
                            imgPengurus.setImageBitmap(bitmap);
                            bitmapPengurus = bitmap;
                        } else if (remasDiubah) {
                            imgRemas.setImageBitmap(bitmap);
                            bitmapRemas = bitmap;
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_struktur);

        initViews();
        apiService = ApiClient.getService();

        struktur = getIntent().getParcelableExtra("struktur");
        if (struktur == null) {
            Toast.makeText(this, "Data struktur tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadData();
    }

    private void initViews() {
        imgPengurus = findViewById(R.id.imgPengurus);
        imgRemas = findViewById(R.id.imgRemas);
        btnPilihPengurus = findViewById(R.id.btnPilihPengurus);
        btnPilihRemas = findViewById(R.id.btnPilihRemas);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatal);

        btnPilihPengurus.setOnClickListener(v -> {
            pengurusDiubah = true;
            remasDiubah = false;
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnPilihRemas.setOnClickListener(v -> {
            pengurusDiubah = false;
            remasDiubah = true;
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSimpan.setOnClickListener(v -> simpanPerubahan());
        btnBatal.setOnClickListener(v -> finish());
    }

    private void loadData() {
        // ——— PENGURUS ———
        String urlPengurus = struktur.getGambarStrukturOrganisasiUrl();
        if (urlPengurus == null || urlPengurus.trim().isEmpty()) {
            String fn = struktur.getGambarStrukturOrganisasi();
            if (fn != null && !fn.trim().isEmpty()) {
                urlPengurus = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/" + fn;
            }
        }
        if (urlPengurus == null || urlPengurus.trim().isEmpty()) {
            urlPengurus = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/default_placeholder.png";
        }
        Glide.with(this)
                .load(urlPengurus)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imgPengurus);

        // ——— REMAS ———
        String urlRemas = struktur.getGambarStrukturRemasUrl();
        if (urlRemas == null || urlRemas.trim().isEmpty()) {
            String fn = struktur.getGambarStrukturRemas();
            if (fn != null && !fn.trim().isEmpty()) {
                urlRemas = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/" + fn;
            }
        }
        if (urlRemas == null || urlRemas.trim().isEmpty()) {
            urlRemas = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/default_placeholder.png";
        }
        Glide.with(this)
                .load(urlRemas)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imgRemas);
    }

    private void simpanPerubahan() {
        String username = "admin";

        ProgressDialog dialog = ProgressDialog.show(this, "Menyimpan...", "Mohon tunggu", true);

        RequestBody reqUsername = RequestBody.create(MediaType.parse("text/plain"), username);

        MultipartBody.Part filePengurus = null;
        MultipartBody.Part fileRemas = null;

        if (pengurusDiubah && bitmapPengurus != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapPengurus.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();

            File file = new File(getCacheDir(), "struk_pengurus_" + System.currentTimeMillis() + ".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(byteArray);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            filePengurus = MultipartBody.Part.createFormData("gambar_struktur_organisasi", file.getName(), requestFile);
        }

        if (remasDiubah && bitmapRemas != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapRemas.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();

            File file = new File(getCacheDir(), "struk_remas_" + System.currentTimeMillis() + ".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(byteArray);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            fileRemas = MultipartBody.Part.createFormData("gambar_struktur_remas", file.getName(), requestFile);
        }

        Call<StrukturOrganisasiResponse> call = apiService.updateStrukturOrganisasi(
                filePengurus, fileRemas, reqUsername
        );

        call.enqueue(new Callback<StrukturOrganisasiResponse>() {
            @Override
            public void onResponse(Call<StrukturOrganisasiResponse> call, Response<StrukturOrganisasiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    StrukturOrganisasiResponse res = response.body();
                    String msg = res.getMessage();
                    Toast.makeText(EditStrukturOrganisasiActivity.this, msg != null ? msg : "Berhasil disimpan", Toast.LENGTH_SHORT).show();

                    // ✅ Kirim data baru ke fragment
                    StrukturOrganisasiModel updated = res.getData();
                    if (updated == null) {
                        updated = new StrukturOrganisasiModel();
                        // Isi dengan data terbaru (jika API tidak return full data)
                        updated.setGambarStrukturOrganisasi(struktur.getGambarStrukturOrganisasi()); // tetap
                        updated.setGambarStrukturRemas(struktur.getGambarStrukturRemas()); // tetap
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updated_struktur", updated);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(EditStrukturOrganisasiActivity.this, "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StrukturOrganisasiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditStrukturOrganisasiActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}