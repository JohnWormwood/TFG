package com.tfg.bbdd.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg.eventos.callbacks.OperacionesDatosCallback;

import java.util.HashMap;

public class FirestoreCRUD {
    public static void insertar(String coleccion, String documento, HashMap<String, Object> datos) {
        FirebaseFirestore.getInstance()
                .collection(coleccion)
                .document(documento)
                .set(datos);
    }

    public static void insertarConCallback(String coleccion, String documento, HashMap<String, Object> datos, OperacionesDatosCallback callback) {
        insertar(coleccion, documento, datos);
        callback.onDatosGuardados();
    }

    public static void actualizar(String coleccion, String documento, HashMap<String, Object> datos) {
        FirebaseFirestore.getInstance()
                .collection(coleccion)
                .document(documento)
                .update(datos).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si todavia no hay datos fallara el update, asi que insertamos
                        insertar(coleccion, documento, datos);
                    }
                });
    }

    public static void actualizarConCallback(String coleccion, String documento, HashMap<String, Object> datos, OperacionesDatosCallback callback) {
        actualizar(coleccion, documento, datos);
        callback.onDatosGuardados();
    }
}
