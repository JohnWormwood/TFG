package com.tfg.modelos.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum RecursosEnum {
    TRONCOS_MADERA(1),
    TABLONES_MADERA(2),
    COMIDA(2),
    PIEDRA(1),
    HIERRO(2),
    ORO(3);

    private final int CALIDAD;
    @Setter
    private int max;

    RecursosEnum(int CALIDAD) {
        this.CALIDAD = CALIDAD;
        max = 10;
    }
}
