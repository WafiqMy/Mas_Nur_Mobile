package com.example.masnur.Fitur_Berita;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class KelolaBeritaFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<BeritaModel> beritaList = new ArrayList<>();
    private KelolaBeritaAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelola_berita, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewkelolaBerita);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new KelolaBeritaAdapter(requireContext(), beritaList, new KelolaBeritaAdapter.OnBeritaActionListener() {
            @Override
            public void onEditClick(BeritaModel berita) {
                Intent intent = new Intent(getActivity(), EditBeritaActivity.class);
                intent.putExtra("berita", berita);
                requireActivity().startActivityForResult(intent, 101);
            }

            @Override
            public void onDeleteClick(String idBerita) {
                hapusBerita(idBerita);
            }
        });

        recyclerView.setAdapter(adapter);

        Button btnTambah = view.findViewById(R.id.btnTambahBerita);
        btnTambah.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TambahBeritaActivity.class);
            requireActivity().startActivityForResult(intent, 100);
        });

        loadBerita();

        return view;
    }

    private void loadBerita() {
        ApiService api = ApiClient.getService();
        api.getBerita().enqueue(new Callback<BeritaListResponse>() {
            @Override
            public void onResponse(Call<BeritaListResponse> call, Response<BeritaListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BeritaListResponse res = response.body();
                    if (res.isSuccess() && res.getData() != null) {
                        beritaList.clear();
                        beritaList.addAll(res.getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(requireContext(), "Tidak ada berita", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeritaListResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Jaringan error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusBerita(String id) {
        ApiService api = ApiClient.getService();
        api.hapusBerita(id).enqueue(new Callback<BeritaResponse>() {
            @Override
            public void onResponse(Call<BeritaResponse> call, Response<BeritaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BeritaResponse res = response.body();
                    if ("success".equals(res.getStatus()) || "1".equals(res.getStatus())) {
                        Toast.makeText(requireContext(), "✓ Berita dihapus", Toast.LENGTH_SHORT).show();
                        loadBerita();
                    } else {
                        Toast.makeText(requireContext(), "✗ Gagal: " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeritaResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Jaringan error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100 || requestCode == 101) && resultCode == Activity.RESULT_OK) {
            loadBerita();
        }
    }
}