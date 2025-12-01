package com.example.masnur.Fitur_Persewaan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemesananFragment extends Fragment {

    private RecyclerView recyclerView;
    private PemesananAdapter adapter;
    private ApiService apiService;
    private List<ReservasiItemModel> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pemesanan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewemesanan);
        apiService = ApiClient.getService();

        setupRecyclerView();
        loadData(); // Saat pertama kali muncul
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh(); // ✅ JAMIN DATA SELALU TERBARU SETIAP KEMBALI KE HALAMAN
    }

    private void setupRecyclerView() {
        adapter = new PemesananAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(idReservasi -> {
            Fragment fragment = DetailReservasiFragment.newInstance(idReservasi);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void loadData() {
        apiService.getAllReservasi().enqueue(new Callback<List<ReservasiItemModel>>() {
            @Override
            public void onResponse(Call<List<ReservasiItemModel>> call, Response<List<ReservasiItemModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dataList.clear();
                    dataList.addAll(response.body());
                    adapter.updateData(dataList);
                } else {
                    Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReservasiItemModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refresh() {
        loadData();
    }
}