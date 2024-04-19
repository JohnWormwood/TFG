package com.tfg.controladores;

import com.tfg.utilidades.Constantes;

public final class ControladorCabaniaCaza {
    public static int getMaximoCazadoresSegunNivel(int nivel) throws IllegalArgumentException {
        // TODO Capturar la excepcion donde se llame la funcion
        if (nivel <= Constantes.Edificio.NIVEL_MAXIMO)
            return nivel * Constantes.CabaniaCaza.AUMENTO_MAX_CAZADORES_POR_NIVEL;
        else
            throw new IllegalArgumentException(nivel+" es mayor al nivel maximo permitido ("+Constantes.Edificio.NIVEL_MAXIMO+")");
    }
}
