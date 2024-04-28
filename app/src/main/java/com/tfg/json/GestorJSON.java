package com.tfg.json;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GestorJSON {

    public String cargarJsonDesdeAssets(String archivo, Context context) throws IOException {
        // Cargar el fichero desde los asstes
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(archivo);

        // Leer el contenido y devolverlo como string
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        inputStream.close();

        return new String(buffer, StandardCharsets.UTF_8);
    }
}
