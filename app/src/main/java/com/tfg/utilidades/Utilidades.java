package com.tfg.utilidades;

public final class Utilidades {
    public static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
