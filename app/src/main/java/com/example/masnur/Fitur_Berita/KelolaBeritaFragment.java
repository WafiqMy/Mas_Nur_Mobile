package com.example.masnur.Fitur_Berita;


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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.masnur.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KelolaBeritaFragment extends Fragment {
    RecyclerView recyclerView;
    List<BeritaModel> beritaList = new ArrayList<>();
    String URL = "http://masnurhuda.atwebpages.com/get_berita.php"; // ganti dengan URL asli kamu

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelola_berita, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewkelolaBerita);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadBerita();
        return view;
    }

    private void loadBerita() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
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
                    recyclerView.setAdapter(new BeritaAdapter(requireContext(), beritaList));
                },
                error -> Toast.makeText(requireContext(), "Gagal ambil data", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }
}