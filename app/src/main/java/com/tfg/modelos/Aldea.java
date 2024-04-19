package com.tfg.modelos;

import com.tfg.activities.JuegoActivity;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.edificios.Carpinteria;
import com.tfg.modelos.edificios.CasetaLeniador;
import com.tfg.modelos.edificios.Granja;
import com.tfg.modelos.edificios.Mina;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.Utilidades;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Aldea implements Runnable {
    private int nivel;
    private int poblacion;
    private Map<RecursosEnum, Integer> recursos;
    private CabaniaCaza cabaniaCaza;
    private Carpinteria carpinteria;
    private CasetaLeniador casetaLeniador;
    private Granja granja;
    private Mina mina;

    public Aldea(int nivel, int poblacion) {
        this.nivel = nivel;
        this.poblacion = poblacion;
        recursos = new HashMap<>();

        cabaniaCaza = new CabaniaCaza(0, this);
        casetaLeniador = new CasetaLeniador(Constantes.NIVEL_INICIAL, 0, 5, 10, this);

        recursos.put(RecursosEnum.COMIDA, Constantes.Aldea.COMIDA_INICIAL);
    }

    public synchronized boolean consumirRecurso(RecursosEnum recurso, int cantidad) {
        Integer cantidadActual = recursos.get(recurso);

        if (cantidadActual != null && cantidadActual >= cantidad) {
            recursos.put(recurso, cantidadActual - cantidad);
            return true;
        } else  {
            recursos.put(recurso, 0);
            return false;
        }
    }

    public synchronized void agregarRecurso(RecursosEnum recurso, int cantidad) {
        Integer cantidadActual = recursos.get(recurso);
        if (cantidadActual != null) {
            recursos.put(recurso, cantidadActual+cantidad);
        } else recursos.put(recurso, 0);
    }

    public void generarAldeano() {
        if (consumirRecurso(RecursosEnum.COMIDA, 1)) {
            poblacion++;
            System.out.println("Aldeano generado");
        }
    }

    @Override
    public void run() {
        // Se ejecuta mientras la activity este activa
        while (JuegoActivity.enEjecucion) {
            // Aqui se gestiona la logica prinicpal del juego
            generarAldeano();
            Utilidades.esperar(1);
        }
    }
}
