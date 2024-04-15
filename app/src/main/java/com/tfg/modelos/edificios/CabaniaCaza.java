package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.RecursosEnum;
import com.tfg.utilidades.Constantes;

import java.util.Map;


public class CabaniaCaza extends Edificio {
    public CabaniaCaza(int nivel, int aldeanosAsignados, int aldeanosMaximos, int segundosEntreRecursos, Aldea aldea) {
        super(nivel, aldeanosAsignados, aldeanosMaximos, segundosEntreRecursos, aldea);
    }

    @Override
    public void producirRecursos() {

    }

}
