package com.example.masnur.profilmasjid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.profilmasjid.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UbahGambarStrukturFragment extends Fragment {

    private ImageView imgLama, imgBaru;
    private Button btnUnggah, btnKembali;
    private DatabaseHelper dbHelper;
    private byte[] gambarBaru;
    private int strukturId = -1;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;

    public UbahGambarStrukturFragment() {
        // Konstruktor kosong
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ubah_gambar_struktur, container, false);

        imgLama = view.findViewById(R.id.imgLama);
        imgBaru = view.findViewById(R.id.imgBaru);
        btnUnggah = view.findViewById(R.id.btnUnggah);
        btnKembali = view.findViewById(R.id.btnKembali);

        dbHelper = new DatabaseHelper(getActivity());

        // Tampilkan gambar lama (jika ada)
        loadDataStruktur();

        // Klik pada kotak gambar baru → buka pilihan kamera/galeri
        imgBaru.setOnClickListener(v -> showImagePicker());

        // Tombol unggah gambar baru
        btnUnggah.setOnClickListener(v -> saveOrUpdateGambar());

        // Tombol kembali → ke StrukturFragment
        btnKembali.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.fragment_container, new StrukturFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    // ========================
    // 🔹 Load gambar lama
    // ========================
    private void loadDataStruktur() {
        Cursor cursor = dbHelper.getStruktur();
        if (cursor != null && cursor.moveToFirst()) {
            strukturId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            byte[] imgData = cursor.getBlob(cursor.getColumnIndexOrThrow("gambar"));

            if (imgData != null && imgData.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                imgLama.setImageBitmap(bitmap);
            } else {
                imgLama.setImageResource(android.R.color.transparent);
            }
            cursor.close();
        } else {
            imgLama.setImageResource(android.R.color.transparent);
        }
    }

    // ========================
    // 🔹 Pilih gambar (kamera/galeri)
    // ========================
    private void showImagePicker() {
        String[] options = {"Ambil dari Kamera", "Pilih dari Galeri"};
        new AlertDialog.Builder(getActivity())
                .setTitle("Pilih Sumber Gambar")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, REQUEST_CAMERA);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, REQUEST_GALLERY);
                    }
                })
                .show();
    }

    // ========================
    // 🔹 Proses hasil gambar
    // ========================
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = null;

            try {
                if (requestCode == REQUEST_GALLERY) {
                    Uri imageUri = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                } else if (requestCode == REQUEST_CAMERA) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                }

                if (bitmap != null) {
                    imgBaru.setImageBitmap(bitmap);
                    gambarBaru = convertBitmapToByteArray(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ========================
    // 🔹 Simpan/Update gambar
    // ========================
    private void saveOrUpdateGambar() {
        if (gambarBaru == null) {
            Toast.makeText(getActivity(), "Silakan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated;
        if (strukturId != -1) {
            updated = dbHelper.updateGambarStruktur(strukturId, gambarBaru);
        } else {
            updated = dbHelper.insertStruktur(gambarBaru);
        }

        if (updated) {
            Toast.makeText(getActivity(), "Gambar berhasil disimpan", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show();
        }
    }

    // ========================
    // 🔹 Ubah Bitmap → byte[]
    // ========================
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
