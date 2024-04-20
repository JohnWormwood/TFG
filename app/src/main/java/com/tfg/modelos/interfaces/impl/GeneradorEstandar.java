package com.tfg.modelos.interfaces.impl;

import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.interfaces.IGeneradorRecursos;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GeneradorEstandar implements IGeneradorRecursos {
    private RecursosEnum recurso;

    @Override
    public RecursosEnum getRecurso() {
        return recurso;
    }
}
