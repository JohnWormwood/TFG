package com.tfg.utilidades;

import java.util.Random;

public final class Utilidades {
    public static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int generarIntRandom(int minimo, int maximo) {
        return new Random().nextInt(maximo+1)+minimo;
    }
}
