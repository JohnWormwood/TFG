package com.tfg.modelos.edificios;

import java.util.Map;


public class CabaniaCaza extends Edificio {
    private int comida;

    public CabaniaCaza(int nivel, int aldeanosAsignados, int aldeanosMaximos, Map<String, Integer> recursosGenerados, boolean produciendoRecursos) {
        super(nivel, aldeanosAsignados, aldeanosMaximos, recursosGenerados);
    }

    @Override
    public void producirRecursos() {

    }

}
