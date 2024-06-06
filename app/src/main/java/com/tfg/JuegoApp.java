package com.tfg;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class JuegoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        crearCanalDeNotificaciones();
    }

    private void crearCanalDeNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    getString(R.string.id_canal_notificaciones),
                    "Notificaciones de FCM",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setDescription("Estas notificaciones van a ser recibidas desde FCM");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
