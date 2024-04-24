package com.tfg.utilidades;

import java.util.ArrayList;
import java.util.List;

public class ListaHilos {
    private static List<Thread> listaHilos = new ArrayList<>();
    private static Object lock = new Object();

    public static void add(Thread thread) {
        listaHilos.add(thread);
        System.out.println(thread.getName()+" añadido a la lista");
    }

    public static void remove(Thread thread) {
        synchronized (lock) {
            listaHilos.remove(thread);
            System.out.println(thread.getName()+" eliminado de la lista");
        }
    }

    public static void interrumpirTodos() {
        synchronized (lock) {
            for (Thread thread : listaHilos) {
                if (thread != null && thread.isAlive()) {
                    thread.interrupt();
                    System.out.println(thread.getName()+" interrumpido");
                }
            }
        }
    }
}
