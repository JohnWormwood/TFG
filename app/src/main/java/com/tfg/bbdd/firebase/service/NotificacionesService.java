package com.tfg.bbdd.firebase.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tfg.R;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
        gestorRealTimeDatabase.actualizarTokenFcm(token);
        Log.d(TAG, "Token actualizado: " + token);
    }

    public void enviarNotificacionAtaque(String tokenVictima, boolean victoria) {
        System.out.println(tokenVictima);
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

    private String getAccessToken() {
        String url = "https://www.googleapis.com/auth/firebase.messaging";

        try {
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"tfg-juego\",\n" +
                    "  \"private_key_id\": \"053faccda06750f43df4f18387b6ce602edb140d\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDaCeuFKN3QQHXD\\nrCjtp+PBpgnU37JqP9+HakFKi1vnyZ07Tgf8ia49SV3g+6a6tMvG5gaxES9qj6pa\\nbkZ8kwjK184kYmEILqeuNShOLSTDpjxyHKU3O0J3oMid5T1gTeEGdqzsRCVY0zqE\\n2jGykg7wOjg+SMwRxDSP+X2CYSiduITrCve+tzqv2V4Lo9hxgJAWOoSOMlmuGiZz\\noXN+5szrOaCMW+pIeLOoVR1gmDFnqXp2ZdASbd0Igx0kIKbVYFPGSp3jSCz/ew7f\\nMwW5Uz1p+oZCe94mSuVIHhuIOKAkULGi3zHeF2rBf67/xsmB5bVwrGMYuNj+Yf3s\\nD3bm9tZNAgMBAAECggEALDEkhjmxM3tNIxqqtPh5Rg+EIJwySiyl/ok/sH8sYn9P\\nmeyH29x/7zYGjAoVUfRA9RA8PPkxthRmP6voJ2mZt5sj0eYKqqDiJYHqqGWn9Oul\\nhq9doSMmpp506uPCm9lNCYVWKRDqzOULEFA1EG9gDbqPGbihe9xblu7HTHyqwcR7\\nWAzfj1IXvbumjUncRpSl1DePeIetUHllHhaZk5hANFLY78PdZRqBu5N7t+JKHLl/\\n4T13ej/1d0fG1jAgeEMJy/SJKwomyy1x/2Cp0i7wt7pe0Mts3ra04CORr3hUfpBY\\neQLBtwDc2P0wb5UHAOPuSIdmwAaEpVsBjVfZAj544QKBgQD8mKdZ997rVRi2t4rK\\nopbxjgQq6nyMdDY7fask93W3t8jrGipeQzK3J+W17fHtE8Os7hhdyZyRsfyMoANO\\nZcJpT2sKx0WJbVCK0ScML5tOKXFodeE0GMeu68gkQ/A0l3pyXboKN2a1MZcaZKcr\\n0a8MvxqCvfDzJdi21oVZESk86QKBgQDc+g7S3pOQGFTCXo4WJRUhFpx2ld6V994R\\nF54o3cl4tuEkGTIfPuuK3jFl4TKmVFRDPHUibHHIs+DRCrxLYX/ZaHhd3fIlCfrt\\nk50OZN+ja5Hp4BCjpswDce6XGE1ayO60HStFBwYNRnqGXiekNECen9hQG+1e0sfH\\n718riUzfxQKBgQDcBSf4QOUGfVgKh7M4MAlknHxa2WblfLBkf2Ec0QPTpB05U9BF\\nzSiWywTWxE0vYUDXF4lX4C4l5jACWbqnhhGiY0/s0ohqXTKDyAzIG4ueuJCVGeCw\\n3UjJv4zUsezAEpXQmn1bOsiW2XwxvCy1CW79nlQ8P7BqSrQLBq6eV0lhSQKBgQCM\\n5nUlR/7XVmxVM21t+V8rpftGMfGntlDGnUSf0itw6UtNdTNccQZHmb3ttFZTcFn6\\nNUaLGJLE4s1q/Wlqt1SKD+8QC0EnFOIHMqRmVTO0RBbaBaEWeHiFidQUS86nII/0\\nENVSQlQ9ir79hnPIY/kAK/QFwIjueYAhoCQS4o098QKBgQCyW4fx9cwCefKzGEQM\\n5K/gPUFYfChO4dk+GsZdPQqpvncQlD5dYeQ/u6E43PDwFsFdrxkgeu2Ubye2WbmO\\n/NvLmgrJaqbVw9D2YOTzLehwaQR3e9wkS2bD3wTluWCReMJ5/LyXt5ffd7XqOIsl\\n5gLIDwPXDa/+xKymwuQA2jjXCw==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-ntfhr@tfg-juego.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"111565326526054449512\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ntfhr%40tfg-juego.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";
            InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream)
                    .createScoped(Lists.newArrayList(url));
            googleCredentials.refresh();

            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            Log.e("Error", e.getLocalizedMessage(), e);
            return null;
        }
    }
}
