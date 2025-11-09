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

public class UbahGambarSejarahFragment extends Fragment {

    private ImageView imgLama, imgBaru;
    private Button btnUnggah, btnKembali;
    private DatabaseHelper dbHelper;
    private byte[] imageBytes;
    private int sejarahId = -1;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;

    public UbahGambarSejarahFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ubah_gambar_sejarah, container, false);

        imgLama = view.findViewById(R.id.imgLama);
        imgBaru = view.findViewById(R.id.imgBaru);
        btnUnggah = view.findViewById(R.id.btnUnggah);
        btnKembali = view.findViewById(R.id.btnKembali);

        dbHelper = new DatabaseHelper(getActivity());

        // 🔹 Tampilkan gambar lama dari database
        loadGambarLama();

        // 🔹 Klik kotak gambar baru untuk pilih gambar (kamera/galeri)
        imgBaru.setOnClickListener(v -> showImagePicker());

        // 🔹 Tombol unggah → simpan gambar baru ke database
        btnUnggah.setOnClickListener(v -> saveNewImage());

        // 🔹 Tombol kembali → balik ke halaman Kelola Sejarah
        btnKembali.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    .replace(R.id.fragment_container, new KelolaSejarahFragment())
                    .commit();
        });

        return view;
    }

    private void loadGambarLama() {
        Cursor cursor = dbHelper.getSejarah();
        if (cursor.moveToFirst()) {
            sejarahId = cursor.getInt(0);
            byte[] img = cursor.getBlob(3);
            if (img != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                imgLama.setImageBitmap(bitmap);
            }
        }
        cursor.close();
    }

    private void showImagePicker() {
        String[] options = {"Kamera", "Galeri"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pilih Sumber Gambar")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, REQUEST_CAMERA);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, REQUEST_GALLERY);
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = null;
            if (requestCode == REQUEST_CAMERA && data.getExtras() != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
            } else if (requestCode == REQUEST_GALLERY) {
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap != null) {
                imgBaru.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();
            }
        }
    }

    private void saveNewImage() {
        if (sejarahId == -1) {
            Toast.makeText(getActivity(), "Data sejarah tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageBytes == null) {
            Toast.makeText(getActivity(), "Silakan pilih gambar baru terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.updateGambarSejarah(sejarahId, imageBytes);
        if (success) {
            Toast.makeText(getActivity(), "Gambar berhasil diperbarui", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new KelolaSejarahFragment())
                    .commit();
        } else {
            Toast.makeText(getActivity(), "Gagal memperbarui gambar", Toast.LENGTH_SHORT).show();
        }
    }
}
