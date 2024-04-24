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

    public static synchronized boolean eliminarAldeanos(int numAldeanos) {
        if (aldea.getPoblacion() >= numAldeanos) {
            aldea.setPoblacion(aldea.getPoblacion()-numAldeanos);
            return true;
        }
        return false;
    }
}
