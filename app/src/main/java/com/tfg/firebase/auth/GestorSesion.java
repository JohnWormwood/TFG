package com.tfg.firebase.auth;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tfg.R;
import com.tfg.activities.MenuActivity;
import com.tfg.utilidades.GestorSharedPreferences;
import com.tfg.utilidades.UtilidadActivity;

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

    public static String cargarSesionLocal(Context context, String sharedPreferencesKey) {
        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(context, sharedPreferencesKey);
        return gestorSharedPreferences.getSharedPreferences().getString("email", null);
    }
}
