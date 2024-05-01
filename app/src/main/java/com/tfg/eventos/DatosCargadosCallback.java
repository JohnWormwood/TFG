package com.tfg.eventos;

// Esta interfaz se utiliza para sincronizar la carga y el guardado de datos con firebase
public interface DatosCargadosCallback {
    void onDatosCargados();
    void onDatosGuardados();
}
