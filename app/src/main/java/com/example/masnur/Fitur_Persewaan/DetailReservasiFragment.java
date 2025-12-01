package com.example.masnur.Fitur_Persewaan;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailReservasiFragment extends Fragment {

    private TextView tvNamaPeminjam, tvTelepon, tvEmail, tvBarang, tvJenis, tvJumlah, tvHarga,
            tvKeperluan, tvStatus, tvNotes, tvTanggalMulai, tvTanggalSelesai;
    private Button btnTerima, btnTolak;
    private int idReservasi;
    private ApiService apiService;

    public static DetailReservasiFragment newInstance(int id) {
        DetailReservasiFragment fragment = new DetailReservasiFragment();
        Bundle args = new Bundle();
        args.putInt("id_reservasi", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idReservasi = getArguments().getInt("id_reservasi");
        }
        apiService = ApiClient.getService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_reservasi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi TextView & Button
        tvNamaPeminjam = view.findViewById(R.id.tvNamaPeminjam);
        tvTelepon = view.findViewById(R.id.tvTelepon);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvBarang = view.findViewById(R.id.tvBarang);
        tvJenis = view.findViewById(R.id.tvJenis);
        tvJumlah = view.findViewById(R.id.tvJumlah);
        tvHarga = view.findViewById(R.id.tvHarga);
        tvKeperluan = view.findViewById(R.id.tvKeperluan);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvNotes = view.findViewById(R.id.tvNotes);
        tvTanggalMulai = view.findViewById(R.id.tvTanggalMulai);
        tvTanggalSelesai = view.findViewById(R.id.tvTanggalSelesai);
        btnTerima = view.findViewById(R.id.btnTerima);
        btnTolak = view.findViewById(R.id.btnTolak);

        loadData();

        btnTerima.setOnClickListener(v -> updateStatus("Disetujui"));
        btnTolak.setOnClickListener(v -> showRejectDialog());
    }

    private void loadData() {
        apiService.getReservasiDetail(idReservasi).enqueue(new Callback<ReservasiDetailResponse>() {
            @Override
            public void onResponse(Call<ReservasiDetailResponse> call, Response<ReservasiDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    ReservasiDetailResponse.Data data = response.body().getData();
                    bindData(data);
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Data tidak ditemukan";
                    Toast.makeText(getContext(), "Gagal: " + msg, Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ReservasiDetailResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }
        });
    }

    private void bindData(ReservasiDetailResponse.Data data) {
        tvNamaPeminjam.setText(data.getNamaPengguna());
        tvTelepon.setText(data.getNoTlpPengguna());
        tvEmail.setText(data.getEmailPengguna());
        tvBarang.setText(data.getNamaBarang());
        tvJenis.setText(data.getJenis());
        tvJumlah.setText(String.valueOf(data.getTotalPeminjaman()));
        tvHarga.setText("Rp " + String.format("%,d", data.getTotalHarga()).replace(',', '.'));
        tvKeperluan.setText(data.getKeperluan());

        // ✅ BIND TANGGAL MULAI
        String tglMulai = data.getTanggalMulaiReservasi();
        if (tglMulai != null && !tglMulai.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                Date date = inputFormat.parse(tglMulai);
                tvTanggalMulai.setText("Tanggal Mulai: " + outputFormat.format(date));
            } catch (Exception e) {
                tvTanggalMulai.setText("Tanggal Mulai: " + tglMulai);
            }
        } else {
            tvTanggalMulai.setText("Tanggal Mulai: -");
        }

        // ✅ BIND TANGGAL SELESAI
        String tglSelesai = data.getTanggalSelesaiReservasi();
        if (tglSelesai != null && !tglSelesai.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                Date date = inputFormat.parse(tglSelesai);
                tvTanggalSelesai.setText("Tanggal Selesai: " + outputFormat.format(date));
            } catch (Exception e) {
                tvTanggalSelesai.setText("Tanggal Selesai: " + tglSelesai);
            }
        } else {
            tvTanggalSelesai.setText("Tanggal Selesai: -");
        }

        tvStatus.setText("Status: " + data.getStatusReservasi());
        tvNotes.setText(data.getNotes() != null ? "Catatan: " + data.getNotes() : "Tidak ada catatan.");

        boolean isFinal = "Disetujui".equals(data.getStatusReservasi()) || "Ditolak".equals(data.getStatusReservasi());
        btnTerima.setEnabled(!isFinal);
        btnTolak.setEnabled(!isFinal);
    }

    private void showRejectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Tolak Permintaan");
        final EditText input = new EditText(requireContext());
        input.setHint("Masukkan alasan penolakan...");
        input.setMinLines(2);
        input.setMaxLines(4);
        input.setPadding(48, 32, 48, 32);
        builder.setView(input);

        builder.setPositiveButton("Kirim", (dialog, which) -> {
            String alasan = input.getText().toString().trim();
            if (alasan.isEmpty()) {
                Toast.makeText(getContext(), "Alasan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            updateStatus("Ditolak", alasan);
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    private void updateStatus(String status) {
        updateStatus(status, null);
    }

    private void updateStatus(String status, String notes) {
        JsonObject json = new JsonObject();
        json.addProperty("id", idReservasi);
        json.addProperty("status", status);
        if (notes != null && !notes.isEmpty()) {
            json.addProperty("notes", notes);
        }

        apiService.updateStatusReservasi(json).enqueue(new Callback<ReservasiResponse>() {
            @Override
            public void onResponse(Call<ReservasiResponse> call, Response<ReservasiResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    Toast.makeText(getContext(), "Status berhasil diubah", Toast.LENGTH_SHORT).show();
                    Fragment f = getParentFragmentManager().findFragmentById(R.id.frameLayout);
                    if (f instanceof PemesananFragment) {
                        ((PemesananFragment) f).refresh();
                    }
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Gagal mengupdate status";
                    Toast.makeText(getContext(), "Gagal: " + msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReservasiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}