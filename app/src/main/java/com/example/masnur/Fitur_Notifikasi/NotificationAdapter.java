package com.example.masnur.Fitur_Notifikasi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masnur.R;

import java.util.Objects;

public class NotificationAdapter extends ListAdapter<NotificationItem, NotificationAdapter.VH> {

    public interface OnLihatClick { void onClick(NotificationItem item); }
    private final OnLihatClick onLihatClick;

    public NotificationAdapter(OnLihatClick onLihatClick) {
        super(DIFF_CALLBACK);
        this.onLihatClick = onLihatClick;
    }

    private static final DiffUtil.ItemCallback<NotificationItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<NotificationItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull NotificationItem oldItem, @NonNull NotificationItem newItem) {
                    return Objects.equals(oldItem.getId_reservasi(), newItem.getId_reservasi());
                }

                @Override
                public boolean areContentsTheSame(@NonNull NotificationItem o, @NonNull NotificationItem n) {
                    return Objects.equals(o.getNama_pengguna(), n.getNama_pengguna()) &&
                            Objects.equals(o.getJenis(), n.getJenis());
                }
            };

    static class VH extends RecyclerView.ViewHolder {
        TextView tvJenis, tvNama;
        Button btnLihat;

        VH(@NonNull View itemView) {
            super(itemView);
            tvJenis = itemView.findViewById(R.id.tv_jenis_permintaan);
            tvNama  = itemView.findViewById(R.id.tv_nama_pengguna);
            btnLihat = itemView.findViewById(R.id.btn_lihat);
        }

        void bind(NotificationItem item, OnLihatClick click) {
            tvJenis.setText(getJudul(item.getJenis()));
            tvNama.setText(String.format("Atas Nama: %s", item.getNama_pengguna()));
            btnLihat.setOnClickListener(v -> {
                if (click != null) click.onClick(item);
            });
        }

        private String getJudul(String jenis) {
            if (jenis == null) return "Permintaan Reservasi";
            jenis = jenis.toLowerCase();
            if (jenis.contains("gedung")) return "Permintaan Pemesanan Gedung";
            if (jenis.contains("alat")) return "Permintaan Peminjaman Alat";
            return "Permintaan Reservasi";
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(getItem(position), onLihatClick);
    }
}