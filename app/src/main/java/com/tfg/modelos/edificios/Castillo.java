package com.tfg.modelos.edificios;

import com.google.firebase.database.DatabaseError;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;
import com.tfg.eventos.callbacks.ObtenerUsuarioCallback;
import com.tfg.modelos.Aldea;

public class Castillo extends Edificio implements ObtenerUsuarioCallback {
    public Castillo(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = false;
        desbloqueado = false;
    }

    @Override
    public void onExito(String email) {
        System.out.println("Usuario a atacar: "+email);
    }

    @Override
    public void onError(DatabaseError databaseError) {
        System.err.println(databaseError.getMessage());
    }

    public void iniciarIncursion() {
        GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
        gestorRealTimeDatabase.getUsuarioAtacableAleatorio(this);
    }

}
