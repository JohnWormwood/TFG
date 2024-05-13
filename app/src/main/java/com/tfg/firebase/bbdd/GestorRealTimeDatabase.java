package com.tfg.firebase.bbdd;

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
    private final static String PATH_ONLINE = "online";

    private FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
    private DatabaseReference usuariosRef = baseDatos.getReference(PATH_USUARIOS);

    public void actualizarEstadoConexion(boolean online) {
        String idUsuario = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference idUsuarioRef = usuariosRef.child(idUsuario);
        idUsuarioRef.child(PATH_ONLINE).setValue(online); // Establece el valor como "conectado"

        /* En caso de desconexion ponerlo a false, cuando es una desconexion
         * no controlada puede tardar un tiempo en actualizarse en la base de datos
         */
        idUsuarioRef.child(PATH_ONLINE).onDisconnect().setValue(false);
    }

    public void mostrarUsuariosConectados() {
        Query onlineUsersQuery = usuariosRef.orderByChild("online").equalTo(true);

        onlineUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> onlineUserIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();
                    onlineUserIds.add(userId);
                }

                System.out.println("USUARIOS CONECTADOS: ");
                for (String id : onlineUserIds) {
                    System.out.println("USER ID = "+id);
                }

                /*
                // Si hay usuarios en línea, seleccionar uno aleatoriamente
                if (!onlineUserIds.isEmpty()) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(onlineUserIds.size());
                    String randomUserId = onlineUserIds.get(randomIndex);
                    Log.d("RandomUserId", "El userId aleatorio seleccionado es: " + randomUserId);
                } else {
                    Log.d("RandomUserId", "No hay usuarios en línea en la base de datos.");
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RandomUserId", "Error al realizar la consulta en Firebase Realtime Database: " + databaseError.getMessage());
            }
        });
    }
}
