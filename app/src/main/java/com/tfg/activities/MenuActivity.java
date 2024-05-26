package com.tfg.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tfg.R;
import com.tfg.bbdd.dto.UsuarioDTO;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;
import com.tfg.bbdd.firebase.auth.GestorSesion;
import com.tfg.bbdd.firebase.service.NotificacionesService;
import com.tfg.eventos.callbacks.ObtenerUsuarioCallback;
import com.tfg.utilidades.SoundManager;
import com.tfg.utilidades.UtilidadActivity;
import com.tfg.utilidades.UtilidadRed;

public class MenuActivity extends AppCompatActivity implements ObtenerUsuarioCallback {

    private TextView textViewEmail;
    private String email;
    private SoundManager soundManager;
    private GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        soundManager = SoundManager.getInstance(this);

        soundManager.playSound1(R.raw.musica_mediaval);


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

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        email = bundle.getString("email");
        configInicial(email);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                GestorSesion.cerrarSesion();
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        comprobarSesion();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setEfectoBoton(ImageButton button, TextView textView) {
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

    private void configInicial(String email) {
        textViewEmail.setText(email);
    }



    public void buttonJugarOnClick(View view) {
        gestorRealTimeDatabase.comprobarEstadoConexion(this);
    }

    public void buttonOpcionesOnClick(View view) {
        UtilidadActivity.lanzarIntent(this, OpcionesActivity.class, null);
    }

    public void buttonSalirOnClick(View view) {
        // Usar este en lugar de finish() asegura que se cierran todas las activities
        finishAffinity();
    }

    private void comprobarSesion() {
        if (UtilidadRed.hayInternet(this)) {
            String email = GestorSesion.cargarSesionLocal();

            if (email == null) {
                //authLayout.setVisibility(View.INVISIBLE);
                finish();
            }

            // Guardar el token fcm en el usuario actual
            GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
            gestorRealTimeDatabase.actualizarTokenFcm(NotificacionesService.getToken());
        } else {
            GestorSesion.cerrarSesion();
            finish();
        }
    }

    // --- IMPLEMENTACION DE ObtenerUsuarioCallback ---
    @Override
    public void onExito(UsuarioDTO usuarioDTO) {
        // Si el usuario ya esta conectado no podra jugar
        if (usuarioDTO.isOnline()) {
            Toast.makeText(this, "Ya estas conectado", Toast.LENGTH_SHORT).show();
        } else {
            Bundle extras = new Bundle();
            extras.putString("email", email);
            UtilidadActivity.lanzarIntent(this, JuegoActivity.class, extras);
        }
    }

    @Override
    public void onError(DatabaseError databaseError) {
        Toast.makeText(this, databaseError.toException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundManager soundManager = SoundManager.getInstance(this);
        soundManager.getMediaPlayerMusica().pause();
        soundManager.getMediaPlayerEfectos().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SoundManager soundManager = SoundManager.getInstance(this);
        soundManager.getMediaPlayerMusica().start();
        soundManager.getMediaPlayerEfectos().start();
    }

}