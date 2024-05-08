package com.tfg.modelos.generadores_recursos.impl;

import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;

import java.util.Map;

public class GeneradorCarpinteria extends GeneradorEstandar {

    private static final Object lock = new Object();

    public GeneradorCarpinteria(RecursosEnum recurso) {
        super(recurso);
    }

    @Override
    public void producirRecursos(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int aldeanosAsignados) {
        // Por cada 2 troncos genera 1 tablon (si hay troncos suficientes)
        // La comprobacion de aldeanos ya se hace en super.producirRecursos, pero es necesario volverla
        // a hacer para asegurar que se consume el recurso solo si hay minimo 1 asignado
        if (aldeanosAsignados > 0) {
            //System.out.println("CANTIDAD DE TABLONES A GENERAR = "+calcularCantidadProducida(aldeanosAsignados));
            //System.out.println("CANTIDAD DE TRONCOS A CONSUMIR = "+calcularCantidadProducida(aldeanosAsignados)*2);
            if (ControladorRecursos.puedeConsumirRecurso(Aldea.getInstance().getRecursos(), RecursosEnum.TRONCOS_MADERA, calcularCantidadProducida(aldeanosAsignados)*2)) {
                super.producirRecursos(recursos, recurso, aldeanosAsignados);
            }
        }
    }
}
