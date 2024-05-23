package com.tfg.bbdd.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tfg.bbdd.dto.UsuarioDTO;
import com.tfg.bbdd.firebase.service.NotificacionesService;
import com.tfg.eventos.callbacks.ObtenerUsuarioCallback;
import com.tfg.modelos.Aldea;

import java.util.ArrayList;
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
        idUsuarioRef.child(PATH_ONLINE).onDisconnect().setValue(Aldea.getInstance().getCastillo().isDesbloqueado());
    }

    public void actualizarTokenFmc(String token) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            String idUsuario = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference idUsuarioRef = usuariosRef.child(idUsuario);
            idUsuarioRef.child(PATH_TOKEN_FCM).setValue(token);
        }
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

                // Si hay usuarios en línea, seleccionar uno aleatoriamente
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
}
