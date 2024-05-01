package com.tfg.utilidades;

import java.util.ArrayList;
import java.util.List;

public class ListaHilos {
    private static List<Thread> listaHilos = new ArrayList<>();
    private static final Object LOCK = new Object();

    public static void add(Thread thread) {
        listaHilos.add(thread);
        System.out.println(thread.getName()+" añadido a la lista");
        System.out.println("[ListaHilos] hilosActivos = "+listaHilos.size());
    }

    public static void remove(Thread thread) {
        synchronized (LOCK) {
            listaHilos.remove(thread);
            System.out.println(thread.getName()+" eliminado de la lista");
            System.out.println("[ListaHilos] hilosActivos = "+listaHilos.size());
        }
    }

    public static void interrumpirTodos() {
        synchronized (LOCK) {
            for (Thread thread : listaHilos) {
                if (thread != null && thread.isAlive()) {
                    thread.interrupt();
                    System.out.println(thread.getName()+" interrumpido");
                }
            }
            System.out.println("[ListaHilos] hilosActivos = "+listaHilos.size());
        }

    }
}
