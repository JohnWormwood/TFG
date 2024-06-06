package com.tfg.activities;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tfg.R;
import com.tfg.bbdd.firebase.auth.GestorSesion;
import com.tfg.bbdd.firebase.service.NotificacionesService;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.UtilidadActivity;
import com.tfg.utilidades.UtilidadRed;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    // Componentes interfaz
    private ImageButton buttonRegistro, buttonLogin, buttonSalir;
    private TextView textViewRegistro, textViewLogin, textViewSalir;
    private EditText editTextEmail, editTextPassword;
    private LinearLayout authLayout;

    // --- FUNCIONES PARA CONTROLAR LA ACTIVITY ---
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instalar la SplashScreen
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_auth);
        authLayout = findViewById(R.id.authLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authLayout.setVisibility(View.VISIBLE);

        solicitarTokenFcm(); // Guardar el token fcm en el dispositivo

        // Comprueba la sesion antes de cargar la UI de login
        if (!comprobarSesion()) {
            configInicial();
            vaciarEditTexts();
        }
    }

    // --- FUNCIONES SESION USUARIO ---
    private boolean comprobarSesion() {
        if (UtilidadRed.hayInternet(this)) {
            String email = GestorSesion.cargarSesionLocal();

            if (email != null) {
                authLayout.setVisibility(View.INVISIBLE);
                Bundle extras = new Bundle();
                extras.putString(Constantes.KEY_EMAIL, email);
                UtilidadActivity.lanzarIntent(this, MenuActivity.class, extras);
                return true;
            }
        } else {
            Toast.makeText(this, getString(R.string.msj_internet_necesario), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void solicitarTokenFcm() {
        // Generar token para el servicio de notificaciones
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(ContentValues.TAG, "Error al obtener el token de registro de FCM", task.getException());
                return;
            }
            String token = task.getResult();
            NotificacionesService.setToken(token);
            Log.d(ContentValues.TAG, "El token es " + token);
        });
    }

    // --- FUNCIONES INTERFAZ GRAFICA ---
    private void configInicial() {
        setContentView(R.layout.activity_auth);
        inicializarComponentes();
        cargarListeners();
    }

    private void inicializarComponentes() {
        // Inicializar componentes de la interfaz
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegistro = findViewById(R.id.imageButtonRegistrarse);
        buttonLogin = findViewById(R.id.imageButtonIniciarSesion);
        buttonSalir = findViewById(R.id.buttonSalirAuth);
        textViewRegistro = findViewById(R.id.textViewRegistrarse);
        textViewLogin = findViewById(R.id.textViewLogin);
        textViewSalir = findViewById(R.id.textViewSalir);
        UtilidadActivity.setEfectoBoton(buttonRegistro, textViewRegistro);
        UtilidadActivity.setEfectoBoton(buttonLogin, textViewLogin);
        UtilidadActivity.setEfectoBoton(buttonSalir, textViewSalir);
    }

    private void cargarListeners() {
        // Cargar los listeners
        buttonRegistro.setOnClickListener(buttonRegistroOnClick);
        buttonLogin.setOnClickListener(buttonLoginOnClick);
        buttonSalir.setOnClickListener(v -> finishAffinity());
    }

    private void vaciarEditTexts() {
        editTextEmail.getText().clear();
        editTextPassword.getText().clear();
    }

    private void mostrarAlerta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha procucido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // --- LISTENERS ---
    private final View.OnClickListener buttonRegistroOnClick = v -> {
        String email = String.valueOf(editTextEmail.getText()), password = String.valueOf(editTextPassword.getText());

        if (UtilidadRed.hayInternet(this)) {
            GestorSesion.registrarUsuario(email, password, task -> {
                if (task.isSuccessful()) {
                    AuthResult resultado = task.getResult();
                    Bundle extras = new Bundle();
                    extras.putString(Constantes.KEY_EMAIL, Objects.requireNonNull(resultado.getUser()).getEmail());
                    UtilidadActivity.lanzarIntent(this, MenuActivity.class, extras);
                } else {
                    mostrarAlerta();
                }
            });
        } else
            Toast.makeText(this, getString(R.string.msj_internet_necesario), Toast.LENGTH_LONG).show();
    };

    private final View.OnClickListener buttonLoginOnClick = v -> {
        String email = String.valueOf(editTextEmail.getText()), password = String.valueOf(editTextPassword.getText());

        if (UtilidadRed.hayInternet(this)) {
            GestorSesion.iniciarSesion(email, password, task -> {
                if (task.isSuccessful()) {
                    AuthResult resultado = task.getResult();
                    Bundle extras = new Bundle();
                    extras.putString(Constantes.KEY_EMAIL, Objects.requireNonNull(resultado.getUser()).getEmail());
                    UtilidadActivity.lanzarIntent(this, MenuActivity.class, extras);
                } else {
                    mostrarAlerta();
                }
            });
        } else
            Toast.makeText(this, getString(R.string.msj_internet_necesario), Toast.LENGTH_LONG).show();
    };
}