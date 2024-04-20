package com.tfg.modelos.interfaces.impl;

import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.interfaces.IGeneradorRecursos;
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
        int comidaProducida = Utilidades.generarIntRandom(1, aldeanosAsignados > 1 ? Math.abs(aldeanosAsignados / 2) : 1);
        if (!muertesAleatorias()) {
            ControladorRecursos.agregarRecurso(recursos, RecursosEnum.COMIDA, comidaProducida);
            System.out.println("Generado +"+comidaProducida+" de comida");
        } else {
            System.out.println("Ha muerto un cazador");
        }
    }

    private boolean muertesAleatorias() {
        int probabilidadMuerte = Constantes.CabaniaCaza.PROBABILIDAD_MUERTE;
        int random = Utilidades.generarIntRandom(1, 100);

        if (random <= probabilidadMuerte) {
            System.out.println("MUERTE");
            cabaniaCaza.setAldeanosAsignados(cabaniaCaza.getAldeanosAsignados()-1);
            cabaniaCaza.setAldeanosMuertosEnPartida(cabaniaCaza.getAldeanosMuertosEnPartida()+1);
            return true;
        }

        return false;
    }

    @Override
    public RecursosEnum getRecurso() {
        return RecursosEnum.COMIDA;
    }
}
