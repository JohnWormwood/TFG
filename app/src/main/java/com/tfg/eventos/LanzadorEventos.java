package com.tfg.eventos;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class LanzadorEventos<T extends EventListener> {
    protected List<T> listeners = new ArrayList<>();

    public void addEventListener(T listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeEventListener(T listener) {
        listeners.remove(listener);
    }
}
