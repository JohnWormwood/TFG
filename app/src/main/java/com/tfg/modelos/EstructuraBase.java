package com.tfg.modelos;

import com.tfg.controladores.ControladorEstructuraBase;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
public abstract class EstructuraBase {
    protected int nivel;
    protected int aldeanosAsignados;
    protected int aldeanosMaximos;
    protected int multiplicadorAldeanosSegunNivel;
    protected Thread thread;
    protected Map<RecursosEnum, Integer> recursos;
    protected List<PrecioMejora> preciosMejoras;

    protected EstructuraBase() {
        // Nivel
        nivel = Constantes.NIVEL_INICIAL;
        setMaximoAldeanosSegunNivel();

        // Precios mejoras
        preciosMejoras = new ArrayList<>();

        // Recursos
        recursos = new HashMap<>();
        recursos.put(RecursosEnum.COMIDA, 0);
        recursos.put(RecursosEnum.TRONCOS_MADERA, 0);
        recursos.put(RecursosEnum.PIEDRA, 0);
        recursos.put(RecursosEnum.TABLONES_MADERA, 0);
        recursos.put(RecursosEnum.HIERRO, 0);
        recursos.put(RecursosEnum.ORO, 0);
    }

    public void reiniciarDatos() {
        // Nivel
        nivel = Constantes.NIVEL_INICIAL;
        setMaximoAldeanosSegunNivel();

        // Precios mejoras
        preciosMejoras = new ArrayList<>();

        // Recursos
        recursos = new HashMap<>();
        recursos.put(RecursosEnum.COMIDA, 0);
        recursos.put(RecursosEnum.TRONCOS_MADERA, 0);
        recursos.put(RecursosEnum.PIEDRA, 0);
        recursos.put(RecursosEnum.TABLONES_MADERA, 0);
        recursos.put(RecursosEnum.HIERRO, 0);
        recursos.put(RecursosEnum.ORO, 0);
    }

    protected void setMaximoAldeanosSegunNivel() throws IllegalArgumentException {
        // TODO Capturar la excepcion donde se llame la funcion
        if (nivel <= Constantes.Estructura.NIVEL_MAXIMO)
            aldeanosMaximos = multiplicadorAldeanosSegunNivel * nivel;
        else
            throw new IllegalArgumentException("Ya se ha alcanzado el nivel maximo");
    }

    public boolean aumentarNivel() throws IllegalArgumentException {
        try {
            int proximoNivel = nivel+1;
            if (ControladorEstructuraBase.puedeSubirDeNivel(preciosMejoras.get(proximoNivel-2))) {
                nivel++;
                setMaximoAldeanosSegunNivel();
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Ya se ha alcanzado el nivel maximo");
        }
        return false;
    }
}
