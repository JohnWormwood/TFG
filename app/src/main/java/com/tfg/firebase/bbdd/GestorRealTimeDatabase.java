package com.tfg.firebase.bbdd;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GestorRealTimeDatabase {
    private final static String PATH_USUARIOS = "usuarios";


    private  FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
    private DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference(PATH_USUARIOS);

    public void manejarDesconexion() {
        // Obtén una referencia a la base de datos en tiempo real
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");

        // Cuando el usuario inicie sesión
        String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference idUsuarioRef = usuariosRef.child(idUsuario);
        idUsuarioRef.child("online").setValue(true); // Establece el valor como "conectado"

        // Cuando el usuario se desconecta, automáticamente actualiza su estado a "desconectado"
        idUsuarioRef.child("online").onDisconnect().setValue(false);
    }
}
