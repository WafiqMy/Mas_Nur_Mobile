package com.example.masnur.Fitur_Berita;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBeritaActivity extends AppCompatActivity {

    private EditText edtJudul, edtIsi;
    private TextView tvCounter;
    private Spinner spinnerTanggal;
    private ImageView imgPreview;
    private Button btnPilihGambar, btnSimpan;
    private ApiService apiService;
    private BeritaModel berita;
    private Uri imageUri;
    private File imageFile;

    private static final int MAX_CHAR = 12000;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = decodeSampledBitmapFromUri(imageUri, 1200, 1200);
                        imgPreview.setImageBitmap(bitmap);
                        imgPreview.setVisibility(View.VISIBLE);
                        imageFile = createCompressedFile(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Data berita hilang", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupSpinner();
        setupClickListeners();
        setupCharacterCounter();
        loadData();
    }

    private void initViews() {
        edtJudul = findViewById(R.id.edtJudul);
        edtIsi = findViewById(R.id.edtIsi);
        tvCounter = findViewById(R.id.tvCounter);
        spinnerTanggal = findViewById(R.id.spinnerTanggal);
        imgPreview = findViewById(R.id.imgPreview);
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSimpan = findViewById(R.id.btnSimpan);
        imgPreview.setVisibility(View.GONE);
    }

    private void setupSpinner() {
        String[] tanggal = {berita.getTanggalBerita().split(" ")[0]};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tanggal);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTanggal.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnPilihGambar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        btnSimpan.setOnClickListener(v -> {
            if (validateInput()) {
                simpanPerubahan();
            }
        });
    }

    private void setupCharacterCounter() {
        tvCounter.setText("0/" + MAX_CHAR);
        tvCounter.setTextColor(getResources().getColor(android.R.color.darker_gray));

        edtIsi.addTextChangedListener(new TextWatcher() {
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
                } else if (length > MAX_CHAR * 0.9) {
                    tvCounter.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                } else {
                    tvCounter.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });
    }

    private boolean validateInput() {
        String judul = edtJudul.getText().toString().trim();
        String isi = edtIsi.getText().toString().trim();

        if (judul.isEmpty()) {
            edtJudul.setError("Judul wajib diisi");
            return false;
        }
        if (isi.isEmpty()) {
            edtIsi.setError("Isi berita wajib diisi");
            return false;
        }
        if (isi.length() > MAX_CHAR) {
            Toast.makeText(this, "Isi berita maksimal " + MAX_CHAR + " karakter", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // ✅ ✅ ✅ KOMPRESI OTOMATIS SAMPAI ≤ 1.8 MB
    private File createCompressedFile(Bitmap bitmap) throws IOException {
        File dir = getCacheDir();
        File file = new File(dir, "edit_berita_" + System.currentTimeMillis() + ".jpg");

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

    private void loadData() {
        edtJudul.setText(berita.getJudulBerita());
        edtIsi.setText(berita.getIsiBerita());
        updateCounterDisplay();

        String url = berita.getFotoBeritaAbsolut();
        Glide.with(this).load(url).placeholder(R.drawable.default_image).into(imgPreview);
        imgPreview.setVisibility(View.VISIBLE);
    }

    private void updateCounterDisplay() {
        int length = edtIsi.getText().toString().length();
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
        ProgressDialog dialog = ProgressDialog.show(this, "Menyimpan...", "Mohon tunggu", true);

        String judul = edtJudul.getText().toString().trim();
        String isi = edtIsi.getText().toString().trim();
        String tanggal = spinnerTanggal.getSelectedItem().toString();
        String username = berita.getUsername();

        RequestBody idRB = RequestBody.create(berita.getIdBerita(), MediaType.get("text/plain"));
        RequestBody judulRB = RequestBody.create(judul, MediaType.get("text/plain"));
        RequestBody isiRB = RequestBody.create(isi, MediaType.get("text/plain"));
        RequestBody tanggalRB = RequestBody.create(tanggal, MediaType.get("text/plain"));
        RequestBody usernameRB = RequestBody.create(username, MediaType.get("text/plain"));

        MultipartBody.Part fotoPart = null;
        if (imageFile != null && imageFile.exists()) {
            RequestBody fileRB = RequestBody.create(imageFile, MediaType.parse("image/jpeg"));
            fotoPart = MultipartBody.Part.createFormData("foto_berita", imageFile.getName(), fileRB);
        }

        Call<BeritaResponse> call = apiService.editBerita(idRB, judulRB, isiRB, tanggalRB, usernameRB, fotoPart);

        call.enqueue(new Callback<BeritaResponse>() {
            @Override
            public void onResponse(Call<BeritaResponse> call, Response<BeritaResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    BeritaResponse res = response.body();
                    if ("success".equals(res.getStatus()) || "1".equals(res.getStatus())) {
                        Toast.makeText(EditBeritaActivity.this, "✓ Berita berhasil diubah", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditBeritaActivity.this, "✗ " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditBeritaActivity.this, "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeritaResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditBeritaActivity.this, "Jaringan error", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}