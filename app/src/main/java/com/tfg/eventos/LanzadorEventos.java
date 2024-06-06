package com.tfg.eventos;

import android.util.Log;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class LanzadorEventos<T extends EventListener> {
    // Se usa un set y no una lista para evitar duplicados
    protected Set<T> listeners = new HashSet<>();

    public void addEventListener(T listener) {
        listeners.add(listener);
    }

    public void removeEventListener(T listener) {
        listeners.remove(listener);
    }

    // Consumer<T> permite pasar cualquier funcion que
    // del listener de tipo T, pero no de otros listeners
    public void lanzarEvento(Consumer<T> action) {
        listeners.forEach(action);
        Log.d(getClass().getSimpleName(), "Evento lanzado: "+ action);
    }
}
