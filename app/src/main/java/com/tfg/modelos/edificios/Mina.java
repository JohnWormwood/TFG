package com.tfg.modelos.edificios;

import java.util.Map;

public class Mina extends Edificio{
    private int piedra;
    private int hierro;
    private int oro;

    public Mina(int nivel, int aldeanosAsignados, int aldeanosMaximos, Map<String, Integer> recursosGenerados) {
        super(nivel, aldeanosAsignados, aldeanosMaximos, recursosGenerados);
    }

    @Override
    public void producirRecursos() {

    }

}
