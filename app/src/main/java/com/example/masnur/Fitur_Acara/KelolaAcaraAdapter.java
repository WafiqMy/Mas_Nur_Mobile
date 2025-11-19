package com.example.masnur.Fitur_Acara;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.Fitur_Acara.AcaraModel;
import com.example.masnur.Fitur_Acara.UbahAcaraFragment;
import com.example.masnur.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelolaAcaraAdapter extends RecyclerView.Adapter<KelolaAcaraAdapter.ViewHolder> {

    private Context context;
    private List<AcaraModel> acaraList;
    private OnRefreshListener refreshListener;

    public interface OnRefreshListener {
        void onRefresh();
    }

    public KelolaAcaraAdapter(Context context, List<AcaraModel> acaraList, OnRefreshListener listener) {
        this.context = context;
        this.acaraList = acaraList;
        this.refreshListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kelola_acara, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AcaraModel acara = acaraList.get(position);

        holder.textJudul.setText(acara.getNamaEvent());
        holder.textTanggal.setText(acara.getTanggalEventFormatted());

        String url = acara.getGambarEvent();
        if (url == null || url.trim().isEmpty() || url.endsWith("/acara/")) {
            holder.imageView.setImageResource(R.drawable.default_image);
        } else {
            Glide.with(context).load(url).into(holder.imageView);
        }

        // ✅ PERBAIKAN: Kirim Bundle berisi data acara
        holder.btnEdit.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putParcelable("acara", acara); // ✅ KIRIM DATA
            UbahAcaraFragment fragment = new UbahAcaraFragment();
            fragment.setArguments(args); // ✅ SET ARGUMENTS

            FragmentTransaction ft = ((AcaraActivity) context).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        holder.btnHapus.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Hapus Acara")
                    .setMessage("Yakin hapus \"" + acara.getNamaEvent() + "\"?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        hapusAcara(acara, position);
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }

    private void hapusAcara(AcaraModel acara, int position) {
        ApiService api = ApiClient.getService();
        Call<AcaraResponse> call = api.hapusAcara(acara.getIdEvent());

        call.enqueue(new Callback<AcaraResponse>() {
            @Override
            public void onResponse(Call<AcaraResponse> call, Response<AcaraResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AcaraResponse res = response.body();
                    if ("success".equals(res.getStatus()) || "1".equals(res.getStatus())) {
                        acaraList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, acaraList.size());
                        Toast.makeText(context, "✓ " + res.getMessage(), Toast.LENGTH_SHORT).show();
                        if (refreshListener != null) refreshListener.onRefresh();
                    } else {
                        Toast.makeText(context, "Gagal: " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AcaraResponse> call, Throwable t) {
                Toast.makeText(context, "Gagal hapus: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return acaraList != null ? acaraList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textTanggal, textJudul;
        Button btnEdit, btnHapus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagekelolaacara);
            textTanggal = itemView.findViewById(R.id.textTanggalkelolaacara);
            textJudul = itemView.findViewById(R.id.textJudulkelolaacara);
            btnEdit = itemView.findViewById(R.id.buttonEdit);
            btnHapus = itemView.findViewById(R.id.buttonHapus);
        }
    }
}