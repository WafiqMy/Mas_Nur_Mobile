package com.example.masnur.Fitur_Notifikasi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.masnur.R;

public final class NotificationHelper {
    private NotificationHelper() {}

    public static final String CHANNEL_ID = "reservasi_notif";

    public static void createChannel(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID, "Reservasi Notifikasi", NotificationManager.IMPORTANCE_DEFAULT);
            ch.setDescription("Notifikasi data reservasi baru");
            ch.enableLights(true);
            ch.setLightColor(Color.BLUE);
            ch.enableVibration(true);

            NotificationManager nm = ctx.getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(ch);
        }
    }

    public static void showNewReservasi(Context ctx, String title, String content) {
        // >>> PERMISSION CHECK YANG BENAR (pakai ContextCompat + ctx)
        if (Build.VERSION.SDK_INT >= 33) {
            int granted = ContextCompat.checkSelfPermission(
                    ctx, android.Manifest.permission.POST_NOTIFICATIONS);
            if (granted != PackageManager.PERMISSION_GRANTED) {
                // izin belum diberikan — jangan crash, cukup batal kirim notif
                return;
            }
        }

        createChannel(ctx);

        Intent intent = new Intent(ctx, MainNotifikasi.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(
                ctx, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)        // ganti ke icon kamu kalau ada
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(ctx).notify(
                (int) System.currentTimeMillis(),
                builder.build()
        );
    }
}
