package com.tfg.modelos.edificios;

import com.tfg.activities.JuegoActivity;
import com.tfg.controladores.ControladorAldea;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.interfaces.IGeneradorRecursos;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public abstract class Edificio implements Runnable {
    protected final int SEGUNDOS_ENTRE_RECURSOS = 10;

    protected int nivel;
    protected int aldeanosAsignados;
    protected int aldeanosMaximos;
    protected boolean generarRecursosConstantemente;
    protected Aldea aldea;
    protected Thread thread;
    protected Map<RecursosEnum, Integer> recursosGenerados;
    protected List<IGeneradorRecursos> generadoresRecursos;


    public Edificio(int aldeanosAsignados, Aldea aldea) {
        this.nivel = Constantes.NIVEL_INICIAL;
        this.aldeanosAsignados = aldeanosAsignados;
        this.aldea = aldea;

        this.recursosGenerados = new HashMap<>();

        generadoresRecursos = new ArrayList<>();
        setMaximoAldeanosSegunNivel();
    }

    protected void setMaximoAldeanosSegunNivel() throws IllegalArgumentException {
        // TODO Capturar la excepcion donde se llame la funcion
        if (nivel <= Constantes.Edificio.NIVEL_MAXIMO)
            aldeanosMaximos += Constantes.Edificio.AUMENTO_MAX_CAZADORES_POR_NIVEL;
        else
            throw new IllegalArgumentException(nivel+" es mayor al nivel maximo permitido ("+Constantes.Edificio.NIVEL_MAXIMO+")");
    }

    protected void transferirRecursoAldea(RecursosEnum recurso) {
        Integer recursos = recursosGenerados.get(recurso);
        if (recursos != null) {
            ControladorRecursos.agregarRecurso(aldea.getRecursos(), recurso, recursos);
            ControladorRecursos.eliminarRecurso(recursosGenerados, recurso, recursos);
        }
    }

    @Override
    public void run() {
        while (JuegoActivity.enEjecucion) {
            System.out.println("EMPIEZA HILO");
            try {
                // Genera recursos y espera x tiempo
                for (IGeneradorRecursos generadorRecursos : generadoresRecursos) {
                    generadorRecursos.producirRecursos(recursosGenerados, generadorRecursos.getRecurso(), aldeanosAsignados);
                }
                Thread.sleep(SEGUNDOS_ENTRE_RECURSOS * 1000);
                for (IGeneradorRecursos generadorRecursos : generadoresRecursos) {
                    transferirRecursoAldea(generadorRecursos.getRecurso());
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void iniciar() {
        if (generarRecursosConstantemente) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void aumentarNivel() {
        if (nivel < Constantes.Edificio.NIVEL_MAXIMO) {
            nivel++;
            setMaximoAldeanosSegunNivel();
        }
    }
}