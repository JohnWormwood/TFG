package com.tfg.modelos.interfaces.impl;

import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.interfaces.IGeneradorRecursos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneradorEstandar implements IGeneradorRecursos {
    protected RecursosEnum recurso;
}
