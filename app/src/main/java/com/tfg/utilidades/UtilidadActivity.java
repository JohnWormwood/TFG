package com.tfg.utilidades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class UtilidadActivity {
    public static void lanzarIntent(Context context, Class<?> activity, Bundle extras) {
        Intent intent = new Intent(context, activity);
        if (extras != null) intent.putExtras(extras);
        context.startActivity(intent);
    }
}
