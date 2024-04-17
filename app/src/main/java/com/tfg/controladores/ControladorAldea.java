package com.tfg.controladores;

import android.widget.Toast;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.RecursosEnum;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.edificios.Edificio;
import com.tfg.utilidades.Constantes;

import java.util.Random;

public final class ControladorAldea {

    private static Aldea aldea = new Aldea(Constantes.Aldea.NIVEL_INICIAL, Constantes.Aldea.POBLACION_INICIAL);
    public static void generarAldeano() {
        if (aldea.consumirRecurso(RecursosEnum.COMIDA, 1)) {
            aldea.setPoblacion(aldea.getPoblacion()+1);
            System.out.println("Aldeano generado");
        }
    }

    public static Integer getComida() {
        return aldea.getRecursos().get(RecursosEnum.COMIDA);
    }

    public static int getPoblacion() {
        return aldea.getPoblacion();
    }

}
