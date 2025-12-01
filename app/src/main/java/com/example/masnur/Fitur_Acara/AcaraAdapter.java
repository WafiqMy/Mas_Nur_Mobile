package com.example.masnur.Fitur_Acara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.masnur.R;

import java.util.List;

public class AcaraAdapter extends RecyclerView.Adapter<AcaraAdapter.ViewHolder> {

    private Context context;
    private List<AcaraModel> acaraList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AcaraModel acara);
    }

    public AcaraAdapter(Context context, List<AcaraModel> acaraList, OnItemClickListener listener) {
        this.context = context;
        this.acaraList = acaraList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_acara, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AcaraModel acara = acaraList.get(position);
        holder.textJudulAcara.setText(acara.getNamaEvent());
        holder.textTanggalAcara.setText(acara.getTanggalEventFormatted());

        String url = acara.getGambarEventAbsolut();

        Glide.with(context)
                .load(url)
                .override(600, 400)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .fallback(R.drawable.default_image)
                .into(holder.imageAcara);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(acara);
            }
        });
    }

    @Override
    public int getItemCount() {
        return acaraList != null ? acaraList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textJudulAcara, textTanggalAcara;
        ImageView imageAcara;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textJudulAcara = itemView.findViewById(R.id.textJudulAcara);
            textTanggalAcara = itemView.findViewById(R.id.textTanggalAcara);
            imageAcara = itemView.findViewById(R.id.imageAcara);
        }
    }
}