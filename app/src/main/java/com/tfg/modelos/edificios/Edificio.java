package com.tfg.modelos.edificios;

import android.util.Log;

import com.tfg.activities.JuegoActivity;
import com.tfg.controladores.ControladorAldea;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.EstructuraBase;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;
import com.tfg.modelos.generadores_recursos.impl.GeneradorEstandar;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.ListaHilos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;


@Getter(onMethod_ = {@Synchronized})
@Setter(onMethod_ = {@Synchronized})
public abstract class Edificio extends EstructuraBase implements Runnable {
    protected final int SEGUNDOS_ENTRE_RECURSOS = 10;

    protected boolean desbloqueado;
    protected boolean generarRecursosConstantemente;
    protected Aldea aldea;
    protected List<IGeneradorRecursos> generadoresRecursos;

    public Edificio(int aldeanosAsignados, Aldea aldea) {
        super();
        multiplicadorAldeanosSegunNivel = Constantes.Edificio.AUMENTO_MAX_ALDEANOS_POR_NIVEL;
        this.aldeanosAsignados = aldeanosAsignados;
        this.aldea = aldea;
        generadoresRecursos = new ArrayList<>();
        setMaximoRecursosSegunNivel();
        setMaximoAldeanosSegunNivel();
    }

    @Override
    public void reiniciarDatos() {
        super.reiniciarDatos();
        setMaximoRecursosSegunNivel();
        setMaximoAldeanosSegunNivel();
    }

    protected void transferirRecursoAldea(RecursosEnum recurso) {
        int cantidad = ControladorRecursos.getCantidadRecurso(recursos, recurso);
        if (ControladorRecursos.consumirRecurso(recursos, recurso, cantidad)) {
            ControladorRecursos.agregarRecursoSinExcederMax(aldea.getRecursos(), recurso, cantidad);
            Log.d(getClass().getSimpleName(), "Se ha transferido "+recurso+" a la Aldea");
        }
    }

    public synchronized void modificarAldeanosAsignados(int aldeanosAsignados) {
        if (aldeanosAsignados >= 0 && aldeanosAsignados <= aldeanosMaximos) {
            int diferenciaAldeanos;
            if (aldeanosAsignados < this.aldeanosAsignados) {
                diferenciaAldeanos = this.aldeanosAsignados - aldeanosAsignados;
                devolverAldeanos(diferenciaAldeanos);
                this.aldeanosAsignados = aldeanosAsignados;
                reiniciarProduccion();
            } else if (aldeanosAsignados > this.aldeanosAsignados) {
                diferenciaAldeanos = aldeanosAsignados - this.aldeanosAsignados;
                if (ControladorAldea.asignarAldeano(diferenciaAldeanos)) {
                    this.aldeanosAsignados = aldeanosAsignados;
                    reiniciarProduccion();
                }
            }
        }
    }

    protected void devolverAldeanos(int aldeanos) {
        aldea.setPoblacion(aldea.getPoblacion() + aldeanos);
        aldea.setAldeanosAsignados(aldea.getAldeanosAsignados() - aldeanos);
    }

    public synchronized void agregarGeneradorRecurso(RecursosEnum recurso) {
        generadoresRecursos.add(new GeneradorEstandar(recurso));
        setMaximoRecursosSegunNivel();
        reiniciarProduccion();
    }

    @Override
    public void run() {
        ListaHilos.add(thread);
        Map<RecursosEnum, Integer> recursosIniciales = new HashMap<>(recursos);
        try {
            Log.d(getClass().getSimpleName(), "Edificio iniciado");
            while (JuegoActivity.enEjecucion) {
                recursosIniciales = new HashMap<>(recursos);
                // Genera recursos, espera x tiempo y despues los pasa a la aldea
                for (IGeneradorRecursos generadorRecursos : generadoresRecursos) {
                    generadorRecursos.producirRecursos(recursos, generadorRecursos.getRecurso(), aldeanosAsignados);
                }
                Thread.sleep(SEGUNDOS_ENTRE_RECURSOS * 1000);
                finalizarGeneracionRecursos();
            }
        } catch (InterruptedException e) {
            // En caso de interrupcion se vuelve al estado anterior, para evitar que se dupliquen recursos
            Log.d(getClass().getSimpleName(), "Edificio interrumpido");
            recursos = recursosIniciales;
            ListaHilos.remove(thread);
        }
    }

    protected void finalizarGeneracionRecursos() {
        generadoresRecursos.forEach(g -> transferirRecursoAldea(g.getRecurso()));
    }

    public void reiniciarProduccion() {
        if (thread != null) thread.interrupt();
        iniciarProduccion();
    }

    public void iniciarProduccion() {
        /*
         * Solo se iniciaran los edificios que tienen generarRecursosConstantemente = true
         * el resto de edificos no haran nada cuando se llame a iniciarProduccion
         * ya que se asume que producen recursos solo cuando el jugador quiere
         */
        if (generarRecursosConstantemente && aldeanosAsignados > 0 && desbloqueado) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public boolean aumentarNivel() throws IllegalArgumentException {
        if (super.aumentarNivel()) {
            setMaximoRecursosSegunNivel();
            return true;
        }
        return false;
    }

    public void ajustarSegunDatosCargados() {
        setMaximoAldeanosSegunNivel();
        setMaximoRecursosSegunNivel();
    }

    public void setMaximoRecursosSegunNivel() {
        if (nivel <= Constantes.Estructura.NIVEL_MAXIMO) {
            if (!(this instanceof CabaniaCaza)) {
                for (IGeneradorRecursos generador : generadoresRecursos) {
                    RecursosEnum recurso = generador.getRecurso();
                    recurso.setMax(Math.min(
                            (Constantes.Estructura.AUMENTO_MAX_RECURSO_POR_NIVEL * nivel),
                            Constantes.Estructura.MAX_RECURSOS
                    ));
                    Log.d(getClass().getSimpleName(), "Maximo de "+recurso+" = "+recurso.getMax());
                }
            }
        }
    }
}