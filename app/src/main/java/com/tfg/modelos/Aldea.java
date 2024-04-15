package com.tfg.modelos;

import com.tfg.modelos.edificios.Edificio;
import com.tfg.utilidades.Constantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Aldea {
    private int nivel;
    private int poblacion;
    private Map<RecursosEnum, Integer> recursos;

    public Aldea(int nivel, int poblacion) {
        this.nivel = nivel;
        this.poblacion = poblacion;
        recursos = new HashMap<>();

        recursos.put(RecursosEnum.COMIDA, Constantes.Aldea.COMIDA_INICIAL);
    }

    public synchronized boolean consumirRecurso(RecursosEnum recurso, int cantidad) {
        Integer cantidadActual = recursos.get(recurso);

        if (cantidadActual != null && cantidadActual >= cantidad) {
            recursos.put(recurso, cantidadActual - cantidad);
            return true;
        }

        return false;
    }
}
