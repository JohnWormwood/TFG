package com.tfg.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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