package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.RecursosEnum;

import java.util.Map;


public class CasetaLeniador extends Edificio {

    public CasetaLeniador(int nivel, int aldeanosAsignados, int aldeanosMaximos, int segundosEntreRecursos, Aldea aldea) {
        super(nivel, aldeanosAsignados, aldeanosMaximos, segundosEntreRecursos, aldea);
    }

    @Override
    public void producirRecursos() {

    }

}
