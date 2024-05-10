package com.tfg.modelos.edificios;

import com.tfg.activities.JuegoActivity;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;
import com.tfg.modelos.generadores_recursos.impl.GeneradorCarpinteria;
import com.tfg.modelos.generadores_recursos.impl.GeneradorEstandar;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.ListaHilos;

import java.util.HashMap;
import java.util.Map;

public class Carpinteria extends Edificio {
    public Carpinteria(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = true;
        generadoresRecursos.add(new GeneradorCarpinteria(RecursosEnum.TABLONES_MADERA));
        desbloqueado = false;
    }

    @Override
    public void run() {
        ListaHilos.add(thread);
        Map<RecursosEnum, Integer> recursosIniciales = new HashMap<>(recursos);
        try {
            while (JuegoActivity.enEjecucion) {
                recursosIniciales = new HashMap<>(recursos);
                /*System.out.println("-----------ANTES-----------");
                System.out.println("RECURSOS CARPINTERIA = "+recursos);
                System.out.println("troncos aldea = "+ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA));
                System.out.println("troncos a consumir = "+(calcularCantidadProducida(aldeanosAsignados)*2));
                System.out.println("puede consumir troncos = "+ControladorRecursos.puedeConsumirRecurso(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA, (calcularCantidadProducida(aldeanosAsignados)*2)));
                System.out.println("--------------------------------");*/
                // Genera recursos, espera x tiempo y despues los pasa a la aldea
                for (IGeneradorRecursos generadorRecursos : generadoresRecursos) {
                    generadorRecursos.producirRecursos(recursos, generadorRecursos.getRecurso(), aldeanosAsignados);
                }
                Thread.sleep(SEGUNDOS_ENTRE_RECURSOS * 1000);

                /*System.out.println("-----------DESPUES-----------");
                System.out.println("RECURSOS CARPINTERIA = "+recursos);
                System.out.println("troncos aldea = "+ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA));
                System.out.println("troncos a consumir = "+(calcularCantidadProducida(aldeanosAsignados)*2));
                System.out.println("puede consumir troncos = "+ControladorRecursos.puedeConsumirRecurso(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA, (calcularCantidadProducida(aldeanosAsignados)*2)));
                System.out.println("--------------------------------");*/

                // Esta comprobacion es necesaria ya que la cantidad de troncos puede haber cambiado durante la generacion
                // y si no se comprueba podrian quitarse troncos sin generar tablones
                for (IGeneradorRecursos generadorRecursos : generadoresRecursos) {
                    if (ControladorRecursos.getCantidadRecurso(recursos, generadorRecursos.getRecurso()) > 0) {
                        ControladorRecursos.consumirRecurso(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA, (calcularCantidadProducida(aldeanosAsignados)*2));
                    }
                }

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
}
