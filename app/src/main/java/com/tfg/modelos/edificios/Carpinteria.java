package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.impl.GeneradorCarpinteria;
import com.tfg.utilidades.Constantes;

public class Carpinteria extends Edificio {
    public Carpinteria(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = true;
        generadoresRecursos.add(new GeneradorCarpinteria(RecursosEnum.TABLONES_MADERA));
        desbloqueado = false;
    }
}
