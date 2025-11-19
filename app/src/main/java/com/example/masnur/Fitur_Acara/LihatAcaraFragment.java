package com.example.masnur.Fitur_Acara;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

public class LihatAcaraFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textEmpty;
    private List<AcaraModel> acaraList = new ArrayList<>();
    private AcaraAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acara, container, false);

        // Inisialisasi view
        recyclerView = view.findViewById(R.id.recyclerViewacara);
        textEmpty = view.findViewById(R.id.textEmpty);
        Button btnKelola = view.findViewById(R.id.btnKelolaAcara);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AcaraAdapter(requireContext(), acaraList);
        recyclerView.setAdapter(adapter);

        // Load data
        loadAcara();

        // Tombol Kelola Acara
        btnKelola.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new KelolaAcaraFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadAcara() {
        ApiService apiService = ApiClient.getService();
        apiService.getAcara().enqueue(new Callback<AcaraModel[]>() {
            @Override
            public void onResponse(Call<AcaraModel[]> call, Response<AcaraModel[]> response) {
                Log.d("API_DEBUG", "onResponse: " + response.isSuccessful() + ", code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    AcaraModel[] body = response.body();
                    Log.d("API_DEBUG", "Jumlah data: " + body.length);

                    acaraList.clear();
                    for (int i = 0; i < body.length; i++) {
                        AcaraModel acara = body[i];
                        Log.d("API_DEBUG", "Data [" + i + "]: id=" + acara.getIdEvent() + ", judul=" + acara.getNamaEvent());
                        acaraList.add(acara);
                    }

                    adapter.notifyDataSetChanged();

                    // Tampilkan/hide placeholder
                    if (acaraList.isEmpty()) {
                        textEmpty.setVisibility(View.VISIBLE);
                    } else {
                        textEmpty.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Response code: " + response.code());
                    textEmpty.setVisibility(View.VISIBLE);
                    textEmpty.setText("Gagal memuat data");
                }
            }

            @Override
            public void onFailure(Call<AcaraModel[]> call, Throwable t) {
                Log.e("API_ERROR", "onFailure: ", t);
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                textEmpty.setVisibility(View.VISIBLE);
                textEmpty.setText("Tidak ada koneksi");
            }
        });
    }
}