package com.tfg.controladores;

import com.tfg.modelos.enums.RecursosEnum;

import java.util.Map;

public final class ControladorRecursos {

    public static int getCantidadRecurso(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso) {
        Integer cantidadActual = recursos.get(recurso);
        if (cantidadActual != null) {
            return cantidadActual;
        }
        return 0;
    }

    public static synchronized void agregarRecurso(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int cantidad) {
        recursos.put(recurso, getCantidadRecurso(recursos, recurso)+cantidad);
    }

    public static synchronized boolean puedeConsumirRecurso(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int cantidad) {
        return cantidad <= getCantidadRecurso(recursos, recurso);
    }

    public static synchronized boolean consumirRecurso(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int cantidad) {
        if (puedeConsumirRecurso(recursos, recurso, cantidad)) {
            recursos.put(recurso, getCantidadRecurso(recursos, recurso)-cantidad);
            return true;
        }
        return false;
    }
}
