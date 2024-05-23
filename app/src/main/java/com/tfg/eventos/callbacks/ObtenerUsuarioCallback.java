package com.tfg.eventos.callbacks;

import com.google.firebase.database.DatabaseError;
import com.tfg.bbdd.dto.UsuarioDTO;

public interface ObtenerUsuarioCallback {
    void onExito(UsuarioDTO usuarioDTO);
    void onError(DatabaseError databaseError);
}
