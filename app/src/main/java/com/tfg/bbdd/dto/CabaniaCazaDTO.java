package com.tfg.bbdd.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CabaniaCazaDTO extends EdificioDTO {
    private long segundosRestantes;
    private int aldeanosMuertosEnPartida;
    private boolean partidaActiva;
}
