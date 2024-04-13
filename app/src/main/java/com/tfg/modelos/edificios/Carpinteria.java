package com.tfg.modelos.edificios;

import java.util.Map;

public class Carpinteria extends Edificio{
    private int tablones;

    public Carpinteria(int nivel, int aldeanosAsignados, int aldeanosMaximos, Map<String, Integer> recursosGenerados) {
        super(nivel, aldeanosAsignados, aldeanosMaximos, recursosGenerados);
    }

    @Override
    public void producirRecursos() {

    }


}
