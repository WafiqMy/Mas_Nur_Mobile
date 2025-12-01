package com.example.masnur.Fitur_Acara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.masnur.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UbahAcaraFragment extends Fragment {

    private EditText etJudul, etLokasi, etTanggal, etDeskripsi, etVideo;
    private ImageView imgGambar;
    private Button btnUbahGambar, btnTambahDokumentasi, btnSimpan, btnKembali;
    private LinearLayout layoutDokumentasiContainer;
    private AcaraModel acara;
    private Uri gambarBaruUri;
    private List<Uri> dokumentasiBaruUris = new ArrayList<>();
    private List<File> dokumentasiBaruFiles = new ArrayList<>();
    private List<String> dokumentasiLamaList = new ArrayList<>();

    private ActivityResultLauncher<Intent> singleImageLauncher;
    private ActivityResultLauncher<Intent> multiImageLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        singleImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        gambarBaruUri = result.getData().getData();
                        if (gambarBaruUri != null) {
                            imgGambar.setImageURI(gambarBaruUri);
                        }
                    }
                });

        multiImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                tambahDokumentasiBaru(uri);
                            }
                        } else if (data.getData() != null) {
                            tambahDokumentasiBaru(data.getData());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ubah_acara, container, false);

        etJudul = view.findViewById(R.id.etJudul);
        etLokasi = view.findViewById(R.id.etLokasi);
        etTanggal = view.findViewById(R.id.etTanggal);
        etDeskripsi = view.findViewById(R.id.etDeskripsi);
        etVideo = view.findViewById(R.id.etVideo);
        imgGambar = view.findViewById(R.id.imgGambar);
        btnUbahGambar = view.findViewById(R.id.btnUbahGambar);
        btnTambahDokumentasi = view.findViewById(R.id.btnTambahDokumentasi);
        btnSimpan = view.findViewById(R.id.btnSimpan);
        btnKembali = view.findViewById(R.id.btnKembali);
        layoutDokumentasiContainer = view.findViewById(R.id.layoutDokumentasiContainer);

        if (getArguments() != null) {
            acara = getArguments().getParcelable("acara");
            if (acara != null) {
                etJudul.setText(acara.getNamaEvent());
                etLokasi.setText(acara.getLokasiEvent());
                etTanggal.setText(acara.getTanggalEvent().split(" ")[0]);
                etDeskripsi.setText(acara.getDeskripsiEvent());

                Glide.with(this).load(acara.getGambarEventAbsolut()).into(imgGambar);

                // Dokumentasi lama
                List<String> dokumentasiList = acara.getDokumentasi();
                if (dokumentasiList != null) {
                    for (String fileName : dokumentasiList) {
                        if (!TextUtils.isEmpty(fileName)) {
                            dokumentasiLamaList.add(fileName.trim());
                        }
                    }
                }
                tampilkanDokumentasi();

                // ✅ VIDEO: string mentah
                etVideo.setText(acara.getVideoUrls());
            }
        }

        // Setup listener
        imgGambar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            singleImageLauncher.launch(intent);
        });

        btnTambahDokumentasi.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            multiImageLauncher.launch(intent);
        });

        btnKembali.setOnClickListener(v -> requireActivity().onBackPressed());
        btnSimpan.setOnClickListener(v -> simpanPerubahan());

        return view;
    }

    private void tampilkanDokumentasi() {
        layoutDokumentasiContainer.removeAllViews();

        for (String fileName : dokumentasiLamaList) {
            tambahPreviewDokumentasi(fileName, true);
        }

        for (int i = 0; i < dokumentasiBaruUris.size(); i++) {
            Uri uri = dokumentasiBaruUris.get(i);
            View item = LayoutInflater.from(getContext()).inflate(R.layout.item_dokumentasi_preview, layoutDokumentasiContainer, false);
            ImageView iv = item.findViewById(R.id.ivDokumentasi);
            TextView tv = item.findViewById(R.id.tvLabel);
            tv.setText("Baru " + (i + 1));

            Glide.with(this).load(uri).into(iv);

            int finalIndex = i;
            item.findViewById(R.id.btnHapus).setOnClickListener(v -> {
                dokumentasiBaruUris.remove(uri);
                if (finalIndex < dokumentasiBaruFiles.size()) {
                    dokumentasiBaruFiles.remove(finalIndex);
                }
                layoutDokumentasiContainer.removeView(item);
            });

            layoutDokumentasiContainer.addView(item);
        }
    }

    private void tambahPreviewDokumentasi(String fileName, boolean isLama) {
        View item = LayoutInflater.from(getContext()).inflate(R.layout.item_dokumentasi_preview, layoutDokumentasiContainer, false);
        ImageView iv = item.findViewById(R.id.ivDokumentasi);
        TextView tv = item.findViewById(R.id.tvLabel);
        tv.setText((isLama ? "Lama: " : "Baru: ") + fileName);

        // ✅ BASE URL TANPA SPASI
        String baseUrl = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/kegiatan/";
        String url = baseUrl + fileName.trim();
        Glide.with(this).load(url).placeholder(R.drawable.default_image).into(iv);

        if (isLama) {
            item.findViewById(R.id.btnHapus).setOnClickListener(v -> {
                dokumentasiLamaList.remove(fileName);
                layoutDokumentasiContainer.removeView(item);
            });
        }

        layoutDokumentasiContainer.addView(item);
    }

    private void tambahDokumentasiBaru(Uri uri) {
        dokumentasiBaruUris.add(uri);
        try {
            File file = createCompressedFile(uri, "edit_doc_" + dokumentasiBaruUris.size());
            dokumentasiBaruFiles.add(file);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Gagal proses gambar", Toast.LENGTH_SHORT).show();
        }
        tampilkanDokumentasi();
    }

    // ✅ ✅ ✅ KOMPRESI OTOMATIS ≤ 1.8 MB
    private File createCompressedFile(Uri uri, String prefix) throws IOException {
        Bitmap bitmap = decodeSampledBitmapFromUri(uri, 1200, 1200);
        File dir = getContext().getCacheDir();
        File file = new File(dir, prefix + "_" + System.currentTimeMillis() + ".jpg");

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
        BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri), null, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri), null, options);
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

    private void simpanPerubahan() {
        String judul = etJudul.getText().toString().trim();
        String lokasi = etLokasi.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String videoInput = etVideo.getText().toString().trim();

        if (TextUtils.isEmpty(judul) || TextUtils.isEmpty(lokasi) || TextUtils.isEmpty(tanggal) || TextUtils.isEmpty(deskripsi)) {
            Toast.makeText(getContext(), "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!tanggal.contains(" ")) {
            tanggal += " 00:00:00";
        }

        // ✅ Kirim video apa adanya (string mentah)
        String videoFinal = videoInput;

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_event", acara.getIdEvent())
                .addFormDataPart("nama_event", judul)
                .addFormDataPart("lokasi_event", lokasi)
                .addFormDataPart("tanggal_event", tanggal)
                .addFormDataPart("deskripsi_event", deskripsi)
                .addFormDataPart("username", "Admin")
                .addFormDataPart("video_urls", videoFinal); // ✅

        // Gambar utama
        if (gambarBaruUri != null) {
            try {
                File file = createCompressedFile(gambarBaruUri, "edit_main");
                builder.addFormDataPart("gambar_event", file.getName(),
                        RequestBody.create(file, MediaType.parse("image/jpeg")));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Gagal proses gambar utama", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Dokumentasi lama
        for (String fileName : dokumentasiLamaList) {
            builder.addFormDataPart("dokumentasi_lama[]", fileName);
        }

        // Dokumentasi baru
        for (int i = 0; i < dokumentasiBaruFiles.size(); i++) {
            File file = dokumentasiBaruFiles.get(i);
            builder.addFormDataPart("dokumentasi_baru[]", file.getName(),
                    RequestBody.create(file, MediaType.parse("image/jpeg")));
        }

        RequestBody requestBody = builder.build();

        // ✅ URL TANPA SPASI
        String url = "https://masnurhudanganjuk.pbltifnganjuk.com/API/api_ubah_acara.php";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        btnSimpan.setEnabled(false);
        btnSimpan.setText("Menyimpan...");

        new OkHttpClient().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                requireActivity().runOnUiThread(() -> {
                    btnSimpan.setEnabled(true);
                    btnSimpan.setText("Simpan");
                    Toast.makeText(getContext(), "❌ Jaringan error", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String resp = response.body() != null ? response.body().string() : "{}";
                requireActivity().runOnUiThread(() -> {
                    btnSimpan.setEnabled(true);
                    btnSimpan.setText("Simpan");

                    boolean isSuccess = resp.contains("\"status\":\"success\"") || resp.contains("\"status\":\"1\"");
                    if (isSuccess) {
                        Toast.makeText(getContext(), "✅ Perubahan berhasil disimpan", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                    } else {
                        String msg = "Gagal";
                        int idx = resp.indexOf("\"message\":\"");
                        if (idx != -1) {
                            int end = resp.indexOf("\"", idx + 11);
                            if (end != -1) msg = resp.substring(idx + 11, end);
                        }
                        Toast.makeText(getContext(), "❗ " + msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}