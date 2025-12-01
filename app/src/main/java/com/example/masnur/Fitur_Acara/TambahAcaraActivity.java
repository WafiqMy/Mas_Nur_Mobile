package com.example.masnur.Fitur_Acara;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahAcaraActivity extends AppCompatActivity {

    private EditText etJudul, etLokasi, etTanggal, etDeskripsi;
    private ImageView imgPreview;
    private LinearLayout layoutPlaceholder;
    private Button btnUnggah;
    private Uri imageUri;
    private File imageFile;
    private ApiService apiService;

    private static final int PICK_IMAGE = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_acara);

        etJudul = findViewById(R.id.et_judul);
        etLokasi = findViewById(R.id.et_lokasi);
        etTanggal = findViewById(R.id.et_tanggal);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        imgPreview = findViewById(R.id.imgPreview);
        layoutPlaceholder = findViewById(R.id.layoutPlaceholder);
        btnUnggah = findViewById(R.id.btnUnggah);

        apiService = ApiClient.getService();
        etTanggal.setOnClickListener(v -> showDatePicker());

        layoutPlaceholder.setOnClickListener(v -> pilihGambar());
        imgPreview.setOnClickListener(v -> pilihGambar());

        etTanggal.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        btnUnggah.setOnClickListener(v -> simpanAcara());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year, month, day) -> {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
            etTanggal.setText(date);
        }, y, m, d).show();
    }

    private void pilihGambar() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = decodeSampledBitmapFromUri(imageUri, 1200, 1200);
                imgPreview.setImageBitmap(bitmap);
                imgPreview.setVisibility(View.VISIBLE);
                layoutPlaceholder.setVisibility(View.GONE);
                imageFile = createCompressedFile(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createCompressedFile(Bitmap bitmap) throws IOException {
        File dir = getCacheDir();
        File file = new File(dir, "event_" + System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int quality = 90;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        while (stream.size() > 1800 * 1024 && quality > 40) {
            quality -= 10;
            stream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(stream.toByteArray());
        }
        return file;
    }

    private Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private void simpanAcara() {
        String nama = etJudul.getText().toString().trim();
        String lokasi = etLokasi.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(lokasi) || TextUtils.isEmpty(tanggal)) {
            Toast.makeText(this, "Judul, lokasi, dan tanggal wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!tanggal.contains(" ")) {
            tanggal += " 00:00:00";
        }

        RequestBody namaRB = RequestBody.create(nama, MediaType.get("text/plain"));
        RequestBody lokasiRB = RequestBody.create(lokasi, MediaType.get("text/plain"));
        RequestBody tanggalRB = RequestBody.create(tanggal, MediaType.get("text/plain"));
        RequestBody deskripsiRB = RequestBody.create(deskripsi, MediaType.get("text/plain"));
        RequestBody usernameRB = RequestBody.create("admin", MediaType.get("text/plain"));

        MultipartBody.Part gambarPart = null;
        if (imageFile != null && imageFile.exists()) {
            RequestBody reqFile = RequestBody.create(imageFile, MediaType.parse("image/jpeg"));
            gambarPart = MultipartBody.Part.createFormData("gambar_event", imageFile.getName(), reqFile);
        }

        btnUnggah.setEnabled(false);
        btnUnggah.setText("Menyimpan...");

        Call<AcaraResponse> call = apiService.tambahAcara(
                namaRB, tanggalRB, deskripsiRB, lokasiRB, usernameRB, gambarPart
        );

        call.enqueue(new Callback<AcaraResponse>() {
            @Override
            public void onResponse(Call<AcaraResponse> call, Response<AcaraResponse> response) {
                btnUnggah.setEnabled(true);
                btnUnggah.setText("Unggah");
                if (response.isSuccessful() && response.body() != null) {
                    AcaraResponse res = response.body();
                    if ("success".equals(res.getStatus()) || "1".equals(res.getStatus())) {
                        Toast.makeText(TambahAcaraActivity.this, "✓ " + res.getMessage(), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(TambahAcaraActivity.this, "✗ " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TambahAcaraActivity.this, "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AcaraResponse> call, Throwable t) {
                btnUnggah.setEnabled(true);
                btnUnggah.setText("Unggah");
                Toast.makeText(TambahAcaraActivity.this, "Jaringan error", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}