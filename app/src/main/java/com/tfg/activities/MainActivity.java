package com.tfg.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tfg.R;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton buttonJugar = findViewById(R.id.buttonJugar);
        TextView textViewJugar = findViewById(R.id.textViewJugar);
        setEfectoBoton(buttonJugar, textViewJugar);

        ImageButton buttonOpciones = findViewById(R.id.buttonOpciones);
        TextView textViewOpciones = findViewById(R.id.textViewOpciones);
        setEfectoBoton(buttonOpciones, textViewOpciones);

        ImageButton buttonSalir = findViewById(R.id.buttonSalir);
        TextView textViewSalir = findViewById(R.id.textViewSalir);
        setEfectoBoton(buttonSalir, textViewSalir);


    }
    public void setEfectoBoton(ImageButton button, TextView textView) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
            }
        });
    }

    public void buttonJugarOnClick(View view) {
        // TODO si no se ha iniciado sesion mostrar el login y luego el juego,
        //  si la sesion esta guardada mostrar directamente el juego
        intent = new Intent(this, JuegoActivity.class);
        startActivity(intent);
    }

    public void buttonOpcionesOnClick(View view) {
        intent = new Intent(this, OpcionesActivity.class);
        startActivity(intent);
    }

    public void buttonSalirOnClick(View view) {
        // Usar este en lugar de finish() asegura que se cierran todas las activities
        finishAffinity();
    }
}