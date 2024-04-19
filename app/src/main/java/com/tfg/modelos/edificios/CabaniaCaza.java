package com.tfg.modelos.edificios;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.tfg.controladores.ControladorAldea;
import com.tfg.controladores.ControladorCabaniaCaza;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.EdificiosEnum;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.TimerPartidaCaza;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.Utilidades;

import java.util.Random;

import lombok.Getter;

@Getter
public class CabaniaCaza extends Edificio {
    private TimerPartidaCaza timerPartidaCaza;
    private int aldeanosMuertosEnPartida;

    public CabaniaCaza(int aldeanosAsignados, Aldea aldea) {
        super(
                Constantes.NIVEL_INICIAL,
                aldeanosAsignados,
                ControladorCabaniaCaza.getMaximoCazadoresSegunNivel(Constantes.NIVEL_INICIAL),
                0,
                aldea
        );
        generarRecursosConstantemente = false;
        timerPartidaCaza = null;
        recursosGenerados.put(RecursosEnum.COMIDA, 0);
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
        Integer cantidadComida = recursosGenerados.get(RecursosEnum.COMIDA);
        if (cantidadComida != null) {
            aldea.agregarRecurso(RecursosEnum.COMIDA, cantidadComida);
            recursosGenerados.put(RecursosEnum.COMIDA, 0);
        }
        aldea.setPoblacion(aldea.getPoblacion()+aldeanosAsignados);
        aldeanosAsignados = 0;
        aldeanosMuertosEnPartida = 0;
    }

    @Override
    public void producirRecursos() {
        int comidaProducida = Utilidades.generarIntRandom(1, aldeanosAsignados > 1 ? Math.abs(aldeanosAsignados / 2) : 1);
        Integer comidaActual = recursosGenerados.get(RecursosEnum.COMIDA);
        if (comidaActual != null) {
            if (!muertesAleatorias()) {
                recursosGenerados.put(RecursosEnum.COMIDA, comidaActual+comidaProducida);
                System.out.println("Generado +"+comidaProducida+" de comida");
            } else {
                System.out.println("Ha muerto un cazador");
            }
        } else recursosGenerados.put(RecursosEnum.COMIDA, 0);
    }

    private boolean muertesAleatorias() {
        int probabilidadMuerte = Constantes.CabaniaCaza.PROBABILIDAD_MUERTE;
        int random = Utilidades.generarIntRandom(1, 100);

        if (random <= probabilidadMuerte) {
            aldeanosAsignados--;
            aldeanosMuertosEnPartida++;
            return true;
        }

        return false;
    }
}