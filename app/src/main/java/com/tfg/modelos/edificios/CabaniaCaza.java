package com.tfg.modelos.edificios;

import android.os.Handler;
import android.os.Looper;

import com.tfg.controladores.ControladorAldea;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.TimerPartidaCaza;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.impl.GeneradorCabaniaCaza;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Getter(onMethod_ = {@Synchronized})
@Setter(onMethod_ = {@Synchronized})
public class CabaniaCaza extends Edificio {
    private TimerPartidaCaza timerPartidaCaza;
    private int aldeanosMuertosEnPartida;
    private boolean partidaActiva;

    public CabaniaCaza(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = false;
        desbloqueado = true;
        timerPartidaCaza = new TimerPartidaCaza(0, this);
        recursos.put(RecursosEnum.COMIDA, 0);
        generadoresRecursos.add(new GeneradorCabaniaCaza(RecursosEnum.COMIDA, this));
    }

    public void iniciarPartidaCaza(int numAldeanos, int tiempoTotal) {
        if (numAldeanos <= aldeanosMaximos) {
            if (ControladorAldea.asignarAldeano(numAldeanos)) {
                partidaActiva = true;
                aldeanosAsignados = numAldeanos;
                // El timer se encarga de llamar a producir recursos cada segundo
                timerPartidaCaza = new TimerPartidaCaza(tiempoTotal, this);
                timerPartidaCaza.start();
            }
        }
    }

    public void finalizarPartidaCaza() {
        // Si se ha generado mas del maxmio se añade solo lo necesario para llegar al maximo
        int comidaAldea = ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.COMIDA);
        int comidaGenerada = ControladorRecursos.getCantidadRecurso(recursos, RecursosEnum.COMIDA);
        if ((comidaAldea + comidaGenerada) > RecursosEnum.COMIDA.getMax()) {
            recursos.put(RecursosEnum.COMIDA, RecursosEnum.COMIDA.getMax() - comidaAldea);
        }
        if (aldeanosAsignados > 0) {
            transferirRecursoAldea(RecursosEnum.COMIDA);
        }
        devolverAldeanos(aldeanosAsignados);
        aldeanosAsignados = 0;
        aldeanosMuertosEnPartida = 0;
        partidaActiva = false;
    }

    @Override
    public void ajustarSegunDatosCargados() {
        super.ajustarSegunDatosCargados();

        if (isPartidaActiva()) {
            CabaniaCaza ref = this;
            new Handler(Looper.getMainLooper()).post(() -> {
                /*
                 * Hay que hacerlo de esta forma para que el timer tenga
                 * un looper en el que ejecutarse ya que si no dara excepcion
                 */
                // Iniciar el timer con el tiempo restante
                timerPartidaCaza = new TimerPartidaCaza(timerPartidaCaza.getSegundosRestantes() * 1000, ref);
                timerPartidaCaza.start();
            });
        }
    }
}