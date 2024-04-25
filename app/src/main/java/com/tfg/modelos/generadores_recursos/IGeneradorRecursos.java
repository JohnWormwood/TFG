package com.tfg.modelos.generadores_recursos;

import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.enums.RecursosEnum;

import java.util.Map;

public interface IGeneradorRecursos {

    default void producirRecursos(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int aldeanosAsignados) {
        if (aldeanosAsignados > 0) {
            int cantidad = (aldeanosAsignados == 1 ? 1 : aldeanosAsignados / 2) + recurso.getCALIDAD();
            ControladorRecursos.agregarRecurso(recursos, recurso, cantidad);
        }
    }

    RecursosEnum getRecurso();
}
