package com.tfg.controladores;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.tfg.activities.JuegoActivity;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.edificios.Edificio;
import com.tfg.modelos.enums.EdificiosEnum;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;

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
        aldea.getCasetaLeniador().getThread().interrupt();
        aldea.getThread().interrupt();
    }

    // --- FUNCIONES PARA OBTENER LOS RECURSOS DE LA ALDEA ---
    public static synchronized Integer getComida() {
        return aldea.getRecursos().get(RecursosEnum.COMIDA);
    }

    public static synchronized Integer getTroncos() {
        Integer troncos = aldea.getRecursos().get(RecursosEnum.TRONCOS_MADERA);
        return troncos == null ? 0 : troncos;
    }

    public static synchronized int getPoblacion() {
        return aldea.getPoblacion();
    }

    public static synchronized boolean eliminarAldeanos(int numAldeanos) {
        if (aldea.getPoblacion() >= numAldeanos) {
            aldea.setPoblacion(aldea.getPoblacion()-numAldeanos);
            return true;
        }
        return false;
    }

    public static void iniciarPartidaDeCaza(int numAldeanos, int tiempoTotal, TextView textViewPartidaCaza, Button buttonCaza, Context context) {
        aldea.getCabaniaCaza().iniciarPartidaCaza(numAldeanos, tiempoTotal, textViewPartidaCaza, buttonCaza, context);
    }

    public static int getCazadoresEnPartida() {
        return aldea.getCabaniaCaza().getAldeanosAsignados();
    }

    public static boolean isPartidaCazaActiva() {
        return aldea.getCabaniaCaza().isPartidaActiva();
    }

    public static void actualizarElementosUiEnTimerPartidaCaza(TextView textView, Button button, Context context) {
        aldea.getCabaniaCaza().getTimerPartidaCaza().actualizarElementosUI(textView, button, context);
    }
}
