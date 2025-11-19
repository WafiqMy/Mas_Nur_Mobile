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
        loadProfilMasjid();

        btnKelola.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EditProfilMasjidActivity.class);
            intent.putExtra("profil", data);
            startActivityForResult(intent, 200);
        });
    }

    private void loadProfilMasjid() {
        ProgressDialog dialog = ProgressDialog.show(requireActivity(), "Memuat...", "Mohon tunggu", true);

        apiService.getProfilMasjid().enqueue(new Callback<ProfilMasjidResponse>() {
            @Override
            public void onResponse(Call<ProfilMasjidResponse> call, Response<ProfilMasjidResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    ProfilMasjidResponse res = response.body();
                    if ("success".equals(res.getStatus()) && res.getData() != null) {
                        data = res.getData();
                        tampilkanData();
                    } else {
                        Toast.makeText(requireContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfilMasjidResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void tampilkanData() {
        tvJudul.setText(data.getJudulSejarah());
        tvDeskripsi.setText(data.getDeskripsiSejarah());

        String fileName = data.getGambarSejarahMasjid();
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = "default_placeholder.png";
        }
        String url = "http://masnurhuda.atwebpages.com/API/api_gambar_profil_masjid.php?file_name=" + Uri.encode(fileName);

        Glide.with(requireActivity())  // ✅ pakai requireActivity()
                .load(url)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.ic_launcher_background)
                .into(imgMasjid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == getActivity().RESULT_OK) {
            // Refresh data
            loadProfilMasjid();
        }
    }
}