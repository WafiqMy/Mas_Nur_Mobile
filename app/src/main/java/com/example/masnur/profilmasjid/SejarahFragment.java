package com.example.masnur.profilmasjid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SejarahFragment extends Fragment {

    public SejarahFragment() {
        // Konstruktor kosong diperlukan
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Hubungkan ke layout fragment_sejarah_masjid.xml
        View view = inflater.inflate(R.layout.fragment_sejarah, container, false);

        Button btnKelola = view.findViewById(R.id.btnKelolaSejarah);
        Button btnHapus = view.findViewById(R.id.btnHapusSejarah);

        // Aksi tombol (sementara tampilkan pesan)
        btnKelola.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Kelola Sejarah Masjid diklik", Toast.LENGTH_SHORT).show()
        );

        btnHapus.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Hapus Sejarah Masjid diklik", Toast.LENGTH_SHORT).show()
        );

        return view;
    }
}
