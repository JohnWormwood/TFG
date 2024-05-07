package com.tfg.modelos.generadores_recursos;

import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.enums.RecursosEnum;

import java.util.Map;

public interface IGeneradorRecursos {

    default void producirRecursos(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int aldeanosAsignados) {
        if (aldeanosAsignados > 0) {
            // Generar el recurso en funcion de los aldeanos asignados y de la calidad del recurso
            int cantidad = (aldeanosAsignados == 1 ? 1 : aldeanosAsignados / 2);
            cantidad = Math.max(cantidad - recurso.getCALIDAD(), 1);
            ControladorRecursos.agregarRecurso(recursos, recurso, cantidad);
        }
    }

    RecursosEnum getRecurso();
}
