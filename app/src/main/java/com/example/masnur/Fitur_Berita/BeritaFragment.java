package com.example.masnur.Fitur_Berita;

public class BeritaFragment extends Fragment {
    RecyclerView recyclerView;
    List<BeritaModel> beritaList = new ArrayList<>();
    String URL = "https://yourdomain.awardspace.info/get_berita.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_berita, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewBerita);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadBerita();
        return view;
    }

    private void loadBerita() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
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
                    recyclerView.setAdapter(new BeritaAdapter(getContext(), beritaList));
                },
                error -> Toast.makeText(getContext(), "Gagal ambil data", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }
}