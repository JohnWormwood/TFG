package com.tfg.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tfg.R;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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