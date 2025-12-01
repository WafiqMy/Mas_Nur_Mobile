package com.example.masnur.Fitur_Berita;

import android.content.Intent;
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

public class BeritaFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<BeritaModel> beritaList = new ArrayList<>();
    private BeritaAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_berita, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBerita);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // ✅ BUAT ADAPTER DENGAN LISTENER KLIK
        adapter = new BeritaAdapter(requireContext(), beritaList, new BeritaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BeritaModel berita) {
                // Mulai activity detail dengan membawa data berita
                Intent intent = new Intent(requireContext(), DetailBeritaActivity.class);
                intent.putExtra("berita", berita); // BeritaModel harus Parcelable (sudah ada!)
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

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
}