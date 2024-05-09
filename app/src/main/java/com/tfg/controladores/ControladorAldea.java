package com.tfg.controladores;

import com.tfg.activities.JuegoActivity;
import com.tfg.firebase.bbdd.GestorBaseDatos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.TimerPartidaCaza;
import com.tfg.modelos.edificios.Edificio;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.impl.GeneradorEstandar;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.ListaHilos;

import java.util.Map;

public final class ControladorAldea {

    private static Aldea aldea = Aldea.getInstance();

    public static void iniciarAldea() {
        aldea.iniciarAldea();
    }

    public static void finalizarAldea() {
        JuegoActivity.enEjecucion = false;
        // Se interrumpen todos los hilos
        aldea.getCabaniaCaza().getTimerPartidaCaza().cancel();
        ListaHilos.interrumpirTodos();
    }

    public static boolean asignarAldeano(int numAldeanos) {
        if (aldea.getPoblacion() >= numAldeanos) {
            aldea.setPoblacion(aldea.getPoblacion()-numAldeanos);
            aldea.setAldeanosAsignados(aldea.getAldeanosAsignados()+numAldeanos);
            return true;
        }
        return false;
    }

    /*public static void comprarRecurso(RecursosEnum recursosEnum, int precio) {
        if (ControladorRecursos.consumirRecurso(aldea.getRecursos(), RecursosEnum.ORO, precio)){
            ControladorRecursos.agregarRecurso(aldea.getRecursos(), RecursosEnum.TABLONES_MADERA, Constantes.Mercader.CANTIDAD);
        }
    }*/

    public static void manejarSubidaNivel() {
        // Los niveles que no estan es por que solo aumentan la poblacion maxima, y de eso ya se encarga la propia Aldea
        switch (aldea.getNivel()) {
            case Constantes.Aldea.NIVEL_DESBLOQUEO_PIEDRA:
                aldea.getMina().setDesbloqueado(true);
                aldea.getMina().agregarGeneradorRecurso(RecursosEnum.PIEDRA);
                break;
            case Constantes.Aldea.NIVEL_DESBLOQUEO_TABLONES:
                // Desbloquear la carpinteria
                aldea.getCarpinteria().setDesbloqueado(true);
                break;
            case Constantes.Aldea.NIVEL_DESBLOQUEO_HIERRO:
                aldea.getMina().agregarGeneradorRecurso(RecursosEnum.HIERRO);
                break;
            case Constantes.Aldea.NIVEL_DESBLOQUEO_GRANJA:
                aldea.getGranja().setDesbloqueado(true);
                break;
            case Constantes.Aldea.NIVEL_DESBLOQUEO_ORO:
                aldea.getMina().agregarGeneradorRecurso(RecursosEnum.ORO);
                break;
            case Constantes.Aldea.NIVEL_DESBLOQUEO_MERCADER:
                // Desbloquear mercader
                break;
            case Constantes.Aldea.NIVEL_DESBLOQUEO_CASTILLO:
                // Desbloquear castillo
                break;
            default:
                break;
        }
    }
}
