package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;

public class Carpinteria extends Edificio{
    public Carpinteria(int nivel, int aldeanosAsignados, int aldeanosMaximos, int segundosEntreRecursos, Aldea aldea) {
        super(nivel, aldeanosAsignados, aldeanosMaximos, segundosEntreRecursos, aldea);
    }

    @Override
    public void producirRecursos() {

    }
}
