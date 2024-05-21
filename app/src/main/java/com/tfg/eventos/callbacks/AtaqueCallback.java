package com.tfg.eventos.callbacks;

public interface AtaqueCallback {
    void onAtaqueTerminado(boolean victoria);
    void onError(Exception e);
}