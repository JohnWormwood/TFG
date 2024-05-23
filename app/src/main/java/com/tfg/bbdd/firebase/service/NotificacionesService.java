package com.tfg.bbdd.firebase.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tfg.R;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class NotificacionesService extends FirebaseMessagingService {
    private final static String TAG = "NotificacionesService";

    @Getter @Setter
    private static String token;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        mostarNotificacion(message);
    }

    private void mostarNotificacion(RemoteMessage message) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        Notification notification =
                new NotificationCompat.Builder(this, getString(R.string.id_canal_notificaciones))
                        .setContentTitle(
                                Objects.requireNonNull(message.getNotification()).getTitle()
                        ).setContentText(
                                Objects.requireNonNull(message.getNotification()).getBody()
                        )
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setAutoCancel(true)
                        .build();
        notificationManager.notify(1, notification);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        NotificacionesService.token = token;
        GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
        gestorRealTimeDatabase.actualizarTokenFmc(token);
        Log.d(TAG, "Token actualizado: " + token);
    }

    public void enviarNotificacionAtaque(String tokenVictima, boolean victoria) {
        new Thread(() -> {
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "key="+getString(R.string.key_api_fcm));
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();
                json.put("to", tokenVictima);
                JSONObject info = new JSONObject();
                info.put("title", "¡Tu aldea está siendo atacada!");
                info.put("body", "Tu aldea ha sido atacada");
                json.put("notification", info);

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                outputStreamWriter.write(json.toString());
                outputStreamWriter.flush();
                outputStreamWriter.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("FCM", "Notificacion enviada correctamente");
                } else {
                    Log.e("FCM", "Error en la peticion, codigo error: "+responseCode);
                }
            } catch (Exception e) {
                Log.e("FCM", "Error al enviar la notificacion", e);
            }
        }).start();
    }
}
