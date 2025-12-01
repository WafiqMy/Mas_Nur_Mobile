package com.example.masnur.Fitur_Informasi_Masjid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInformasiMasjid extends Fragment {

    private ImageView imgMasjid;
    private TextView tvJudul, tvDeskripsi;
    private Button btnKelola;
    private ApiService apiService;
    private ProfilMasjidModel data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_informasi_masjid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgMasjid = view.findViewById(R.id.imgMasjid);
        tvJudul = view.findViewById(R.id.tvJudulSejarah);
        tvDeskripsi = view.findViewById(R.id.tvDeskripsiSejarah);
        btnKelola = view.findViewById(R.id.btnKelolaSejarah);

        apiService = ApiClient.getService();

        // ✅ Tampilkan placeholder langsung — jangan biarkan blank
        tvJudul.setText("Memuat...");
        tvDeskripsi.setText("Tunggu sebentar...");
        imgMasjid.setImageResource(R.drawable.default_image);

        loadProfilMasjid();

        btnKelola.setOnClickListener(v -> {
            if (data != null) {
                Intent intent = new Intent(requireActivity(), EditProfilMasjidActivity.class);
                intent.putExtra("profil", data);
                startActivityForResult(intent, 200);
            } else {
                Toast.makeText(requireContext(), "Data belum siap", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfilMasjid() {
        apiService.getProfilMasjid().enqueue(new Callback<ProfilMasjidResponse>() {
            @Override
            public void onResponse(Call<ProfilMasjidResponse> call, Response<ProfilMasjidResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfilMasjidResponse res = response.body();
                    if ("success".equals(res.getStatus()) && res.getData() != null) {
                        data = res.getData();
                        tampilkanData();
                    } else {
                        Toast.makeText(requireContext(), res.getMessage() != null ? res.getMessage() : "Data tidak tersedia", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfilMasjidResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tampilkanData() {
        tvJudul.setText(data.getJudulSejarah() != null ? data.getJudulSejarah() : "–");
        tvDeskripsi.setText(data.getDeskripsiSejarah() != null ? data.getDeskripsiSejarah() : "–");

        // ✅ Prioritas 1: ambil URL dari API (gambar_sejarah_masjid_url)
        String imageUrl = data.getGambarSejarahMasjidUrl();

        // ✅ Prioritas 2: fallback ke build manual
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            String fileName = data.getGambarSejarahMasjid();
            if (fileName != null && !fileName.trim().isEmpty()) {
                imageUrl = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/" + fileName;
            }
        }

        // ✅ Prioritas 3: default
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            imageUrl = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/default_placeholder.png";
        }

        // ✅ Load dengan Glide — cache aktif!
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .override(600, 400)
                .centerCrop()
                .into(imgMasjid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == getActivity().RESULT_OK) {
            // ✅ Terima data hasil edit langsung — SATSET!
            ProfilMasjidModel updated = data.getParcelableExtra("updated_profil");
            if (updated != null) {
                this.data = updated;
                tampilkanData(); // langsung update UI
            } else {
                // Fallback: reload API
                loadProfilMasjid();
            }
        }
    }
}