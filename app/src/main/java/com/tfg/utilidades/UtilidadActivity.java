package com.tfg.utilidades;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class UtilidadActivity {
    public static void lanzarIntent(Context context, Class<?> activity, Bundle extras) {
        Intent intent = new Intent(context, activity);
        if (extras != null) intent.putExtras(extras);
        context.startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void setEfectoBoton(ImageButton button) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setScaleX(0.9f);
                    v.setScaleY(0.9f);
                    break;
                case MotionEvent.ACTION_UP:
                    v.setScaleX(1.0f);
                    v.setScaleY(1.0f);
                    v.performClick(); // Llamar a performClick cuando se suelta el botón
                    break;
            }
            return true; // Devolver true para indicar que se ha manejado el evento
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void setEfectoBoton(ImageButton button, TextView textView) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setScaleX(0.9f);
                    v.setScaleY(0.9f);
                    textView.setScaleX(0.9f);
                    textView.setScaleY(0.9f);
                    break;
                case MotionEvent.ACTION_UP:
                    v.setScaleX(1.0f);
                    v.setScaleY(1.0f);
                    textView.setScaleX(1.0f);
                    textView.setScaleY(1.0f);
                    v.performClick(); // Llamar a performClick cuando se suelta el botón
                    break;
            }
            return true; // Devolver true para indicar que se ha manejado el evento
        });
    }

    public static void setLimitesSeekbar(SeekBar seekBar, int min, int max) {
        seekBar.setMin(0);
        seekBar.setMax(max);
    }
}
