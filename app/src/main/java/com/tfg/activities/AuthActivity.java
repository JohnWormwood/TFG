package com.tfg.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.tfg.R;
import com.tfg.firebase.auth.GestorSesion;
import com.tfg.firebase.bbdd.GestorBaseDatos;
import com.tfg.utilidades.UtilidadActivity;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    // Componentes interfaz
    private Button buttonRegistro, buttonLogin;
    private EditText editTextEmail, editTextPassword;
    private LinearLayout authLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Inicializar componentes de la interfaz
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegistro = findViewById(R.id.buttonRegistro);
        buttonLogin = findViewById(R.id.buttonLogin);
        authLayout = findViewById(R.id.authLayout);

        // Cargar los listeners
        buttonRegistro.setOnClickListener(buttonRegistroOnClick);
        buttonLogin.setOnClickListener(buttonLoginOnClick);

        comprobarSesion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        authLayout.setVisibility(View.VISIBLE);
    }

    private void comprobarSesion() {
        String email = GestorSesion.cargarSesionLocal(this, getString(R.string.prefs_file));

        if (email != null) {
            authLayout.setVisibility(View.INVISIBLE);
            Bundle extras = new Bundle();
            extras.putString("email", email);
            UtilidadActivity.lanzarIntent(this, MenuActivity.class, extras);
        }
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
    };

    private final View.OnClickListener buttonLoginOnClick = v -> {
        String email = String.valueOf(editTextEmail.getText()) , password = String.valueOf(editTextPassword.getText());

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
    };
}