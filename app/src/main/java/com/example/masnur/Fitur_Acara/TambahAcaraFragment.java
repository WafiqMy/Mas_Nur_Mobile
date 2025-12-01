package com.example.masnur.Fitur_Acara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.masnur.Api.ApiClient;
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

public class TambahAcaraFragment extends Fragment {

    private EditText etJudul, etLokasi, etTanggal, etDeskripsi, etVideo;
    private ImageView imgPreview;
    private LinearLayout layoutPlaceholder, layoutDokumentasiContainer;
    private Button btnUnggah, btnTambahDokumentasi;
    private Uri gambarUtamaUri;
    private List<Uri> dokumentasiUris = new ArrayList<>();
    private List<File> dokumentasiFiles = new ArrayList<>();

    private ActivityResultLauncher<Intent> singleImageLauncher;
    private ActivityResultLauncher<Intent> multiImageLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        singleImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        gambarUtamaUri = result.getData().getData();
                        if (gambarUtamaUri != null) {
                            imgPreview.setImageURI(gambarUtamaUri);
                            imgPreview.setVisibility(View.VISIBLE);
                            layoutPlaceholder.setVisibility(View.GONE);
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
                                tambahDokumentasi(uri);
                            }
                        } else if (data.getData() != null) {
                            tambahDokumentasi(data.getData());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tambah_acara, container, false);

        etJudul = view.findViewById(R.id.et_judul);
        etLokasi = view.findViewById(R.id.et_lokasi);
        etTanggal = view.findViewById(R.id.et_tanggal);
        etDeskripsi = view.findViewById(R.id.et_deskripsi);
        etVideo = view.findViewById(R.id.et_video);
        imgPreview = view.findViewById(R.id.imgPreview);
        layoutPlaceholder = view.findViewById(R.id.layoutPlaceholder);
        layoutDokumentasiContainer = view.findViewById(R.id.layoutDokumentasiContainer);
        btnUnggah = view.findViewById(R.id.btnUnggah);
        btnTambahDokumentasi = view.findViewById(R.id.btnTambahDokumentasi);

        view.findViewById(R.id.layout_upload_poster).setOnClickListener(v -> {
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

        btnUnggah.setOnClickListener(v -> simpanAcara());

        return view;
    }

    private void tambahDokumentasi(Uri uri) {
        dokumentasiUris.add(uri);
        View item = LayoutInflater.from(getContext()).inflate(R.layout.item_dokumentasi_preview, layoutDokumentasiContainer, false);
        ImageView iv = item.findViewById(R.id.ivDokumentasi);
        TextView tv = item.findViewById(R.id.tvLabel);
        tv.setText("Foto " + dokumentasiUris.size());

        Glide.with(this).load(uri).into(iv);

        item.findViewById(R.id.btnHapus).setOnClickListener(v -> {
            int index = layoutDokumentasiContainer.indexOfChild(item);
            dokumentasiUris.remove(index);
            dokumentasiFiles.remove(index);
            layoutDokumentasiContainer.removeView(item);
        });

        layoutDokumentasiContainer.addView(item);

        try {
            File file = createCompressedFile(uri, "doc_" + dokumentasiUris.size());
            dokumentasiFiles.add(file);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Gagal proses gambar dokumentasi", Toast.LENGTH_SHORT).show();
        }
    }

    private File createCompressedFile(Uri uri, String prefix) throws IOException {
        Bitmap bitmap = decodeSampledBitmapFromUri(uri, 800, 800);
        File dir = getContext().getCacheDir();
        File file = new File(dir, prefix + "_" + System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int quality = 85;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        while (stream.size() > 1000 * 1024 && quality > 40) {
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

    private void simpanAcara() {
        String judul = etJudul.getText().toString().trim();
        String lokasi = etLokasi.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String videoInput = etVideo.getText().toString().trim();

        if (judul.isEmpty() || lokasi.isEmpty() || tanggal.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(getContext(), "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!tanggal.contains(" ")) {
            tanggal += " 00:00:00";
        }

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("nama_event", judul)
                .addFormDataPart("lokasi_event", lokasi)
                .addFormDataPart("tanggal_event", tanggal)
                .addFormDataPart("deskripsi_event", deskripsi)
                .addFormDataPart("username", "Admin")
                .addFormDataPart("video_urls", videoInput);

        if (gambarUtamaUri != null) {
            try {
                File file = createCompressedFile(gambarUtamaUri, "event_main");
                builder.addFormDataPart("gambar_event", file.getName(),
                        RequestBody.create(file, MediaType.parse("image/jpeg")));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Gagal proses gambar utama", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        for (int i = 0; i < dokumentasiFiles.size(); i++) {
            File file = dokumentasiFiles.get(i);
            builder.addFormDataPart("dokumentasi[]", file.getName(),
                    RequestBody.create(file, MediaType.parse("image/jpeg")));
        }

        RequestBody requestBody = builder.build();

        // ✅ HAPUS SPASI DI URL
        Request request = new Request.Builder()
                .url("https://masnurhudanganjuk.pbltifnganjuk.com/API/api_tambah_acara.php")
                .post(requestBody)
                .build();

        btnUnggah.setEnabled(false);
        btnUnggah.setText("Menyimpan...");

        new OkHttpClient().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                requireActivity().runOnUiThread(() -> {
                    btnUnggah.setEnabled(true);
                    btnUnggah.setText("Unggah");
                    Toast.makeText(getContext(), "Jaringan error", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String resp = response.body().string();
                requireActivity().runOnUiThread(() -> {
                    btnUnggah.setEnabled(true);
                    btnUnggah.setText("Unggah");
                    if (resp.contains("\"status\":\"success\"")) {
                        Toast.makeText(getContext(), "✓ Acara berhasil ditambah", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                    } else {
                        String msg = "Gagal";
                        int idx = resp.indexOf("\"message\":\"");
                        if (idx != -1) {
                            int end = resp.indexOf("\"", idx + 11);
                            if (end != -1) msg = resp.substring(idx + 11, end);
                        }
                        Toast.makeText(getContext(), "✗ " + msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}