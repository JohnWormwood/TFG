package com.tfg.controladores;

import com.tfg.modelos.enums.RecursosEnum;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

public final class ControladorRecursos {

    public static synchronized void agregarRecurso(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int cantidad) {
        Integer cantidadActual = recursos.get(recurso);
        if (cantidadActual != null) {
            recursos.put(recurso, cantidadActual+cantidad);
        } else recursos.put(recurso, cantidad);
    }

    public static synchronized boolean consumirRecurso(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int cantidad) {
        Integer cantidadActual = recursos.get(recurso);
        if (cantidadActual != null) {
            /*System.out.println("--- consumirRecurso ---");
            System.out.println("recurso = "+recurso);
            System.out.println("cantidadActual = "+cantidadActual);
            System.out.println("cantidad = "+cantidad);
            System.out.println("-----------------------");*/
            if (cantidad <= cantidadActual) {
                recursos.put(recurso, cantidadActual-cantidad);
                return true;
            }
        } else recursos.put(recurso, 0);
        return false;
    }
}
