package com.tfg.controladores;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.tfg.activities.JuegoActivity;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.EdificiosEnum;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.utilidades.Constantes;

public final class ControladorAldea {

    private static Aldea aldea = new Aldea(Constantes.NIVEL_INICIAL, Constantes.Aldea.POBLACION_INICIAL);
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
            aldea.getCasetaLeniador().getThread().interrupt();
            hiloAldea.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*public static int getNivelEdificio(EdificiosEnum edificiosEnum) throws IllegalArgumentException {
        // TODO Capturar la excepcion donde se llame la funcion
        switch (edificiosEnum) {
            case CABANIA_CAZA:
                return aldea.getCabaniaCaza().getNivel();
            case CARPINTERIA:
                return aldea.getCarpinteria().getNivel();
            case CASETA_LENIADOR:
                return aldea.getCasetaLeniador().getNivel();
            case GRANJA:
                return aldea.getGranja().getNivel();
            case MINA:
                return aldea.getMina().getNivel();
            default:
                throw new IllegalArgumentException("No existe un edifico de tipo: "+edificiosEnum);
        }
    }*/

    public static int getAldeanosMaximosEdificio(EdificiosEnum edificiosEnum) throws IllegalArgumentException {
        // TODO Capturar la excepcion donde se llame la funcion
        switch (edificiosEnum) {
            case CABANIA_CAZA:
                return aldea.getCabaniaCaza().getAldeanosMaximos();
            case CARPINTERIA:
                return aldea.getCarpinteria().getAldeanosMaximos();
            case CASETA_LENIADOR:
                return aldea.getCasetaLeniador().getAldeanosMaximos();
            case GRANJA:
                return aldea.getGranja().getAldeanosMaximos();
            case MINA:
                return aldea.getMina().getAldeanosMaximos();
            default:
                throw new IllegalArgumentException("No existe un edifico de tipo: "+edificiosEnum);
        }
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
}
