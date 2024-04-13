package com.tfg.modelos.edificios;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public abstract class Edificio extends Thread {
    protected int nivel;
    protected int aldeanosAsignados;
    protected int aldeanosMaximos;
    protected Map<String, Integer> recursosGenerados;

    public abstract void producirRecursos();

    @Override
    public void run() {
        while (true) {
            producirRecursos();
        }
    }
}
