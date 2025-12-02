package com.example.masnur.Fitur_Persewaan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahBarangActivity extends AppCompatActivity {

    private EditText edtNama, edtHarga, edtJumlah, edtDeskripsi, edtSpesifikasi, edtFasilitas;
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
                        // Decode & compress di awal
                        bitmap = decodeSampledBitmapFromUri(imageUri, 1024, 1024);
                        if (bitmap != null) {
                            imgPreview.setImageBitmap(bitmap);
                            imgPreview.setVisibility(android.view.View.VISIBLE);
                        } else {
                            throw new IOException("Bitmap null");
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Gagal memuat/memproses gambar", Toast.LENGTH_SHORT).show();
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
        edtDeskripsi = findViewById(R.id.edtDeskripsi);
        edtSpesifikasi = findViewById(R.id.edtSpesifikasi);
        edtFasilitas = findViewById(R.id.edtFasilitas);
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
        String hargaStr = edtHarga.getText().toString().trim();
        String jumlahStr = edtJumlah.getText().toString().trim();

        if (nama.isEmpty()) {
            edtNama.setError("Wajib diisi");
            return false;
        }
        if (hargaStr.isEmpty() || Integer.parseInt(hargaStr) < 0) {
            edtHarga.setError("Harga ≥ 0");
            return false;
        }
        if (jumlahStr.isEmpty() || Integer.parseInt(jumlahStr) < 1) {
            edtJumlah.setError("Stok ≥ 1");
            return false;
        }
        return true;
    }

    private void simpanBarang() {
        ProgressDialog dialog = ProgressDialog.show(this, "Menyimpan...", "Mohon tunggu", true);

        RequestBody namaBarang = RequestBody.create(edtNama.getText().toString(), MediaType.get("text/plain"));
        RequestBody jenis = RequestBody.create(spinnerJenis.getSelectedItem().toString(), MediaType.get("text/plain"));
        RequestBody harga = RequestBody.create(edtHarga.getText().toString(), MediaType.get("text/plain"));
        RequestBody jumlah = RequestBody.create(edtJumlah.getText().toString(), MediaType.get("text/plain"));
        // 🔹 TAMBAHAN
        RequestBody deskripsi = RequestBody.create(edtDeskripsi.getText().toString(), MediaType.get("text/plain"));
        RequestBody spesifikasi = RequestBody.create(edtSpesifikasi.getText().toString(), MediaType.get("text/plain"));
        RequestBody fasilitas = RequestBody.create(edtFasilitas.getText().toString(), MediaType.get("text/plain"));

        MultipartBody.Part filePart = null;

        if (bitmap != null) {
            // Kompres & pastikan ≤ 2MB
            File compressedFile = compressBitmapToFile(bitmap, 90); // kualitas 90%
            if (compressedFile != null && compressedFile.length() > 2 * 1024 * 1024) {
                // Jika masih >2MB, turunkan kualitas
                compressedFile = compressBitmapToFile(bitmap, 70);
            }

            if (compressedFile == null || !compressedFile.exists()) {
                dialog.dismiss();
                Toast.makeText(this, "Gagal kompres gambar", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody requestFile = RequestBody.create(compressedFile, MediaType.parse("image/jpeg"));
            filePart = MultipartBody.Part.createFormData("gambar", compressedFile.getName(), requestFile);
        }

        Call<ReservasiResponse> call = apiService.tambahBarang(
                namaBarang, jenis, harga, jumlah,
                // 🔹 TAMBAHKAN 3 BARU (API belum pakai, jadi kita kirim sebagai field tambahan)
                deskripsi, spesifikasi, fasilitas, // ❌ TAPI API PHP BELUM DUKUNG!
                filePart
        );

        // ❗ KOREKSI: Karena ApiService belum punya method 7 param, kita buat dulu di bawah!
        // → LANJUT KE LANGKAH 7

        // Untuk sementara, kita buat versi terpisah (karena API belum support 7 field text)
        // ✅ SOLUSI: Kita kirim via @Part Multipart — sudah kompatibel sejak awal!

        // TAPI: ApiService.tambahBarang() saat ini hanya 5 param!
        // KITA FIX DI LANGKAH 7 → update ApiService & buat method baru

        dialog.dismiss();
        Toast.makeText(this, "⚠️ ApiService belum diupdate — lanjut ke langkah 7", Toast.LENGTH_LONG).show();
        // Ini placeholder — nanti diganti setelah ApiService diperbarui
    }

    // Helper: decode & scale bitmap
    private Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (IOException e) {
            return null;
        }
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

    // Helper: kompres bitmap ke file JPEG ≤ 2MB
    private File compressBitmapToFile(Bitmap bitmap, int quality) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            byte[] byteArray = stream.toByteArray();

            File file = new File(getCacheDir(), "compressed_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArray);
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}