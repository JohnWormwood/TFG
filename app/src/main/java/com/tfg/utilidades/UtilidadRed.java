package com.tfg.utilidades;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class UtilidadRed {
    // Método para verificar si hay conexión a Internet
    public static boolean hayInternet(Context context) {
        return hayWifi(context) || hayDatos(context);
    }

    public static boolean hayWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            }
        }
        return false;
    }

    public static boolean hayDatos(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            }
        }
        return false;
    }

}
