package com.tfg.modelos.edificios;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.tfg.controladores.ControladorAldea;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.TimerPartidaCaza;
import com.tfg.modelos.interfaces.impl.GeneradorCabaniaCaza;
import com.tfg.utilidades.Constantes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CabaniaCaza extends Edificio {
    private TimerPartidaCaza timerPartidaCaza;
    private int aldeanosMuertosEnPartida;

    public CabaniaCaza(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = false;
        timerPartidaCaza = null;
        recursosGenerados.put(RecursosEnum.COMIDA, 0);
        generadoresRecursos.add(new GeneradorCabaniaCaza(RecursosEnum.COMIDA, this));
    }

    public void iniciarPartidaCaza(int numAldeanos, int tiempoTotal, TextView textViewPartidaCaza, Button buttonCaza, Context context) {
        if (numAldeanos <= aldeanosMaximos) {
            if (ControladorAldea.eliminarAldeanos(numAldeanos)) {
                aldeanosAsignados = numAldeanos;
                // El timer se encarga de llamar a producir recursos cada segundo
                timerPartidaCaza = new TimerPartidaCaza(tiempoTotal, this, textViewPartidaCaza, buttonCaza, context);
                timerPartidaCaza.start();
            }
        }
    }

    public void finalizarPartidaCaza() {
        transferirRecursoAldea(RecursosEnum.COMIDA);
        aldea.setPoblacion(aldea.getPoblacion()+aldeanosAsignados);
        aldeanosAsignados = 0;
        aldeanosMuertosEnPartida = 0;
    }

}