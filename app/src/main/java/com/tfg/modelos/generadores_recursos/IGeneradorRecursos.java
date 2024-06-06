package com.tfg.modelos.generadores_recursos;

import com.tfg.modelos.enums.RecursosEnum;

import java.util.Map;

public interface IGeneradorRecursos {
    void producirRecursos(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int aldeanosAsignados);
    RecursosEnum getRecurso();
}
