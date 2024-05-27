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
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg.bbdd.dto.UsuarioDTO;
import com.tfg.bbdd.firebase.service.NotificacionesService;
import com.tfg.eventos.callbacks.ObtenerUsuarioCallback;
import com.tfg.modelos.Aldea;
import com.tfg.utilidades.Constantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class GestorRealTimeDatabase {
    private final static String PATH_USUARIOS = "usuarios";
    private final static String PATH_EMAIL = "email";
    private final static String PATH_CASTILLO = "castillo";
    private final static String PATH_ONLINE = "online";
    private final static String PATH_TOKEN_FCM = "token_fcm";
    private final static String PATH_PUNTOS = "puntos";

    private FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
    private DatabaseReference usuariosRef = baseDatos.getReference(PATH_USUARIOS);

    public void actualizarEstadoConexion(boolean online) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String idUsuario = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DatabaseReference idUsuarioRef = usuariosRef.child(idUsuario);
        idUsuarioRef.child(PATH_ONLINE).setValue(online);
        idUsuarioRef.child(PATH_CASTILLO).setValue(Aldea.getInstance().getCastillo().isDesbloqueado());
        idUsuarioRef.child(PATH_EMAIL).setValue(firebaseAuth.getCurrentUser().getEmail());
        idUsuarioRef.child(PATH_TOKEN_FCM).setValue(NotificacionesService.getToken());

        /* En caso de desconexion ponerlo a false, cuando es una desconexion
         * no controlada puede tardar un tiempo en actualizarse en la base de datos
         */
        idUsuarioRef.child(PATH_ONLINE).onDisconnect().setValue(false);
        idUsuarioRef.child(PATH_CASTILLO).onDisconnect().setValue(Aldea.getInstance().getCastillo().isDesbloqueado());
    }

    public void actualizarTokenFcm(String token) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        NotificacionesService.setToken(token);
        if (firebaseAuth.getCurrentUser() != null) {
            System.out.println("NO ES NULL");
            System.out.println("TOKEN = "+token);
            String idUsuario = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference idUsuarioRef = usuariosRef.child(idUsuario);
            idUsuarioRef.child(PATH_TOKEN_FCM).setValue(NotificacionesService.getToken());
        } else System.out.println("ES NULL");
    }

    public void comprobarEstadoConexion(ObtenerUsuarioCallback callback) {

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Query usuarioActualQuery = usuariosRef.child(uid);
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioActualQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioDTO.setOnline(Optional.ofNullable(snapshot.child(PATH_ONLINE).getValue(Boolean.class)).orElse(false));
                callback.onExito(usuarioDTO);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }

    public void getUsuarioAtacableAleatorio(ObtenerUsuarioCallback callback) {
        Query onlineUsersQuery = usuariosRef.orderByChild(PATH_ONLINE).equalTo(false);
        onlineUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UsuarioDTO> usuariosDesconectados = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UsuarioDTO usuarioDTO = new UsuarioDTO();
                    usuarioDTO.setUid(snapshot.getKey());
                    usuarioDTO.setEmail(snapshot.child(PATH_EMAIL).getValue(String.class));
                    usuarioDTO.setTokenFmc(snapshot.child(PATH_TOKEN_FCM).getValue(String.class));
                    usuarioDTO.setCastillo(Optional.ofNullable(snapshot.child(PATH_CASTILLO).getValue(Boolean.class)).orElse(false));
                    usuarioDTO.setOnline(Optional.ofNullable(snapshot.child(PATH_ONLINE).getValue(Boolean.class)).orElse(false));

                    if (usuarioDTO.isCastillo()) usuariosDesconectados.add(usuarioDTO);
                }

                // Si hay usuarios desconectados, seleccionar uno aleatoriamente
                if (!usuariosDesconectados.isEmpty()) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(usuariosDesconectados.size());
                    callback.onExito(usuariosDesconectados.get(randomIndex));
                } else {
                    System.out.println("No hay usuarios desconectados");
                    callback.onExito(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.err.println(databaseError.getMessage());
                callback.onError(databaseError);
            }
        });
    }
    public void modificarPuntuacionUsuarioActual(int puntuacion) {
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        if (email != null) {
            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            Query usuarioActualQuery = usuariosRef.child(uid);
            UsuarioDTO usuarioDTO = new UsuarioDTO();

            usuarioActualQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    usuarioDTO.setPuntos(Optional.ofNullable(snapshot.child(PATH_PUNTOS).getValue(Integer.class)).orElse(0));
                    DatabaseReference idUsuarioRef = usuariosRef.child(uid);
                    idUsuarioRef.child(PATH_PUNTOS).setValue(Math.max(usuarioDTO.getPuntos()+puntuacion, 0));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Error", error.getMessage(), error.toException());
                }
            });
        }
    }

    public void printRanking() {
        Query onlineUsersQuery = usuariosRef.getRef();
        onlineUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("--- RANKING ---");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UsuarioDTO usuarioDTO = new UsuarioDTO();
                    usuarioDTO.setEmail(snapshot.child(PATH_EMAIL).getValue(String.class));
                    usuarioDTO.setPuntos(Optional.ofNullable(snapshot.child(PATH_PUNTOS).getValue(Integer.class)).orElse(0));

                    System.out.println("Usuario: "+usuarioDTO.getEmail()+", Puntos: "+usuarioDTO.getPuntos());
                }
                System.out.println("--------------------------------------");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.err.println(databaseError.getMessage());
            }
        });
    }

}
