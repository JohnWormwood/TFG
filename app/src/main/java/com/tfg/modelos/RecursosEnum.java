package com.tfg.modelos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecursosEnum {
    TRONCOS_MADERA("Troncos"),
    TABLONES_MADERA("Tablones"),
    COMIDA("Comida"),
    PIEDRA("Piedra"),
    HIERRO("Hierro"),
    ORO("Oro");

    private final String recurso;
}
