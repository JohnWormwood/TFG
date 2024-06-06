package com.tfg.eventos.listeners;

import java.util.EventListener;

public interface AtaqueEventListener extends EventListener {
    void onAtaqueTerminado(boolean victoria);

    void onError(Exception e);
}
