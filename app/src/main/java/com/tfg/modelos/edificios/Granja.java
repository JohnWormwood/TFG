package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.impl.GeneradorEstandar;

public class Granja extends Edificio {
    public Granja(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        desbloqueado = false;
        generarRecursosConstantemente = true;
        generadoresRecursos.add(new GeneradorEstandar(RecursosEnum.COMIDA));
    }
}
