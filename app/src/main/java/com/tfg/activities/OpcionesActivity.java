package com.tfg.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tfg.R;
import com.tfg.bbdd.firebase.auth.GestorSesion;

public class OpcionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
    }

    public void buttonCerrarSesionOnClick(View view) {
        GestorSesion.cerrarSesion();
        finish();
    }
}