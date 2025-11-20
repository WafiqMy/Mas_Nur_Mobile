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

        // ✅ PERBAIKAN: Handle URL dobel & null
        String imageUrl = berita.getFotoBerita();
        if (imageUrl != null && imageUrl.startsWith("http")) {
            // Fix double URL (e.g., "http://.../http://...")
            if (imageUrl.indexOf("http://", 7) != -1) {
                imageUrl = imageUrl.substring(imageUrl.indexOf("http://", 7));
            } else if (imageUrl.indexOf("https://", 8) != -1) {
                imageUrl = imageUrl.substring(imageUrl.indexOf("https://", 8));
            }
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .centerCrop()
                    .into(holder.imageBerita);
        } else {
            holder.imageBerita.setImageResource(R.drawable.default_image);
        }
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