package com.example.masnur.Fitur_Berita;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {
    private List<BeritaModel> beritaList;
    private Context context;

    public BeritaAdapter(Context context, List<BeritaModel> beritaList) {
        this.context = context;
        this.beritaList = beritaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_berita, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BeritaModel berita = beritaList.get(position);
        holder.textJudul.setText(berita.getJudul());
        holder.textTanggal.setText(berita.getTanggal());

        Glide.with(context)
                .load(berita.getFoto())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageBerita);
    }

    @Override
    public int getItemCount() {
        return beritaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textJudul, textTanggal;
        ImageView imageBerita;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textJudul = itemView.findViewById(R.id.textJudulBerita);
            textTanggal = itemView.findViewById(R.id.textTanggalBerita);
            imageBerita = itemView.findViewById(R.id.imageBerita);
        }
    }
}