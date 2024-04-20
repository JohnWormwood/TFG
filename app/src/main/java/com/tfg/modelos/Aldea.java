package com.tfg.modelos;

import com.tfg.activities.JuegoActivity;
import com.tfg.controladores.ControladorRecursos;
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
    private Thread thread;
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
        casetaLeniador = new CasetaLeniador(2, this);

        recursos.put(RecursosEnum.COMIDA, Constantes.Aldea.COMIDA_INICIAL);
    }
    public void generarAldeano() {
        if (ControladorRecursos.consumirRecurso(recursos, RecursosEnum.COMIDA, 1)) {
            poblacion++;
            System.out.println("Aldeano generado");
        }
    }

    @Override
    public void run() {
        /*
        * Iniciar todos los edificios
        *
        */
        casetaLeniador.iniciarProduccion();

        // Se ejecuta mientras la activity este activa
        while (JuegoActivity.enEjecucion) {
            try {
                // Aqui se gestiona la logica prinicpal del juego
                Thread.sleep(1000);
                generarAldeano();
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void iniciarAldea() {
        thread = new Thread(this);
        thread.start();
    }
}
