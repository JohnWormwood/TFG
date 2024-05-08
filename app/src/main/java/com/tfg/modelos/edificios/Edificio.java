package com.tfg.modelos.edificios;

import com.tfg.activities.JuegoActivity;
import com.tfg.controladores.ControladorAldea;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.EstructuraBase;
import com.tfg.modelos.PrecioMejora;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;
import com.tfg.modelos.generadores_recursos.impl.GeneradorEstandar;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.ListaHilos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;


@Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
public abstract class Edificio extends EstructuraBase implements Runnable {
    protected final int SEGUNDOS_ENTRE_RECURSOS = 10;

    protected boolean desbloqueado;
    protected boolean generarRecursosConstantemente;
    protected Aldea aldea;
    protected List<IGeneradorRecursos> generadoresRecursos;

    public Edificio(int aldeanosAsignados, Aldea aldea) {
        super();
        this.aldeanosAsignados = aldeanosAsignados;
        this.aldea = aldea;
        generadoresRecursos = new ArrayList<>();
    }


    protected void transferirRecursoAldea(RecursosEnum recurso) {
        Integer cantidad = recursos.get(recurso);
        if (cantidad != null) {
            if (ControladorRecursos.consumirRecurso(recursos, recurso, cantidad)) {
                ControladorRecursos.agregarRecurso(aldea.getRecursos(), recurso, cantidad);
            }
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
        aldea.setPoblacion(aldea.getPoblacion()+aldeanos);
        aldea.setAldeanosAsignados(aldea.getAldeanosAsignados()-aldeanos);
    }

    public synchronized void agregarGeneradorRecurso(RecursosEnum recurso) {
        generadoresRecursos.add(new GeneradorEstandar(recurso));
        reiniciarProduccion();
    }

    @Override
    public void run() {
        ListaHilos.add(thread);
        Map<RecursosEnum, Integer> recursosIniciales = new HashMap<>(recursos);
        try {
            while (JuegoActivity.enEjecucion) {
                recursosIniciales = new HashMap<>(recursos);
                // Genera recursos, espera x tiempo y despues los pasa a la aldea
                for (IGeneradorRecursos generadorRecursos : generadoresRecursos) {
                    generadorRecursos.producirRecursos(recursos, generadorRecursos.getRecurso(), aldeanosAsignados);
                }
                Thread.sleep(SEGUNDOS_ENTRE_RECURSOS * 1000);
                generadoresRecursos.forEach(g -> transferirRecursoAldea(g.getRecurso()));
                //System.out.println("Tablones aldea = "+aldea.getRecursos().get(RecursosEnum.TABLONES_MADERA));
            }
        } catch (InterruptedException e) {
            // En caso de interrupcion se vuelve al estado anterior, para evitar que se dupliquen recursos
            System.out.println("IMTERRUMPIDO");
            recursos = recursosIniciales;
            ListaHilos.remove(thread);
        }
    }

    protected int calcularCantidadProducida(int aldeanosAsignados) {
        // Generar el recurso en funcion de los aldeanos asignados y de la calidad del recurso
        int cantidad = (aldeanosAsignados == 1 ? 1 : aldeanosAsignados / 2);
        cantidad = Math.max(cantidad - RecursosEnum.TABLONES_MADERA.getCALIDAD(), 1);
        return cantidad;
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

    public void ajustarSegunDatosCargados() {
        setMaximoAldeanosSegunNivel();
    }
}