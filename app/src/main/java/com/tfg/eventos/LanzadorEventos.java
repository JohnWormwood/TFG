package com.tfg.eventos;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public class LanzadorEventos <T extends EventListener> {
    // Se usa un set y no una lista para evitar duplicados
    protected Set<T> listeners = new HashSet<>();

    public void addEventListener(T listener) {
        listeners.add(listener);
    }

    public void removeEventListener(T listener) {
        listeners.remove(listener);
    }

    // Con el Consumer<T> permitira pasarle como argumento cualquier funcion que
    // pertenezca al listener de tipo T, pero no de otros listeners
    public void lanzarEvento(Consumer<T> action) {
        listeners.forEach(action);
    }
}
