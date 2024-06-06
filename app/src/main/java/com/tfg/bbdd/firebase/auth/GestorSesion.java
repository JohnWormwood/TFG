package com.tfg.bbdd.firebase.auth;

import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;

import java.util.function.Consumer;

public final class GestorSesion {

    public static void registrarUsuario(String email, String password,
                                        Consumer<Task<AuthResult>> consumer) {
        if (!Strings.isNullOrEmpty(email) && !Strings.isNullOrEmpty(password)) {
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(consumer::accept);
        }
    }

    public static void iniciarSesion(String email, String password,
                                     Consumer<Task<AuthResult>> consumer) {
        if (!Strings.isNullOrEmpty(email) && !Strings.isNullOrEmpty(password)) {
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(consumer::accept);
        }
    }

    public static void cerrarSesion() {
        // Borrar el token fcm de la base de datos
        // para evitar que se reciban notifiaciones de otros usuarios
        GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
        gestorRealTimeDatabase.actualizarTokenFcm("");

        FirebaseAuth.getInstance().signOut();
    }

    public static String cargarSesionLocal() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return (firebaseUser != null) ? firebaseUser.getEmail() : null;
    }
}
