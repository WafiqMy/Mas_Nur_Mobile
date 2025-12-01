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

public class FragmentStrukturOrganisasi extends Fragment {

    private ImageView imgStrukturPengurus, imgStrukturRemas;
    private TextView txtNoImagePengurus, txtNoImageRemas;
    private Button btnUbahStruktur, btnHapusStruktur;
    private ApiService apiService;
    private StrukturOrganisasiModel data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_struktur, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgStrukturPengurus = view.findViewById(R.id.imgStrukturPengurus);
        imgStrukturRemas = view.findViewById(R.id.imgStrukturRemas);
        txtNoImagePengurus = view.findViewById(R.id.txtNoImagePengurus);
        txtNoImageRemas = view.findViewById(R.id.txtNoImageRemas);
        btnUbahStruktur = view.findViewById(R.id.btnUbahStruktur);
        btnHapusStruktur = view.findViewById(R.id.btnHapusStruktur);

        apiService = ApiClient.getService();

        // ✅ Tampilkan placeholder langsung
        imgStrukturPengurus.setImageResource(R.drawable.default_image);
        imgStrukturRemas.setImageResource(R.drawable.default_image);
        txtNoImagePengurus.setText("Memuat...");
        txtNoImageRemas.setText("Memuat...");

        loadStrukturData();

        btnUbahStruktur.setOnClickListener(v -> {
            if (data != null) {
                Intent intent = new Intent(requireActivity(), EditStrukturOrganisasiActivity.class);
                intent.putExtra("struktur", data);
                startActivityForResult(intent, 300);
            } else {
                Toast.makeText(requireContext(), "Data belum siap", Toast.LENGTH_SHORT).show();
            }
        });

        btnHapusStruktur.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Fitur hapus belum tersedia", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadStrukturData() {
        apiService.getStrukturOrganisasi().enqueue(new Callback<StrukturOrganisasiResponse>() {
            @Override
            public void onResponse(Call<StrukturOrganisasiResponse> call, Response<StrukturOrganisasiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StrukturOrganisasiResponse res = response.body();
                    if ("success".equals(res.getStatus()) && res.getData() != null) {
                        data = res.getData();
                        tampilkanGambarCepat();
                    } else {
                        Toast.makeText(requireContext(), res.getMessage() != null ? res.getMessage() : "Data tidak tersedia", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StrukturOrganisasiResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tampilkanGambarCepat() {
        // ——— PENGURUS ———
        String urlPengurus = data.getGambarStrukturOrganisasiUrl();
        if (urlPengurus == null || urlPengurus.trim().isEmpty()) {
            String fn = data.getGambarStrukturOrganisasi();
            if (fn != null && !fn.trim().isEmpty()) {
                urlPengurus = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/" + fn;
            }
        }
        if (urlPengurus == null || urlPengurus.trim().isEmpty()) {
            urlPengurus = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/default_placeholder.png";
        }

        Glide.with(this)
                .load(urlPengurus)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .override(600, 800)
                .centerCrop()
                .into(imgStrukturPengurus);

        // ——— REMAS ———
        String urlRemas = data.getGambarStrukturRemasUrl();
        if (urlRemas == null || urlRemas.trim().isEmpty()) {
            String fn = data.getGambarStrukturRemas();
            if (fn != null && !fn.trim().isEmpty()) {
                urlRemas = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/" + fn;
            }
        }
        if (urlRemas == null || urlRemas.trim().isEmpty()) {
            urlRemas = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/profil_masjid/default_placeholder.png";
        }

        Glide.with(this)
                .load(urlRemas)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .override(600, 800)
                .centerCrop()
                .into(imgStrukturRemas);

        // Sembunyikan teks placeholder
        txtNoImagePengurus.setVisibility(View.GONE);
        txtNoImageRemas.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == getActivity().RESULT_OK) {
            // ✅ Terima data hasil edit langsung — SATSET!
            StrukturOrganisasiModel updated = data.getParcelableExtra("updated_struktur");
            if (updated != null) {
                this.data = updated;
                tampilkanGambarCepat(); // langsung update UI
            } else {
                loadStrukturData(); // fallback
            }
        }
    }
}