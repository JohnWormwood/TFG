package com.tfg.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.tfg.R;
import com.tfg.utilidades.GestorSharedPreferences;

public class OpcionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
    }

    public void buttonCerrarSesionOnClick(View view) {
        FirebaseAuth.getInstance().signOut();
        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this, getString(R.string.prefs_file));
        gestorSharedPreferences.getEditor().clear();
        gestorSharedPreferences.getEditor().apply();

        finish();
    }
}