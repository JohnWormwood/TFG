package com.tfg.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.tfg.R;
import com.tfg.utilidades.GestorSharedPreferences;

public class MenuActivity extends AppCompatActivity {

    private Intent intent;
    private TextView textViewEmail;
    private Button buttonCerrarSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton buttonJugar = findViewById(R.id.buttonJugar);
        TextView textViewJugar = findViewById(R.id.textViewJugar);
        setEfectoBoton(buttonJugar, textViewJugar);

        ImageButton buttonOpciones = findViewById(R.id.buttonOpciones);
        TextView textViewOpciones = findViewById(R.id.textViewOpciones);
        setEfectoBoton(buttonOpciones, textViewOpciones);

        ImageButton buttonSalir = findViewById(R.id.buttonSalir);
        TextView textViewSalir = findViewById(R.id.textViewSalir);
        setEfectoBoton(buttonSalir, textViewSalir);

        textViewEmail = findViewById(R.id.textViewEmail);
        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String email = bundle.getString("email");
        configInicial(email);


        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this, getString(R.string.prefs_file));
        gestorSharedPreferences.getEditor().putString("email", email).apply();
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

    private void configInicial(String email) {
        textViewEmail.setText(email);
        buttonCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.clear();
            prefsEditor.apply();

            finish();
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