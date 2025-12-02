package com.example.masnur.Fitur_Persewaan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.masnur.R;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class DetailBarangFragment extends Fragment {

    private BarangModel barang;
    private TextView tvNama, tvJenis, tvHarga, tvJumlah, tvDeskripsi, tvSpesifikasi, tvFasilitas;
    private ImageView imgGambar;
    private Button btnEdit, btnKembali;

    public static DetailBarangFragment newInstance(BarangModel barang) {
        DetailBarangFragment fragment = new DetailBarangFragment();
        Bundle args = new Bundle();
        args.putParcelable("barang", barang);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            barang = getArguments().getParcelable("barang");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_barang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNama = view.findViewById(R.id.tvNama);
        tvJenis = view.findViewById(R.id.tvJenis);
        tvHarga = view.findViewById(R.id.tvHarga);
        tvJumlah = view.findViewById(R.id.tvJumlah);
        tvDeskripsi = view.findViewById(R.id.tvDeskripsi);
        tvSpesifikasi = view.findViewById(R.id.tvSpesifikasi);
        tvFasilitas = view.findViewById(R.id.tvFasilitas);
        imgGambar = view.findViewById(R.id.imgGambar);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnKembali = view.findViewById(R.id.btnKembali);

        if (barang != null) {
            bindData();
        } else {
            Toast.makeText(getContext(), "Data barang tidak ditemukan", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditBarangActivity.class);
            intent.putExtra("barang", barang);
            startActivityForResult(intent, 101);
        });

        btnKembali.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void bindData() {
        tvNama.setText(barang.getNamaBarang());
        tvJenis.setText("Jenis: " + barang.getJenis());
        tvHarga.setText("Harga: Rp " + String.format("%,d", barang.getHarga()).replace(',', '.'));
        tvJumlah.setText("Stok: " + barang.getJumlah());

        tvDeskripsi.setText(barang.getDeskripsi() != null && !barang.getDeskripsi().isEmpty() ? barang.getDeskripsi() : "-");
        tvSpesifikasi.setText(barang.getSpesifikasi() != null && !barang.getSpesifikasi().isEmpty() ? barang.getSpesifikasi() : "-");
        tvFasilitas.setText(barang.getFasilitas() != null && !barang.getFasilitas().isEmpty() ? barang.getFasilitas() : "-");

        String url = "https://masnurhudanganjuk.pbltifnganjuk.com/API/get_gambar.php?file=" + barang.getGambar();
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .centerCrop()
                .into(imgGambar);
    }
}