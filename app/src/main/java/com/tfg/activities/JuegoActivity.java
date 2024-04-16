package com.tfg.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tfg.R;
import com.tfg.controladores.ControladorAldea;
import com.tfg.utilidades.Utilidades;

public class JuegoActivity extends AppCompatActivity {

    public static boolean enEjecucion = true;

    // Componentes interfaz
    private TextView textViewAldeanos;
    private TextView textViewComida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        // Inicializar los componentes de la interfaz
        textViewAldeanos = findViewById(R.id.textViewAldeanos);
        textViewComida = findViewById(R.id.textViewComida);

        // Iniciar el juego
        ejecutarHiloPrincipal();
    }

    private void ejecutarHiloPrincipal() {
        // Iniciar un hilo secundario para ejecutar el código continuamente
        new Thread(new Runnable() {
            @Override
            public void run() {
                actulizarUI();
                while (enEjecucion) {
                    // Logica del juego
                    ControladorAldea.generarAldeano();
                    Utilidades.esperar(1);


                    // Actualizar la interfaz al final de cada ciclo
                    actulizarUI();
                }
            }
        }).start();
    }

    private void actulizarUI() {
        // Actualizar la interfaz de usuario desde el hilo principal
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewAldeanos.setText(" " + ControladorAldea.getPoblacion());
                textViewComida.setText(" " + ControladorAldea.getComida());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener el bucle infinito cuando la actividad se destruye para evitar problemas de memoria
        enEjecucion = false;
    }
}