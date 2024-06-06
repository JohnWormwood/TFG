package com.tfg.activities;

import static com.tfg.utilidades.UtilidadActivity.setEfectoBoton;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
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
    // Componentes interfaz
    private TextView textViewEmail;
    private String email;

    // Referencia soundmanager
    private SoundManager soundManager;

    // Base de datos
    private GestorRealTimeDatabase gestorRealTimeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        gestorRealTimeDatabase = new GestorRealTimeDatabase();

        soundManager = SoundManager.getInstance(this);
        soundManager.playMusica(R.raw.musica_mediaval);

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
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onStart() {
        super.onStart();

        comprobarSesion();
    }

    private void configInicial(String email) {
        textViewEmail.setText(email);
    }

    public void buttonJugarOnClick(View view) {
        if (UtilidadRed.hayInternet(this)) {
            gestorRealTimeDatabase.comprobarEstadoConexion(this);
        } else Toast.makeText(this,
                getString(R.string.msj_internet_necesario), Toast.LENGTH_LONG).show();
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
                finish();
            }

            // Guardar el token fcm en el usuario actual
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
        Toast.makeText(this,
                databaseError.toException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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