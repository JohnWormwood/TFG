package com.tfg.utilidades;

import java.util.Random;

public final class Utilidades {
    public static int generarIntRandom(int minimo, int maximo) {
        return new Random().nextInt(maximo + 1) + minimo;
    }
}
