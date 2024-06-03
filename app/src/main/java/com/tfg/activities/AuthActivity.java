package com.tfg.activities;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tfg.R;
import com.tfg.bbdd.firebase.auth.GestorSesion;
import com.tfg.bbdd.firebase.service.NotificacionesService;
import com.tfg.utilidades.UtilidadActivity;
import com.tfg.utilidades.UtilidadRed;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    // Componentes interfaz
    private Button buttonRegistro, buttonLogin;
    private EditText editTextEmail, editTextPassword;
    private LinearLayout authLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Install the splash screen
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
            cargarDatos();
            vaciarEditTexts();
        }
    }


    private boolean comprobarSesion() {
        if (UtilidadRed.hayInternet(this)) {
            String email = GestorSesion.cargarSesionLocal();

            if (email != null) {
                authLayout.setVisibility(View.INVISIBLE);
                Bundle extras = new Bundle();
                extras.putString("email", email);
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
            Log.d(ContentValues.TAG, "El token es "+token);
        });
    }

    private void cargarDatos(){
        setContentView(R.layout.activity_auth);

        // Inicializar componentes de la interfaz
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegistro = findViewById(R.id.buttonRegistro);
        buttonLogin = findViewById(R.id.buttonLogin);


        // Cargar los listeners
        buttonRegistro.setOnClickListener(buttonRegistroOnClick);
        buttonLogin.setOnClickListener(buttonLoginOnClick);
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
        String email = String.valueOf(editTextEmail.getText()) , password = String.valueOf(editTextPassword.getText());

        if (UtilidadRed.hayInternet(this)) {
            GestorSesion.registrarUsuario(email, password, task -> {
                if (task.isSuccessful()) {
                    AuthResult resultado = task.getResult();
                    Bundle extras = new Bundle();
                    extras.putString("email", Objects.requireNonNull(resultado.getUser()).getEmail());
                    UtilidadActivity.lanzarIntent(this, MenuActivity.class, extras);
                } else {
                    mostrarAlerta();
                }
            });
        } else Toast.makeText(this, getString(R.string.msj_internet_necesario), Toast.LENGTH_LONG).show();
    };

    private final View.OnClickListener buttonLoginOnClick = v -> {
        String email = String.valueOf(editTextEmail.getText()) , password = String.valueOf(editTextPassword.getText());

        if (UtilidadRed.hayInternet(this)) {
            GestorSesion.iniciarSesion(email, password, task -> {
                if (task.isSuccessful()) {
                    AuthResult resultado = task.getResult();
                    Bundle extras = new Bundle();
                    extras.putString("email", Objects.requireNonNull(resultado.getUser()).getEmail());
                    UtilidadActivity.lanzarIntent(this, MenuActivity.class, extras);
                } else {
                    mostrarAlerta();
                }
            });
        } else Toast.makeText(this, getString(R.string.msj_internet_necesario), Toast.LENGTH_LONG).show();
    };

    private void vaciarEditTexts() {
        editTextEmail.getText().clear();
        editTextPassword.getText().clear();
    }

}