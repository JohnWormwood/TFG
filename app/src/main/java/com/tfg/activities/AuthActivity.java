package com.tfg.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Strings;
import com.google.firebase.auth.FirebaseAuth;
import com.tfg.R;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    private Button buttonRegistro, buttonLogin;
    private EditText editTextEmail, editTextPassword;
    private LinearLayout authLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegistro = findViewById(R.id.buttonRegistro);
        buttonLogin = findViewById(R.id.buttonLogin);
        authLayout = findViewById(R.id.authLayout);

        buttonRegistro.setOnClickListener(buttonRegistroOnClick);
        buttonLogin.setOnClickListener(buttonLoginOnClick);

        sesion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        authLayout.setVisibility(View.VISIBLE);
    }

    private void sesion() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        if (email != null) {
            authLayout.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }
    }


    private final View.OnClickListener buttonRegistroOnClick = v -> {
        String email = String.valueOf(editTextEmail.getText()) , password = String.valueOf(editTextPassword.getText());

        if (!Strings.isNullOrEmpty(email) && !Strings.isNullOrEmpty(email)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(this, MenuActivity.class);
                            intent.putExtra("email", Objects.requireNonNull(task.getResult().getUser()).getEmail());
                            startActivity(intent);
                        } else {
                            mostrarAlerta();
                        }
                    });

        }
    };

    private final View.OnClickListener buttonLoginOnClick = v -> {
        String email = String.valueOf(editTextEmail.getText()) , password = String.valueOf(editTextPassword.getText());

        if (!Strings.isNullOrEmpty(email) && !Strings.isNullOrEmpty(email)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(this, MenuActivity.class);
                            intent.putExtra("email", Objects.requireNonNull(task.getResult().getUser()).getEmail());
                            startActivity(intent);
                        } else {
                            mostrarAlerta();
                        }
                    });

        }
    };

    private void mostrarAlerta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha procucido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}