package com.tfg.modelos.edificios;

import android.os.Handler;
import android.os.Looper;

import com.tfg.controladores.ControladorAldea;
import com.tfg.eventos.listeners.PartidaCazaEventListener;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.TimerPartidaCaza;
import com.tfg.modelos.generadores_recursos.impl.GeneradorCabaniaCaza;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.ListaHilos;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
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
        transferirRecursoAldea(RecursosEnum.COMIDA);
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
                System.out.println("SEGUNDOS = "+timerPartidaCaza.getSegundosRestantes());
                timerPartidaCaza = new TimerPartidaCaza(timerPartidaCaza.getSegundosRestantes()*1000, ref);
                timerPartidaCaza.start();
            });
        }
    }
}