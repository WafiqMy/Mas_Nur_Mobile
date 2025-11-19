package com.example.masnur.Fitur_Acara;

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

public class KelolaAcaraFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<AcaraModel> acaraList = new ArrayList<>();
    private KelolaAcaraAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelola_acara, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewKelola);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new KelolaAcaraAdapter(requireContext(), acaraList, this::loadAcara);
        recyclerView.setAdapter(adapter);

        loadAcara();

        Button btnTambah = view.findViewById(R.id.btnTambahAcara);
        btnTambah.setOnClickListener(v -> {
            TambahAcaraFragment fragment = new TambahAcaraFragment();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    public void loadAcara() {
        ApiService apiService = ApiClient.getService();
        apiService.getAcara().enqueue(new Callback<AcaraModel[]>() {
            @Override
            public void onResponse(Call<AcaraModel[]> call, Response<AcaraModel[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    acaraList.clear();
                    for (AcaraModel acara : response.body()) {
                        acaraList.add(acara);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AcaraModel[]> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}