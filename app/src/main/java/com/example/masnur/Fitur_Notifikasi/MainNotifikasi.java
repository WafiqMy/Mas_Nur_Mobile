package com.example.masnur.Fitur_Notifikasi;

import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.masnur.R;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.Header_dan_Footer.Header;

public class MainNotifikasi extends AppCompatActivity {


    private static final int REQ_POST_NOTIF = 1001;

    private RecyclerView rv;
    private ProgressBar progress;
    private TextView tvKosong;
    private SwipeRefreshLayout swipe;

    private NotificationAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifikasi_main);

        Footer.setupFooter(this);
        Header.setupHeader(this);

        requestPostNotifIfNeeded();

        rv = findViewById(R.id.rv_notifications);
        progress = findViewById(R.id.progress_bar);
        tvKosong = findViewById(R.id.tv_status_kosong);
//        swipe = findViewById(R.id.swipe_refresh);

        adapter = new NotificationAdapter(item -> {
            // TODO: ke halaman detail jika sudah tersedia
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        if (swipe != null) swipe.setOnRefreshListener(this::fetchNotifications);

        fetchNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchNotifications();
    }

    private void fetchNotifications() {
        if (!isNetworkAvailable()) {
            showEmpty("Tidak ada koneksi internet.");
            return;
        }

        showLoading(true);

        ApiService api = ApiClient.getService();
        api.getNotifications().enqueue(new Callback<NotifResponse>() {
            @Override
            public void onResponse(Call<NotifResponse> call, Response<NotifResponse> response) {
                showLoading(false);

                if (!response.isSuccessful() || response.body() == null) {
                    showEmpty("Gagal: " + response.code() + " - " + response.message());
                    return;
                }

                NotifResponse body = response.body();
                List<NotificationItem> list = body.getData() != null ? body.getData() : Collections.emptyList();

                if ("success".equalsIgnoreCase(body.getStatus()) && !list.isEmpty()) {
                    adapter.submitList(list);
                    rv.setVisibility(View.VISIBLE);
                    tvKosong.setVisibility(View.GONE);

                    Log.d("NET", "Notifikasi berhasil dimuat: " + list.size() + " item");
                    handleNewNotification(list);
                } else {
                    showEmpty(body.getMessage() != null ? body.getMessage() : "Tidak ada notifikasi baru.");
                }
            }

            @Override
            public void onFailure(Call<NotifResponse> call, Throwable t) {
                showLoading(false);
                Log.e("NET", "FAIL: " + t.getMessage(), t);
                showEmpty("Koneksi gagal: " + t.getMessage());
            }
        });
    }

    private void handleNewNotification(List<NotificationItem> list) {
        String newestId = safe(list.get(0).getId_reservasi());
        String last = getLastSeenId();

        if (!TextUtils.isEmpty(newestId) && !newestId.equals(last)) {
            String nama = safe(list.get(0).getNama_pengguna());
            String jenis = safe(list.get(0).getJenis());

            NotificationHelper.showNewReservasi(
                    this,
                    "Reservasi baru",
                    "Atas nama " + nama + " (" + jenis + ")"
            );

            setLastSeenId(newestId);
        }
    }

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
        if (Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    REQ_POST_NOTIF
            );
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
        return info != null && info.isConnected();
    }
}