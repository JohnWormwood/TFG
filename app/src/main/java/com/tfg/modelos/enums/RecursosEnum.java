package com.tfg.modelos.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecursosEnum {
    TRONCOS_MADERA(1),
    TABLONES_MADERA(2),
    COMIDA(2),
    PIEDRA(1),
    HIERRO(2),
    ORO(3);

    private final int CALIDAD;
}
