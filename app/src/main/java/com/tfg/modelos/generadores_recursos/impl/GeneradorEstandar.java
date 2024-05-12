package com.tfg.modelos.generadores_recursos.impl;

import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;
import com.tfg.utilidades.Constantes;


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
            agregarRecursoSinExcederMax(recursos, recurso, cantidad);
        }
    }

    protected void agregarRecursoSinExcederMax(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int cantidad) {
        int cantidadActual = ControladorRecursos.getCantidadRecurso(Aldea.getInstance().getRecursos(), recurso);
        int disponible = recurso.getMax() - cantidadActual;

        ControladorRecursos.agregarRecurso(recursos, recurso, Math.min(cantidad, disponible));
        //System.out.println("Cantidad = " + recurso +", "+Math.min(cantidad, disponible));
    }

    protected int calcularCantidadProducida(int aldeanosAsignados) {
        // Generar el recurso en funcion de los aldeanos asignados y de la calidad del recurso
        int cantidad = (aldeanosAsignados == 1 ? 1 : aldeanosAsignados / 2);
        cantidad = Math.max(cantidad - recurso.getCALIDAD(), 1);
        return cantidad;
    }
}
