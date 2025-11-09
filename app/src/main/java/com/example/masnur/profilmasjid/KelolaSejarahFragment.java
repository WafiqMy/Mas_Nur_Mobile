package com.example.masnur.profilmasjid;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.profilmasjid.database.DatabaseHelper;

public class KelolaSejarahFragment extends Fragment {

    private ImageView imgSejarah;
    private EditText edtJudul, edtDeskripsi;
    private Button btnUbah, btnSimpan, btnKembali;
    private DatabaseHelper dbHelper;
    private byte[] imageBytes;
    private int sejarahId = -1;

    public KelolaSejarahFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelola_sejarah, container, false);

        imgSejarah = view.findViewById(R.id.imgSejarah);
        edtJudul = view.findViewById(R.id.edtJudul);
        edtDeskripsi = view.findViewById(R.id.edtDeskripsi);
        btnUbah = view.findViewById(R.id.btnUbahGambar);
        btnSimpan = view.findViewById(R.id.btnSimpanPerubahan);
        btnKembali = view.findViewById(R.id.btnKembali);

        dbHelper = new DatabaseHelper(getActivity());
        loadData();

        // Navigasi ke halaman Ubah Gambar
        btnUbah.setOnClickListener(v -> {
            Fragment ubahGambarFragment = new UbahGambarSejarahFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container, ubahGambarFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Kembali ke halaman utama (Sejarah)
        btnKembali.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    .replace(R.id.fragment_container, new SejarahFragment())
                    .commit();
        });

        // Simpan ke database
        btnSimpan.setOnClickListener(v -> saveChanges());

        return view;
    }

    private void loadData() {
        Cursor cursor = dbHelper.getSejarah();
        if (cursor.moveToFirst()) {
            sejarahId = cursor.getInt(0);
            edtJudul.setText(cursor.getString(1));
            edtDeskripsi.setText(cursor.getString(2));
            byte[] img = cursor.getBlob(3);
            if (img != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                imgSejarah.setImageBitmap(bitmap);
            }
        }
        cursor.close();
    }

    private void saveChanges() {
        String judul = edtJudul.getText().toString().trim();
        String deskripsi = edtDeskripsi.getText().toString().trim();

        if (judul.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(getActivity(), "Isi semua kolom terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success;
        if (sejarahId == -1)
            success = dbHelper.insertSejarah(judul, deskripsi, imageBytes);
        else
            success = dbHelper.updateSejarah(sejarahId, judul, deskripsi, imageBytes);

        if (success) {
            Toast.makeText(getActivity(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SejarahFragment())
                    .commit();
        } else {
            Toast.makeText(getActivity(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
        }
    }
}
