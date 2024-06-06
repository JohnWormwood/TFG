package com.tfg.modelos.edificios;

import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;
import com.tfg.modelos.generadores_recursos.impl.GeneradorCarpinteria;

public class Carpinteria extends Edificio {
    public Carpinteria(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = true;
        generadoresRecursos.add(new GeneradorCarpinteria(RecursosEnum.TABLONES_MADERA));
        desbloqueado = false;
    }

    @Override
    protected void finalizarGeneracionRecursos() {
        // Esta comprobacion es necesaria ya que la cantidad de troncos puede haber cambiado durante la generacion
        // y si no se comprueba podrian quitarse troncos sin generar tablones y viceversa
        for (IGeneradorRecursos generadorRecursos : generadoresRecursos) {
            if (ControladorRecursos.getCantidadRecurso(recursos, generadorRecursos.getRecurso()) > 0) {
                // Consumir el doble de troncos que los tablones que se generan
                if (ControladorRecursos.consumirRecurso(
                        aldea.getRecursos(),
                        RecursosEnum.TRONCOS_MADERA,
                        ControladorRecursos.getCantidadRecurso(recursos, generadorRecursos.getRecurso()) * 2)
                ) {
                    transferirRecursoAldea(generadorRecursos.getRecurso());
                }
            }
        }
    }
}
