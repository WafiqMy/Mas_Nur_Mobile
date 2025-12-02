package com.example.masnur.Fitur_Persewaan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.masnur.R;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FragmentKelolaPersewaan extends Fragment {

    private LinearLayout layoutGedung, layoutMultimedia, layoutMusik;
    private ApiService apiService;
    private List<BarangModel> barangList = new ArrayList<>();

    public FragmentKelolaPersewaan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelola_pemesanan, container, false);

        layoutGedung = view.findViewById(R.id.layoutGedung);
        layoutMultimedia = view.findViewById(R.id.layoutMultimedia);
        layoutMusik = view.findViewById(R.id.layoutMusik);

        Button btnTambah = view.findViewById(R.id.btnTambahBarang);
        btnTambah.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TambahBarangActivity.class);
            startActivityForResult(intent, 100);
        });

        apiService = ApiClient.getService();
        loadBarangData();

        return view;
    }

    private void loadBarangData() {
        apiService.getBarang().enqueue(new Callback<List<BarangModel>>() {
            @Override
            public void onResponse(Call<List<BarangModel>> call, Response<List<BarangModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    barangList = response.body();
                    tampilkanBarang();
                } else {
                    Toast.makeText(getContext(), "Gagal mengambil data barang", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BarangModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("BarangAPI", "onFailure: ", t);
            }
        });
    }

    private void tampilkanBarang() {
        layoutGedung.removeAllViews();
        layoutMultimedia.removeAllViews();
        layoutMusik.removeAllViews();

        List<BarangModel> gedung = new ArrayList<>();
        List<BarangModel> multimedia = new ArrayList<>();
        List<BarangModel> musik = new ArrayList<>();

        for (BarangModel barang : barangList) {
            String jenis = barang.getJenis().toLowerCase();
            if (jenis.contains("gedung")) {
                gedung.add(barang);
            } else if (jenis.contains("multimedia")) {
                multimedia.add(barang);
            } else if (jenis.contains("musik")) {
                musik.add(barang);
            }
        }

        for (BarangModel b : gedung) layoutGedung.addView(buatItemView(b));
        for (BarangModel b : multimedia) layoutMultimedia.addView(buatItemView(b));
        for (BarangModel b : musik) layoutMusik.addView(buatItemView(b));
    }

    private View buatItemView(BarangModel barang) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_barang_horizontal, null);

        TextView textNama = view.findViewById(R.id.textNamaBarang);
        TextView textHarga = view.findViewById(R.id.textHarga);
        TextView textJumlah = view.findViewById(R.id.textJumlah);
        ImageView imageBarang = view.findViewById(R.id.imageBarang);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnHapus = view.findViewById(R.id.btnHapus);

        textNama.setText(barang.getNamaBarang());
        textHarga.setText("Rp " + String.format("%,d", barang.getHarga()).replace(',', '.'));
        textJumlah.setText("Stok: " + barang.getJumlah());

        // ✅ Perbaiki URL: hapus spasi ekstra
        String url = "https://masnurhudanganjuk.pbltifnganjuk.com/API/get_gambar.php?file=" + barang.getGambar();
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .centerCrop()
                .into(imageBarang);

        // ✅ Ubah "Edit" → "Detail", lalu buka DetailBarangFragment
        btnEdit.setText("Detail");
        btnEdit.setOnClickListener(v -> {
            Fragment fragment = DetailBarangFragment.newInstance(barang);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnHapus.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus " + barang.getNamaBarang() + "?")
                    .setPositiveButton("Ya", (d, w) -> hapusBarang(barang.getIdPersewaan()))
                    .setNegativeButton("Batal", null)
                    .show();
        });

        return view;
    }

    private void hapusBarang(int id) {
        ProgressDialog dialog = ProgressDialog.show(getContext(), "Menghapus...", "Mohon tunggu", true);
        apiService.hapusBarang(id).enqueue(new Callback<ReservasiResponse>() {
            @Override
            public void onResponse(Call<ReservasiResponse> call, Response<ReservasiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Barang dihapus", Toast.LENGTH_SHORT).show();
                    loadBarangData();
                } else {
                    Toast.makeText(getContext(), "Gagal hapus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReservasiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100 || requestCode == 101) && resultCode == RESULT_OK) {
            loadBarangData();
        }
    }
}