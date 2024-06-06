package com.tfg.eventos.callbacks;

import com.google.firebase.database.DatabaseError;
import com.tfg.bbdd.dto.UsuarioDTO;

import java.util.List;

public interface ObtenerRankingCallback {
    void onExito(List<UsuarioDTO> ranking);

    void onError(DatabaseError databaseError);
}
