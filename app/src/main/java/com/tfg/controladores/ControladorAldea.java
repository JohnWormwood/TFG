package com.tfg.controladores;

import com.tfg.activities.JuegoActivity;
import com.tfg.modelos.Aldea;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.ListaHilos;

public final class ControladorAldea {

    private static Aldea aldea = Aldea.getInstance();

    public static void iniciarAldea() {
        aldea.setNivel(Constantes.NIVEL_INICIAL);
        aldea.setPoblacion(Constantes.Aldea.POBLACION_INICIAL);
        aldea.iniciarAldea();
    }

    public static void finalizarAldea() {
        JuegoActivity.enEjecucion = false;
        // Se interrumpen todos los hilos
        ListaHilos.interrumpirTodos();
        /*aldea.getCasetaLeniador().getThread().interrupt();
        aldea.getThread().interrupt();*/
    }

    public static boolean asignarAldeano(int numAldeanos) {
        if (aldea.getPoblacion() >= numAldeanos) {
            aldea.setPoblacion(aldea.getPoblacion()-numAldeanos);
            aldea.setPoblacionAsignada(aldea.getPoblacionAsignada()+numAldeanos);
            return true;
        }
        return false;
    }
}
