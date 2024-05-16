package com.tfg.bbdd.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GestorRealTimeDatabase {
    private final static String PATH_USUARIOS = "usuarios";
    private final static String PATH_EMAIL = "email";
    private final static String PATH_ONLINE = "online";

    private FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
    private DatabaseReference usuariosRef = baseDatos.getReference(PATH_USUARIOS);

    public void actualizarEstadoConexion(boolean online) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String idUsuario = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DatabaseReference idUsuarioRef = usuariosRef.child(idUsuario);
        idUsuarioRef.child(PATH_ONLINE).setValue(online);
        idUsuarioRef.child(PATH_EMAIL).setValue(firebaseAuth.getCurrentUser().getEmail());

        /* En caso de desconexion ponerlo a false, cuando es una desconexion
         * no controlada puede tardar un tiempo en actualizarse en la base de datos
         */
        idUsuarioRef.child(PATH_ONLINE).onDisconnect().setValue(false);
    }

    public String getUsuarioDesconectadoAleatorio() {
        Query onlineUsersQuery = usuariosRef.orderByChild(PATH_ONLINE).equalTo(false);
        final String[] usuario = new String[1];
        onlineUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> usuariosConectados = new ArrayList<>();
                System.out.println("--- USUARIOS DESCONECTADOS ---");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();
                    String email = snapshot.child(PATH_EMAIL).getValue(String.class);
                    System.out.println("uid = "+userId+", email = "+email);
                    usuariosConectados.add(email);
                }

                // Si hay usuarios en línea, seleccionar uno aleatoriamente
                if (!usuariosConectados.isEmpty()) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(usuariosConectados.size());
                    usuario[0] = usuariosConectados.get(randomIndex);
                } else {
                    System.out.println("No hay usuarios desconectados");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RandomUserId", "Error al realizar la consulta en Firebase Realtime Database: " + databaseError.getMessage());
            }
        });

        return usuario[0];
    }
}
