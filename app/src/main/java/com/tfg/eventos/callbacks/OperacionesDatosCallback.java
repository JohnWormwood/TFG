package com.tfg.eventos.callbacks;

// Esta interfaz se utiliza para sincronizar la carga y el guardado de datos con firebase
public interface OperacionesDatosCallback {
    void onDatosCargados();
    void onDatosGuardados();
}
