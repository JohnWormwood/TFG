package com.tfg.modelos.edificios;

import com.tfg.activities.JuegoActivity;
import com.tfg.controladores.ControladorAldea;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.PrecioMejora;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.ListaHilos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Data
@Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
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
    protected List<PrecioMejora> preciosMejoras;

    public Edificio(int aldeanosAsignados, Aldea aldea) {
        this.nivel = Constantes.NIVEL_INICIAL;
        this.aldeanosAsignados = aldeanosAsignados;
        this.aldea = aldea;

        this.recursosGenerados = new HashMap<>();

        generadoresRecursos = new ArrayList<>();
        setMaximoAldeanosSegunNivel();

        preciosMejoras = new ArrayList<>();
    }

    protected void setMaximoAldeanosSegunNivel() throws IllegalArgumentException {
        // TODO Capturar la excepcion donde se llame la funcion
        if (nivel <= Constantes.Edificio.NIVEL_MAXIMO)
            aldeanosMaximos += Constantes.Edificio.AUMENTO_MAX_ALDEANOS_POR_NIVEL;
        else
            throw new IllegalArgumentException(nivel+" es mayor al nivel maximo permitido ("+Constantes.Edificio.NIVEL_MAXIMO+")");
    }

    protected void transferirRecursoAldea(RecursosEnum recurso) {
        Integer recursos = recursosGenerados.get(recurso);
        if (recursos != null) {
            if (ControladorRecursos.consumirRecurso(recursosGenerados, recurso, recursos)) {
                ControladorRecursos.agregarRecurso(aldea.getRecursos(), recurso, recursos);
            }
        }
    }

    public synchronized void modificarAldeanosAsignados(int aldeanosAsignados) {
        if (aldeanosAsignados >= 0 && aldeanosAsignados <= aldeanosMaximos) {
            int diferenciaAldeanos;
            if (aldeanosAsignados < this.aldeanosAsignados) {
                diferenciaAldeanos = this.aldeanosAsignados - aldeanosAsignados;
                devolverAldeanos(diferenciaAldeanos);
                reiniciarProduccion(aldeanosAsignados);
            } else if (aldeanosAsignados > this.aldeanosAsignados) {
                diferenciaAldeanos = aldeanosAsignados - this.aldeanosAsignados;
                if (ControladorAldea.asignarAldeano(diferenciaAldeanos)) {
                    reiniciarProduccion(aldeanosAsignados);
                }
            }
        }
    }

    private void devolverAldeanos(int aldeanos) {
        aldea.setPoblacion(aldea.getPoblacion()+aldeanos);
        aldea.setPoblacionAsignada(aldea.getPoblacionAsignada()-aldeanos);
    }

    @Override
    public void run() {
        ListaHilos.add(thread);
        Map<RecursosEnum, Integer> recursosIniciales = new HashMap<>(recursosGenerados);
        try {
            while (JuegoActivity.enEjecucion) {
                recursosIniciales = new HashMap<>(recursosGenerados);
                // Genera recursos, espera x tiempo y despues los pasa a la aldea
                for (IGeneradorRecursos generadorRecursos : generadoresRecursos) {
                    generadorRecursos.producirRecursos(recursosGenerados, generadorRecursos.getRecurso(), aldeanosAsignados);
                }
                Thread.sleep(SEGUNDOS_ENTRE_RECURSOS * 1000);
                generadoresRecursos.forEach(g -> transferirRecursoAldea(g.getRecurso()));
            }
        } catch (InterruptedException e) {
            // En caso de interrupcion se vuelve al estado anterior, para evitar que se dupliquen recursos
            recursosGenerados = recursosIniciales;
            ListaHilos.remove(thread);
        }
    }

    public void reiniciarProduccion(int aldeanosAsignados) {
        if (thread != null) thread.interrupt();
        this.aldeanosAsignados = aldeanosAsignados;
        iniciarProduccion();
    }

    public void iniciarProduccion() {
        /*
        * Solo se iniciaran los edificios que tienen generarRecursosConstantemente = true
        * el resto de edificos no haran nada cuando se llame a iniciarProduccion
        * ya que se asume que producen recursos solo cuando el jugador quiere
        */
        if (generarRecursosConstantemente && aldeanosAsignados > 0) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public boolean aumentarNivel() {
        int proximoNivel = nivel+1;
        if (puedeSubirDeNivel(preciosMejoras.get(proximoNivel-2))) {
            nivel++;
            setMaximoAldeanosSegunNivel();
            return true;
        }
        return false;
    }

    private synchronized boolean puedeSubirDeNivel(PrecioMejora precio) {
        Map<RecursosEnum, Integer> recursosInicialesAldea = new HashMap<>(aldea.getRecursos());
        for (Map.Entry<RecursosEnum, Integer> entry : precio.getRecursos().entrySet()) {
            Integer cantidadAldea = aldea.getRecursos().get(entry.getKey());
            if (cantidadAldea != null && cantidadAldea < entry.getValue()) {
                aldea.setRecursos(recursosInicialesAldea);
                return false;
            } else {
                ControladorRecursos.consumirRecurso(aldea.getRecursos(), entry.getKey(), entry.getValue());
            }
        }
        return true;
    }
}