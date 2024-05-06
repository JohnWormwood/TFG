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

@Data
@Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
public abstract class EstructuraBase {
    protected int nivel;
    protected int aldeanosAsignados;
    protected int aldeanosMaximos;
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
        recursos.put(RecursosEnum.COMIDA, Constantes.Aldea.COMIDA_INICIAL);
        recursos.put(RecursosEnum.TRONCOS_MADERA, 0);
        recursos.put(RecursosEnum.PIEDRA, 0);
        recursos.put(RecursosEnum.TABLONES_MADERA, 0);
        recursos.put(RecursosEnum.HIERRO, 0);
        recursos.put(RecursosEnum.ORO, 0);
    }

    protected void setMaximoAldeanosSegunNivel() throws IllegalArgumentException {
        // TODO Capturar la excepcion donde se llame la funcion
        if (nivel <= Constantes.Edificio.NIVEL_MAXIMO)
            aldeanosMaximos = Constantes.Edificio.AUMENTO_MAX_ALDEANOS_POR_NIVEL * nivel;
        else
            throw new IllegalArgumentException(nivel+" es mayor al nivel maximo permitido ("+Constantes.Edificio.NIVEL_MAXIMO+")");
    }

    public boolean aumentarNivel() {
        int proximoNivel = nivel+1;
        if (ControladorEstructuraBase.puedeSubirDeNivel(preciosMejoras.get(proximoNivel-2))) {
            nivel++;
            setMaximoAldeanosSegunNivel();
            return true;
        }
        return false;
    }
}
