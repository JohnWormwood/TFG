package com.tfg.bbdd.firebase.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tfg.R;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
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
                String accessToken = getAccessToken();

                URL url = new URL("https://fcm.googleapis.com/v1/projects/tfg-juego/messages:send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                conn.setRequestProperty("Content-Type", "application/json; UTF-8");

                JSONObject message = new JSONObject();
                JSONObject notification = new JSONObject();
                notification.put("title", "¡Tu aldea está siendo atacada!");
                notification.put("body", "Han atacado tu aldea");
                message.put("token", tokenVictima);
                message.put("notification", notification);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("message", message);

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(jsonBody.toString().getBytes("UTF-8"));
                outputStream.close();


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

    private String getAccessToken() throws IOException {
        InputStream serviceAccount = getResources().openRawResource(R.raw.service_account);

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccount)
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredentials.refreshIfExpired();
        AccessToken token = googleCredentials.getAccessToken();

        return token.getTokenValue();
    }
}
