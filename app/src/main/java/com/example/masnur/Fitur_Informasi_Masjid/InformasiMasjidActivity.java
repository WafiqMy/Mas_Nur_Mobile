package com.example.masnur.Fitur_Informasi_Masjid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformasiMasjidActivity extends AppCompatActivity {

    private ImageView imgMasjid;
    private TextView tvJudul, tvDeskripsi;
    private Button btnKelola, btnKembali;

    private ApiService apiService;
    private ProfilMasjidModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_masjid_detail); // ✅ layout baru

        initViews();
        apiService = ApiClient.getService();

        loadProfilMasjid();
    }

    private void initViews() {
        imgMasjid = findViewById(R.id.imgMasjid);
        tvJudul = findViewById(R.id.tvJudulSejarah);
        tvDeskripsi = findViewById(R.id.tvDeskripsiSejarah);
        btnKelola = findViewById(R.id.btnKelolaSejarah);
        btnKembali = findViewById(R.id.btnKembali);

        btnKelola.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfilMasjidActivity.class);
            intent.putExtra("profil", data);
            startActivityForResult(intent, 200);
        });

        btnKembali.setOnClickListener(v -> finish());
    }

    private void loadProfilMasjid() {
        ProgressDialog dialog = ProgressDialog.show(this, "Memuat...", "Mohon tunggu", true);

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
                        Toast.makeText(InformasiMasjidActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InformasiMasjidActivity.this, "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfilMasjidResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(InformasiMasjidActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void tampilkanData() {
        tvJudul.setText(data.getJudulSejarah());
        tvDeskripsi.setText(data.getDeskripsiSejarah());

        String url = "http://masnurhuda.atwebpages.com/API/api_gambar_profil_masjid.php?file_name=" +
                (data.getGambarSejarahMasjid() != null ? data.getGambarSejarahMasjid() : "default_placeholder.png");

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.ic_launcher_background)
                .into(imgMasjid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            // Refresh data setelah edit
            loadProfilMasjid();
        }
    }
}