package com.tfg.eventos.listeners;


import java.util.EventListener;

public interface PartidaCazaEventListener extends EventListener {
    void onTimerTick();

    void onFinalizarPartida();
}
