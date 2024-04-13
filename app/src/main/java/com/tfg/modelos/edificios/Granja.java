package com.tfg.modelos.edificios;

import java.util.Map;

public class Granja extends Edificio{
    private int comida;

    public Granja(int nivel, int aldeanosAsignados, int aldeanosMaximos, Map<String, Integer> recursosGenerados) {
        super(nivel, aldeanosAsignados, aldeanosMaximos, recursosGenerados);
    }

    @Override
    public void producirRecursos() {

    }


}
