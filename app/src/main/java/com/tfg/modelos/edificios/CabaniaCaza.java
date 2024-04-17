package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;


public class CabaniaCaza extends Edificio {
    public CabaniaCaza(int nivel, int aldeanosAsignados, int aldeanosMaximos, Aldea aldea) {
        super(nivel, aldeanosAsignados, aldeanosMaximos, 0, aldea);
        generarRecursosConstantemente = false;
    }

    @Override
    public void producirRecursos() {

    }

}
