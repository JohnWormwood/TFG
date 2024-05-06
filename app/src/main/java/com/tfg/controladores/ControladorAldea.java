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
        /*aldea.getCasetaLeniador().getThread().interrupt();
        aldea.getThread().interrupt();*/
    }

    public static boolean asignarAldeano(int numAldeanos) {
        if (aldea.getPoblacion() >= numAldeanos) {
            aldea.setPoblacion(aldea.getPoblacion()-numAldeanos);
            aldea.setAldeanosAsignados(aldea.getAldeanosAsignados()+numAldeanos);
            return true;
        }
        return false;
    }

    public static void manejarSubidaNivel() {
        // Los niveles que no estan es por que solo aumentan la poblacion maxima, y de eso ya se encarga la propia Aldea
        switch (aldea.getNivel()) {
            case 2:
                // Desbloquear mina (por defecto solo generara piedra)
                aldea.getMina().setDesbloqueado(true);
                break;
            case 3:
                // Desbloquear la carpinteria
                aldea.getCarpinteria().setDesbloqueado(true);
                break;
            case 5:
                aldea.getMina().getGeneradoresRecursos().add(new GeneradorEstandar(RecursosEnum.HIERRO));
                aldea.getMina().reiniciarProduccion();
                break;
            case 6:
                aldea.getGranja().setDesbloqueado(true);
                break;
            case 7:
                aldea.getMina().getGeneradoresRecursos().add(new GeneradorEstandar(RecursosEnum.ORO));
                break;
            case 9:

                break;
            case 10:

                break;
            default:
                break;
        }
    }
}
