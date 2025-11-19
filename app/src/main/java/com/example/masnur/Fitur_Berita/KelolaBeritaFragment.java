package com.example.masnur.Fitur_Berita;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelolaBeritaFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<BeritaModel> beritaList = new ArrayList<>();
    private final String URL = "http://masnurhuda.atwebpages.com/API/get_berita.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelola_berita, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewkelolaBerita);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Button btnTambah = view.findViewById(R.id.btnTambahBerita);
        btnTambah.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TambahBeritaActivity.class);
            requireActivity().startActivityForResult(intent, 100);
        });

        loadBerita();
        return view;
    }

    private void loadBerita() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, response -> {
            beritaList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    beritaList.add(new BeritaModel(
                            obj.getString("id_berita"),
                            obj.getString("judul_berita"),
                            obj.getString("isi_berita"),
                            obj.getString("tanggal_berita"),
                            obj.getString("foto_berita"),
                            obj.getString("username")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            KelolaBeritaAdapter adapter = new KelolaBeritaAdapter(requireContext(), beritaList, new KelolaBeritaAdapter.OnBeritaActionListener() {
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
        }, error -> Toast.makeText(requireContext(), "Gagal ambil data", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }

    public void hapusBerita(String id) {
        ProgressDialog dialog = ProgressDialog.show(requireContext(), "Menghapus...", "Mohon tunggu", true);

        ApiService apiService = ApiClient.getService();
        apiService.hapusBerita(id).enqueue(new Callback<BeritaResponse>() {
            @Override
            public void onResponse(Call<BeritaResponse> call, Response<BeritaResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null && "success".equalsIgnoreCase(response.body().getStatus())) {
                    Toast.makeText(requireContext(), "Berita dihapus", Toast.LENGTH_SHORT).show();
                    loadBerita();
                } else {
                    Toast.makeText(requireContext(), "Gagal hapus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeritaResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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