package com.tfg.modelos.edificios;

import com.tfg.controladores.ControladorAldea;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.TimerPartidaCaza;
import com.tfg.modelos.generadores_recursos.impl.GeneradorCabaniaCaza;
import com.tfg.utilidades.Constantes;

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
        timerPartidaCaza = null;
        recursosGenerados.put(RecursosEnum.COMIDA, 0);
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

}