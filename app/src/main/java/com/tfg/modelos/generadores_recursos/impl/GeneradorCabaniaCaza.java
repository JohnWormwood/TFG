package com.tfg.modelos.generadores_recursos.impl;

import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.Utilidades;

import java.util.Map;

public class GeneradorCabaniaCaza extends GeneradorEstandar {

    private CabaniaCaza cabaniaCaza;

    public GeneradorCabaniaCaza(RecursosEnum recurso, CabaniaCaza cabaniaCaza) {
        super(recurso);
        this.cabaniaCaza = cabaniaCaza;
    }

    @Override
    public void producirRecursos(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int aldeanosAsignados) {
        int comidaProducida = Utilidades.generarIntRandom(1, aldeanosAsignados > 1 ? Math.abs(aldeanosAsignados / 20) : 1);
        if (!muertesAleatorias() && cabaniaCaza.getAldeanosAsignados() > 0) {
            ControladorRecursos.agregarRecurso(recursos, this.recurso, comidaProducida);
            System.out.println("Generado +"+comidaProducida+" de comida");
        } else {
            System.out.println("Ha muerto un cazador");
        }
    }

    private boolean muertesAleatorias() {
        int probabilidadMuerte = Constantes.CabaniaCaza.PROBABILIDAD_MUERTE;
        int random = Utilidades.generarIntRandom(1, 100);

        if (random <= probabilidadMuerte && cabaniaCaza.getAldeanosAsignados() > 0) {
            cabaniaCaza.setAldeanosAsignados(cabaniaCaza.getAldeanosAsignados()-1);
            Aldea.getInstance().setAldeanosAsignados(Aldea.getInstance().getAldeanosAsignados()-1);
            cabaniaCaza.setAldeanosMuertosEnPartida(cabaniaCaza.getAldeanosMuertosEnPartida()+1);
            return true;
        }

        return false;
    }

}