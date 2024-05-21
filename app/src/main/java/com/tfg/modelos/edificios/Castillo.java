package com.tfg.modelos.edificios;

import com.google.firebase.database.DatabaseError;
import com.tfg.bbdd.firebase.GestorFirestore;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;
import com.tfg.eventos.callbacks.AtaqueCallback;
import com.tfg.eventos.callbacks.ObtenerUsuarioCallback;
import com.tfg.modelos.Aldea;

public class Castillo extends Edificio implements ObtenerUsuarioCallback, AtaqueCallback {

    private String victimaAtaque;
    private int soldadosEnviados;

    public Castillo(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = false;
        desbloqueado = false;
    }

    @Override
    public void onExito(String email) {
        victimaAtaque = email;
        System.out.println("Usuario a atacar: "+victimaAtaque);
        GestorFirestore gestorFirestore = new GestorFirestore();
        gestorFirestore.gestionarAtaque(email, soldadosEnviados, this);
    }

    @Override
    public void onError(DatabaseError databaseError) {
        System.err.println(databaseError.getMessage());
    }

    public void iniciarIncursion(int soldadosEnviados) {
        GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
        gestorRealTimeDatabase.getUsuarioAtacableAleatorio(this);
        this.soldadosEnviados = soldadosEnviados;
    }

    @Override
    public void onAtaqueTerminado(boolean victoria) {
        if (victoria) System.out.println("HAS GANADO");
        else System.out.println("HAS PERDIDO");
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
