package com.tfg.modelos.generadores_recursos.impl;

import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;
import com.tfg.utilidades.Constantes;


import java.util.Map;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneradorEstandar implements IGeneradorRecursos {
    protected RecursosEnum recurso;
    private static final Random random = new Random();


    @Override
    public void producirRecursos(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int aldeanosAsignados) {
        if (aldeanosAsignados > 0) {
            int cantidad = calcularCantidadProducida(aldeanosAsignados);
            System.out.println(recurso+" "+cantidad);
            agregarRecursoSinExcederMax(recursos, recurso, cantidad);
        }
    }

    protected void agregarRecursoSinExcederMax(Map<RecursosEnum, Integer> recursos, RecursosEnum recurso, int cantidad) {
        int cantidadActual = ControladorRecursos.getCantidadRecurso(Aldea.getInstance().getRecursos(), recurso);
        int disponible = recurso.getMax() - cantidadActual;

        ControladorRecursos.agregarRecurso(recursos, recurso, Math.min(cantidad, disponible));
        System.out.println("---------------------------------------------------");
        System.out.println("MAXIMO RECURSO = "+recurso+", "+recurso.getMax());
        System.out.println("Cantidad = "+cantidad);
        System.out.println("Disponible = "+disponible);
        System.out.println("Resultado math.min = " + recurso +", "+Math.min(cantidad, disponible));
        System.out.println("---------------------------------------------------");
    }

    protected int calcularCantidadProducida(int aldeanosAsignados) {
        int cantidadProducida = 0;

        // Calcular la cantidad producida
        for (int i = 0; i < aldeanosAsignados; i++) {
            if (random.nextDouble() < 0.5) {
                cantidadProducida++;
            }
        }
        // Ajustar la cantidad producida por la calidad del recurso
        cantidadProducida = Math.max(cantidadProducida, 1);
        cantidadProducida = cantidadProducida / recurso.getCALIDAD();

        return cantidadProducida;
    }
}
