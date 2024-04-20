package com.tfg.controladores;

import com.tfg.modelos.enums.RecursosEnum;

import java.util.Map;

public final class ControladorRecursos {
    public static void agregarRecurso(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int cantidad) {
        Integer cantidadActual = recursos.get(recurso);
        if (cantidadActual != null) recursos.put(recurso, cantidadActual+cantidad);
        else recursos.put(recurso, 0);
    }

    public static void eliminarRecurso(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int cantidad) {
        Integer cantidadActual = recursos.get(recurso);
        if (cantidadActual != null) {
            if (cantidad <= cantidadActual) {
                recursos.put(recurso, cantidadActual-cantidad);
            }
        } else recursos.put(recurso, 0);
    }
}
