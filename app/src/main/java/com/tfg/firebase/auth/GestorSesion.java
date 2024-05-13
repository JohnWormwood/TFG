package com.tfg.firebase.auth;

import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.function.Consumer;

public final class GestorSesion {

    public static void registrarUsuario(String email, String password, Consumer<Task<AuthResult>> consumer) {
        if (!Strings.isNullOrEmpty(email) && !Strings.isNullOrEmpty(password)) {
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        consumer.accept(task);
                    });
        }
    }

    public static void iniciarSesion(String email, String password, Consumer<Task<AuthResult>> consumer) {
        if (!Strings.isNullOrEmpty(email) && !Strings.isNullOrEmpty(password)) {
             FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        consumer.accept(task);
                    });
        }
    }

    public static void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
    }

    public static String cargarSesionLocal() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return (firebaseUser != null) ? firebaseUser.getEmail() : null;
    }
}
