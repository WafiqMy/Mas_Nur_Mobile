package com.example.masnur.profilmasjid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class StrukturFragment extends Fragment {

    public StrukturFragment() {
        // Konstruktor kosong
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Hubungkan ke layout fragment_struktur_organisasi.xml
        View view = inflater.inflate(R.layout.fragment_struktur, container, false);

        Button btnUbah = view.findViewById(R.id.btnUbahStruktur);
        Button btnHapus = view.findViewById(R.id.btnHapusStruktur);

        // Aksi tombol sementara (testing)
        btnUbah.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Ubah Gambar Struktur diklik", Toast.LENGTH_SHORT).show()
        );

        btnHapus.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Hapus Gambar Struktur diklik", Toast.LENGTH_SHORT).show()
        );

        return view;
    }
}
