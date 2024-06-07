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
    private final static String TAG = NotificacionesService.class.getSimpleName();

    @Getter
    @Setter
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
                // Victoria es true si gana el atacante
                if (victoria) {
                    notification.put("body", "Has perdido tus defensas y algunos recursos");
                } else {
                    notification.put("body", "Tu aldea ha soportado el ataque");
                }
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
                    Log.e("FCM", "Error en la peticion, codigo error: " + responseCode);
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
                    "  \"private_key_id\": \"17aafee41a77d72c93f3c30eec2fa950801a03c8\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCtFwxkGV3ZK1qG\\nv1+yEGBDXS+8fcoWpNIYAiV93CsfNhlZKNxfm/ItYaJJ4NR4FDIVjOR/U5BP2MWO\\ncVat1j/qNQ69c1j9vTFE61aoledwemQ4mGYkdIrg19aXKM5caoxfIwubI0Ou0qXR\\nVlHNrwyOs/2XUSDNjhxVmSbO2XHpj1R0hBs2TMNUiXey1GhjpDMV6vXNRMouPLJ0\\ndneTczDR18D82Njm5yTmNAS0/4lBzsNb46bWvbdGhgJrGkHZ5Wqp/JGrfLdDpwXg\\nxvx40A+1d1sG4knU236FqdFB+figp3Y4lStr6CkdKFKVXf/cWAp0xsKNy0TNRUhA\\nutpN0/2tAgMBAAECggEAG2ZD35+VV9CRULXAGgGWH2vDp/iYkiuOl17VnP4KdHMd\\noZvCgL8An9bt9/hnYce+ouMt84rglAcqRLFjyXpaHUK/XdWBz47QmwqO6fpqy8cQ\\nENZtCtXI+y0nir+apQ33wQKc7navWt/9AC2EHwVX/Un4OWkOAoP9r74h4soQimpT\\neZ8q6cYSVQb054GgVGEGzYjB42g3gHisLWsGOTErcsKRLPaov8uvP+DXodtE9o2J\\nl6ES2ZKbiZ5mEFcDefc2ieHsTloxSPU0IUDFwjNVxRA5lRrbqa2fo1Bwa51QhcoL\\nNGTFX6PV7WMw/Os2PWN7v7Kr0s7ZIdgarMX2FrQ0oQKBgQDcxfGmpz2i8go//VrK\\ny9P2mCkBBgnCzBXoBehMlem40Eew9p7yjxTYokRXyYUm6YJNs+fzmC4+BZBQUT+a\\nPxt68uXHQcN9OISlz0J7X9ki5cQ+y/NZC3trxUrbV6fg7caY4hr99OEbZlOwDwSR\\ndhhPhBRLtS+dPaAjblz0kV0PTQKBgQDItVxbgFVyGTaHT1nGTyBQK1MJi61uZy/5\\nD990dybIt+vsIbH20NMBmVVGQsFKUhFeQKozT8xK01YHN3UzrmO9FsqiZ9X4sLa4\\n0YcYU/hKBehoariI34DGLPn1y/mi5wMQ/Lq/1fDDS/dW2HFs2spb/J+/IVZ0/PMt\\nM9K838A34QKBgAsv+KviQ6xTKZPOieBG7enMEh/cJ3h4kQ+d5QSkgCB+ZXOhO7K8\\n72h+6ImuS5IKOTEo0bM1XfqEm6iGRXvSgNj9IAm12mspBwfD9fF8jOtIy2YHPcCg\\nZVOzvrRYv8gVJIAwzP9bfmC+EbWVPT0Fx/uXjNO9TblhGxFQkWNPTnDtAoGAcHoq\\nauDNgaadh1Cxt8+qBSNyYnwTUjajjMkXadZINt3Tab4sVGwMg+3g+wH3/mVldFN6\\nzkye6SPsJxkuJitJm7MFFeRo5fhqPcK2Ga0dDKao6rM7/QB70xNrdRK9MEyTzFZF\\ndbI4Pcn5r4KWdfFazBgTkfHl2KPUx3UuWUhvGSECgYAmEdUZoHk0mo864gnsYIfS\\nbWyBy2U5ObxV9Mv8a2VB39yTvFm6bae7d2QEMokIq1UmdaFYvLlI7js25rqQJRBR\\nHrYYrtRbjDF+HQcrO9SU9fBoPNKbqAFEW7eTY96/NtNKJif+Ed/Vu+Yongo+HpxM\\nVT+ymP0ZHzKWnfiOBOY6Lg==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-d5gpy@tfg-juego.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"108844759100687801425\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-d5gpy%40tfg-juego.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}";
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
