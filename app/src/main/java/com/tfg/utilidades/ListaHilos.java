package com.tfg.utilidades;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ListaHilos {
    private static List<Thread> listaHilos = new ArrayList<>();
    private static final Object LOCK = new Object();

    public static void add(Thread thread) {
        listaHilos.add(thread);
        Log.d(ListaHilos.class.getSimpleName(), thread.getName()+" añadido a la lista");
        Log.d(ListaHilos.class.getSimpleName(), "hilosActivos = "+listaHilos.size());
    }

    public static void remove(Thread thread) {
        synchronized (LOCK) {
            listaHilos.remove(thread);
            Log.d(ListaHilos.class.getSimpleName(), thread.getName()+" eliminado de la lista");
            Log.d(ListaHilos.class.getSimpleName(), "hilosActivos = "+listaHilos.size());
        }
    }

    public static void interrumpirTodos() {
        synchronized (LOCK) {
            for (Thread thread : listaHilos) {
                if (thread != null && thread.isAlive()) {
                    thread.interrupt();
                    Log.d(ListaHilos.class.getSimpleName(), thread.getName()+" interrumpido");
                }
            }
            Log.d(ListaHilos.class.getSimpleName(), "hilosActivos = "+listaHilos.size());
        }

    }
}
