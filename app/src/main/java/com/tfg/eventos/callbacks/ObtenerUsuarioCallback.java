package com.tfg.eventos.callbacks;

import com.google.firebase.database.DatabaseError;

public interface ObtenerUsuarioCallback {
    void onExito(String email);
    void onError(DatabaseError databaseError);
}
