package com.tfg.modelos.generadores_recursos.impl;

import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;


import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneradorEstandar implements IGeneradorRecursos {
    protected RecursosEnum recurso;

    @Override
    public void producirRecursos(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int aldeanosAsignados) {
        if (aldeanosAsignados > 0) {
            int cantidad = calcularCantidadProducida(aldeanosAsignados);
            ControladorRecursos.agregarRecurso(recursos, recurso, cantidad);
            //System.out.println("Producido: ("+recurso+", "+cantidad+")");
        }
    }

    protected int calcularCantidadProducida(int aldeanosAsignados) {
        // Generar el recurso en funcion de los aldeanos asignados y de la calidad del recurso
        int cantidad = (aldeanosAsignados == 1 ? 1 : aldeanosAsignados / 2);
        cantidad = Math.max(cantidad - recurso.getCALIDAD(), 1);
        return cantidad;
    }
}
