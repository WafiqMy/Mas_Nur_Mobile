// File: app/src/main/java/com/example/masnur/Fitur_Halaman_Utama/Halaman_Utama_Activity.java

package com.example.masnur.Fitur_Halaman_Utama;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.Fitur_Acara.AcaraActivity;
import com.example.masnur.Fitur_Berita.Berita_Activity;
import com.example.masnur.Fitur_Informasi_Masjid.Informasi_Masjid_Activity;
import com.example.masnur.Fitur_Persewaan.Persewaan_Activity;
import com.example.masnur.Fitur_Persewaan.ReservasiItemModel;
import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.Header_dan_Footer.Header;
import com.example.masnur.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Halaman_Utama_Activity extends AppCompatActivity {

    private CardView cardGedung, cardAlat;
    private TextView tvDetailGedung, tvDetailAlat, tvNoData;
    private Button btnLihat1, btnLihat2, btn_acara, btn_informasi, btn_pemesanan, btn_berita;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);

        Footer.setupFooter(this);
        Header.setupHeader(this);

        // Inisialisasi view
        cardGedung = findViewById(R.id.card_gedung);
        cardAlat = findViewById(R.id.card_alat);
        tvDetailGedung = findViewById(R.id.status_1_detail);
        tvDetailAlat = findViewById(R.id.status_2_detail);
        tvNoData = findViewById(R.id.tv_no_data);
        btnLihat1 = findViewById(R.id.btn_lihat_1);
        btnLihat2 = findViewById(R.id.btn_lihat_2);

        btn_acara = findViewById(R.id.btn_acara);
        btn_informasi = findViewById(R.id.btn_informasi);
        btn_pemesanan = findViewById(R.id.btn_pemesanan);
        btn_berita = findViewById(R.id.btn_berita);

        apiService = ApiClient.getService();

        // Setup button navigasi
        btnLihat1.setOnClickListener(v -> openPemesanan());
        btnLihat2.setOnClickListener(v -> openPemesanan());
        btn_pemesanan.setOnClickListener(v -> openPemesanan());
        btn_acara.setOnClickListener(v -> openAcara());
        btn_informasi.setOnClickListener(v -> openInformasiMasjid());
        btn_berita.setOnClickListener(v -> openBerita());

        loadMenungguData();
    }

    private void openPemesanan() {
        startActivity(new Intent(this, Persewaan_Activity.class));
    }

    private void openInformasiMasjid() {
        startActivity(new Intent(this, Informasi_Masjid_Activity.class));
    }

    private void openAcara() {
        startActivity(new Intent(this, AcaraActivity.class));
    }

    private void openBerita() {
        startActivity(new Intent(this, Berita_Activity.class));
    }

    private void loadMenungguData() {
        apiService.getReservasiMenunggu().enqueue(new Callback<List<ReservasiItemModel>>() {
            @Override
            public void onResponse(Call<List<ReservasiItemModel>> call, Response<List<ReservasiItemModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReservasiItemModel> menunggu = new ArrayList<>();
                    for (ReservasiItemModel item : response.body()) {
                        if (item.getExtendedProps() != null) {
                            String status = item.getExtendedProps().getStatus();
                            if (status != null && status.toLowerCase().contains("menunggu")) {
                                menunggu.add(item);
                            }
                        }
                    }
                    updateUI(menunggu);
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<List<ReservasiItemModel>> call, Throwable t) {
                Toast.makeText(Halaman_Utama_Activity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                showError();
            }
        });
    }

    private void updateUI(List<ReservasiItemModel> list) {
        if (list.isEmpty()) {
            cardGedung.setVisibility(View.GONE);
            cardAlat.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
            return;
        }

        tvNoData.setVisibility(View.GONE);

        // Card 1: data pertama
        ReservasiItemModel item1 = list.get(0);
        if (item1.getExtendedProps() != null) {
            tvDetailGedung.setText("Atas Nama: " + item1.getExtendedProps().getPeminjam());
            cardGedung.setVisibility(View.VISIBLE);
        }

        // Card 2: data kedua (jika ada)
        if (list.size() >= 2) {
            ReservasiItemModel item2 = list.get(1);
            if (item2.getExtendedProps() != null) {
                tvDetailAlat.setText("Atas Nama: " + item2.getExtendedProps().getPeminjam());
                cardAlat.setVisibility(View.VISIBLE);
            }
        } else {
            cardAlat.setVisibility(View.GONE);
        }
    }

    private void showError() {
        cardGedung.setVisibility(View.GONE);
        cardAlat.setVisibility(View.GONE);
        tvNoData.setText("Gagal memuat data");
        tvNoData.setVisibility(View.VISIBLE);
    }
}