package com.example.masnur.Fitur_Notifikasi;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.masnur.Fitur_Notifikasi.NotifResponse;
import com.example.masnur.Fitur_Notifikasi.NotificationAdapter;
import com.example.masnur.Fitur_Notifikasi.NotificationHelper;
import com.example.masnur.Fitur_Notifikasi.NotificationItem;
import com.example.masnur.R;
import com.example.masnur.Fitur_Notifikasi.NotifApiService;
import com.example.masnur.Fitur_Notifikasi.NotifRetrofitClient;

import java.util.Collections;
import java.util.List;

public class MainNotifikasi extends AppCompatActivity {

    private static final int REQ_POST_NOTIF = 1001;

    private RecyclerView rv;
    private ProgressBar progress;
    private TextView tvKosong;
    private SwipeRefreshLayout swipe; // boleh null

    private NotificationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifikasi_main);

        // --- minta izin notifikasi untuk Android 13+ ---
        requestPostNotifIfNeeded();

        rv = findViewById(R.id.rv_notifications);
        progress = findViewById(R.id.progress_bar);
        tvKosong = findViewById(R.id.tv_status_kosong);
//        swipe = findViewById(R.id.swipe_refresh); // null kalau ga ada di XML

        adapter = new NotificationAdapter(item -> {
            // TODO: ke halaman detail kalau sudah ada
            // Intent i = new Intent(this, DetailReservasi.class);
            // i.putExtra("ID_RESERVASI", item.getId_reservasi());
            // startActivity(i);
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        if (swipe != null) swipe.setOnRefreshListener(this::fetchNotifications);

        fetchNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchNotifications(); // auto refresh saat balik
    }

    private void fetchNotifications() {
        showLoading(true);

        NotifApiService api = NotifRetrofitClient.getApi();
        api.getNotificationsRaw().enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> res) {
                showLoading(false);

                if (!res.isSuccessful()) {
                    String msg = "HTTP " + res.code() + " - " + res.message();
                    Log.e("NET", msg);
                    showEmpty(msg);
                    return;
                }

                String raw = res.body();
                if (raw == null) raw = "";
                Log.d("NET_RAW", "<<<\n" + raw + "\n>>>");

                // --- NORMALIZE (buang BOM / HTML / iklan / noise) ---
                raw = raw.replaceFirst("^\uFEFF", "").trim();
                if (raw.startsWith("<")) { // hosting nyuntik HTML/warning
                    showEmpty("Server kirim HTML/warning, bukan JSON. Cek PHP/hosting.");
                    return;
                }
                int sObj = raw.indexOf('{'), sArr = raw.indexOf('['), start = -1;
                if (sObj >= 0 && sArr >= 0) start = Math.min(sObj, sArr);
                else if (sObj >= 0) start = sObj; else if (sArr >= 0) start = sArr;
                if (start > 0) raw = raw.substring(start).trim();
                int eObj = raw.lastIndexOf('}'), eArr = raw.lastIndexOf(']'), end = Math.max(eObj, eArr);
                if (end > 0 && end < raw.length() - 1) raw = raw.substring(0, end + 1);

                // unquote kalau JSON dipetiin
                com.google.gson.Gson tmp = new com.google.gson.Gson();
                for (int i = 0; i < 2; i++) {
                    if (raw.startsWith("\"") && raw.endsWith("\"")) {
                        try { raw = tmp.fromJson(raw, String.class).trim(); } catch (Exception ignored) {}
                    } else break;
                }

                // --- PARSE ke model ---
                try {
                    com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setLenient().create();
                    NotifResponse body = gson.fromJson(raw, NotifResponse.class);
                    List<NotificationItem> list = (body != null && body.getData() != null)
                            ? body.getData() : Collections.emptyList();

                    if (body != null && "success".equalsIgnoreCase(body.getStatus()) && !list.isEmpty()) {
                        adapter.submitList(list);
                        rv.setVisibility(View.VISIBLE);
                        tvKosong.setVisibility(View.GONE);

                        // --- DETEKSI DATA BARU & TAMPILKAN NOTIFIKASI ---
                        // asumsi urutan terbaru di indeks 0
                        String newestId = safe(list.get(0).getId_reservasi());
                        String last = getLastSeenId();

                        if (!newestId.isEmpty() && !newestId.equals(last)) {
                            String nama = safe(list.get(0).getNama_pengguna());
                            String jenis = safe(list.get(0).getJenis());

                            // panggil helper notifikasi (channel dibuat otomatis di helper)
                            NotificationHelper.showNewReservasi(
                                    MainNotifikasi.this,
                                    "Reservasi baru",
                                    "Atas nama " + nama + " (" + jenis + ")"
                            );

                            setLastSeenId(newestId);
                        }

                    } else {
                        showEmpty(body != null && body.getMessage() != null
                                ? body.getMessage()
                                : "Tidak ada notifikasi baru.");
                    }
                } catch (Exception e) {
                    Log.e("NET", "Parse gagal: " + e.getMessage());
                    showEmpty("Format respons bukan JSON valid.");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                showLoading(false);
                Log.e("NET", "FAIL: " + t.getClass().getSimpleName() + " -> " + t.getMessage(), t);
                showEmpty("Koneksi gagal: " + t.getMessage());
            }
        });
    }

    // --- utils ---

    private void showEmpty(String msg) {
        adapter.submitList(Collections.emptyList());
        rv.setVisibility(View.GONE);
        tvKosong.setVisibility(View.VISIBLE);
        tvKosong.setText(msg);
    }

    private void showLoading(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        if (swipe != null && swipe.isRefreshing()) swipe.setRefreshing(false);
    }

    private String getLastSeenId() {
        return getSharedPreferences("notif", MODE_PRIVATE).getString("last_id", "");
    }

    private void setLastSeenId(String id) {
        getSharedPreferences("notif", MODE_PRIVATE).edit().putString("last_id", id).apply();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private void requestPostNotifIfNeeded() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        REQ_POST_NOTIF
                );
            }
        }
    }
}