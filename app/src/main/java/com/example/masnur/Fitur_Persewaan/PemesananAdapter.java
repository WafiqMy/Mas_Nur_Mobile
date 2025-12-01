package com.example.masnur.Fitur_Persewaan;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masnur.R;

import java.util.List;

public class PemesananAdapter extends RecyclerView.Adapter<PemesananAdapter.ViewHolder> {

    private List<ReservasiItemModel> list;

    public PemesananAdapter(List<ReservasiItemModel> list) {
        this.list = list;
    }

    public void updateData(List<ReservasiItemModel> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pemesanan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReservasiItemModel item = list.get(position);
        ReservasiItemModel.ExtendedProps props = item.getExtendedProps();

        holder.textJenisPermintaan.setText("Peminjaman: " + props.getBarang());
        holder.textNamaPemesan.setText("Atas Nama: " + props.getPeminjam());

        String status = props.getStatus();
        holder.textStatus.setText("Status: " + status);

        // ✅ WARNA STATUS SESUAI STATUS
        if ("Disetujui".equalsIgnoreCase(status) || "Sukses".equalsIgnoreCase(status)) {
            holder.textStatus.setTextColor(Color.parseColor("#198754")); // hijau
        } else if ("Ditolak".equalsIgnoreCase(status) || "Batal".equalsIgnoreCase(status)) {
            holder.textStatus.setTextColor(Color.parseColor("#dc3545")); // merah
        } else {
            holder.textStatus.setTextColor(Color.parseColor("#ffc107")); // kuning
        }

        holder.buttonLihat.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(int idReservasi);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textJenisPermintaan, textNamaPemesan, textStatus;
        Button buttonLihat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textJenisPermintaan = itemView.findViewById(R.id.textJenisPermintaan);
            textNamaPemesan = itemView.findViewById(R.id.textNamaPemesan);
            textStatus = itemView.findViewById(R.id.textStatus);
            buttonLihat = itemView.findViewById(R.id.buttonLihat);
        }
    }
}