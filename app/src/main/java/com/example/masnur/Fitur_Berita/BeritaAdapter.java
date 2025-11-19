package com.example.masnur.Fitur_Berita;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.masnur.R;

import java.util.List;

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
        holder.textJudul.setText(berita.getJudulBerita());
        holder.textTanggal.setText(berita.getTanggalBerita());

        Glide.with(context)
                .load(berita.getFotoBerita()) // URL lengkap dari API
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .centerCrop()
                .into(holder.imageBerita);
    } // ✅ penutup onBindViewHolder

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