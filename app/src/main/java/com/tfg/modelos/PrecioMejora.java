package com.tfg.modelos;

import com.tfg.modelos.enums.RecursosEnum;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrecioMejora {
    private int troncos;
    private int tablones;
    private int comida;
    private int piedra;
    private int hierro;
    private int oro;

    public Map<RecursosEnum, Integer> getRecursos() {
        Map<RecursosEnum, Integer> recursos = new HashMap<>();
        if (troncos > 0) recursos.put(RecursosEnum.TRONCOS_MADERA, troncos);
        if (tablones > 0) recursos.put(RecursosEnum.TABLONES_MADERA, tablones);
        if (comida > 0) recursos.put(RecursosEnum.COMIDA, comida);
        if (piedra > 0) recursos.put(RecursosEnum.PIEDRA, piedra);
        if (hierro > 0) recursos.put(RecursosEnum.HIERRO, hierro);
        if (oro > 0) recursos.put(RecursosEnum.ORO, oro);

        return recursos;
    }
}
