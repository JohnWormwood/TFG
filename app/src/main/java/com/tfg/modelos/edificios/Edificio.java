package com.tfg.modelos.edificios;

import com.tfg.activities.JuegoActivity;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.RecursosEnum;
import com.tfg.utilidades.Utilidades;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Edificio extends Thread {
    protected int nivel;
    protected int aldeanosAsignados;
    protected int aldeanosMaximos;
    protected int segundosEntreRecursos;
    protected Map<RecursosEnum, Integer> recursosGenerados;
    protected Aldea aldea;

    public Edificio(int nivel, int aldeanosAsignados, int aldeanosMaximos, int segundosEntreRecursos, Aldea aldea) {
        this.nivel = nivel;
        this.aldeanosAsignados = aldeanosAsignados;
        this.aldeanosMaximos = aldeanosMaximos;
        this.segundosEntreRecursos = segundosEntreRecursos;
        this.aldea = aldea;

        this.recursosGenerados = new HashMap<>();
    }

    // Cada tipo de edificio implementara esto de una manera distinta
    public abstract void producirRecursos();

    @Override
    public void run() {
        while (JuegoActivity.enEjecucion) {
            // Genera recursos y espera x tiempo
            producirRecursos();
            esperar();
        }
    }

    protected void esperar() {
        Utilidades.esperar(segundosEntreRecursos);
    }
}
