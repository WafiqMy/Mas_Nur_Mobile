package com.example.masnur.Fitur_Berita;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.masnur.R;

import java.util.List;

public class KelolaBeritaAdapter extends RecyclerView.Adapter<KelolaBeritaAdapter.ViewHolder> {

    private List<BeritaModel> beritaList;
    private Context context;
    private OnBeritaActionListener listener;

    public interface OnBeritaActionListener {
        void onEditClick(BeritaModel berita);
        void onDeleteClick(String idBerita);
    }

    public KelolaBeritaAdapter(Context context, List<BeritaModel> beritaList, OnBeritaActionListener listener) {
        this.context = context;
        this.beritaList = beritaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kelola_berita, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BeritaModel berita = beritaList.get(position);
        holder.textJudul.setText(berita.getJudulBerita());
        holder.textTanggal.setText(berita.getTanggalBerita());

        String imageUrl = berita.getFotoBeritaAbsolut(); // ✅

        Glide.with(context)
                .load(imageUrl)
                .override(300, 200)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .fallback(R.drawable.default_image)
                .into(holder.imageBerita);

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(berita);
            }
        });

        holder.btnHapus.setOnClickListener(v -> {
            if (listener != null) {
                new AlertDialog.Builder(context)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Yakin ingin menghapus berita ini?")
                        .setPositiveButton("Ya", (dialog, which) -> listener.onDeleteClick(berita.getIdBerita()))
                        .setNegativeButton("Batal", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return beritaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textJudul, textTanggal;
        ImageView imageBerita;
        Button btnEdit, btnHapus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textJudul = itemView.findViewById(R.id.textJudulkelolaBerita);
            textTanggal = itemView.findViewById(R.id.textTanggalkelolaBerita);
            imageBerita = itemView.findViewById(R.id.imagekelolaBerita);
            btnEdit = itemView.findViewById(R.id.buttonEdit);
            btnHapus = itemView.findViewById(R.id.buttonHapus);
        }
    }
}