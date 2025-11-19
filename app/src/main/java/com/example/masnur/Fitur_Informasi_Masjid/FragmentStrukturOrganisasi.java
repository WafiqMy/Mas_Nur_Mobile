// com.example.masnur.Fitur_Informasi_Masjid.FragmentStrukturOrganisasi.java
package com.example.masnur.Fitur_Informasi_Masjid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
        loadStrukturData();

        btnUbahStruktur.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EditStrukturOrganisasiActivity.class);
            intent.putExtra("struktur", data);
            startActivityForResult(intent, 300);
        });

        btnHapusStruktur.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Fitur hapus belum tersedia", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadStrukturData() {
        ProgressDialog dialog = ProgressDialog.show(requireActivity(), "Memuat...", "Mohon tunggu", true);

        apiService.getStrukturOrganisasi().enqueue(new Callback<StrukturOrganisasiResponse>() {
            @Override
            public void onResponse(Call<StrukturOrganisasiResponse> call, Response<StrukturOrganisasiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    StrukturOrganisasiResponse res = response.body();
                    if ("success".equals(res.getStatus()) && res.getData() != null) {
                        data = res.getData();
                        tampilkanGambar();
                    } else {
                        Toast.makeText(requireContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StrukturOrganisasiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void tampilkanGambar() {
        // Struktur Pengurus
        String fileNamePengurus = data.getGambarStrukturOrganisasi();
        if (fileNamePengurus == null || fileNamePengurus.trim().isEmpty()) {
            txtNoImagePengurus.setVisibility(View.VISIBLE);
            imgStrukturPengurus.setImageDrawable(null);
        } else {
            txtNoImagePengurus.setVisibility(View.GONE);
            String urlPengurus = "http://masnurhuda.atwebpages.com/API/api_gambar_profil_masjid.php?file_name=" + Uri.encode(fileNamePengurus);
            Glide.with(requireActivity())
                    .load(urlPengurus)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.ic_launcher_background)
                    .into(imgStrukturPengurus);
        }

        // Struktur Remas
        String fileNameRemas = data.getGambarStrukturRemas();
        if (fileNameRemas == null || fileNameRemas.trim().isEmpty()) {
            txtNoImageRemas.setVisibility(View.VISIBLE);
            imgStrukturRemas.setImageDrawable(null);
        } else {
            txtNoImageRemas.setVisibility(View.GONE);
            String urlRemas = "http://masnurhuda.atwebpages.com/API/api_gambar_profil_masjid.php?file_name=" + Uri.encode(fileNameRemas);
            Glide.with(requireActivity())
                    .load(urlRemas)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.ic_launcher_background)
                    .into(imgStrukturRemas);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == getActivity().RESULT_OK) {
            loadStrukturData(); // Refresh
        }
    }
}