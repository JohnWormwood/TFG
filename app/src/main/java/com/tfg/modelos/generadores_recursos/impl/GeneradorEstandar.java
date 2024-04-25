package com.tfg.modelos.generadores_recursos.impl;

import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneradorEstandar implements IGeneradorRecursos {
    protected RecursosEnum recurso;
}
