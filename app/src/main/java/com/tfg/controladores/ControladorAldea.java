package com.tfg.controladores;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.tfg.activities.JuegoActivity;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.RecursosEnum;
import com.tfg.utilidades.Constantes;

public final class ControladorAldea {

    private static Aldea aldea = new Aldea(Constantes.Aldea.NIVEL_INICIAL, Constantes.Aldea.POBLACION_INICIAL);
    private static Thread hiloAldea;

    public static void iniciarAldea() {
        hiloAldea = new Thread(aldea);
        hiloAldea.start();
    }

    public static void finalizarAldea() {
        JuegoActivity.enEjecucion = false;
        try {
            /*
             * Si no se espera a que termine el hilo, al volver a abrir la activity
             * muy rapido es posible que todavia no haya terminado de generar un aldeano
             * y eso produce que al volver a la activity se generen los aldeanos
             * el doble de rapido
             */
            hiloAldea.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Integer getComida() {
        return aldea.getRecursos().get(RecursosEnum.COMIDA);
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
}
