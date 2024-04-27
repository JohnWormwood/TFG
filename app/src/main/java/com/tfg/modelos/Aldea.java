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
import com.tfg.utilidades.ListaHilos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Data
@Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
public class Aldea implements Runnable {
    private static Aldea instance; // Campo estático para almacenar la instancia única

    private int nivel;
    private int poblacion;
    private int poblacionMaxima;
    private int poblacionAsignada;
    private Thread thread;
    private Map<RecursosEnum, Integer> recursos;
    private CabaniaCaza cabaniaCaza;
    private Carpinteria carpinteria;
    private CasetaLeniador casetaLeniador;
    private Granja granja;
    private Mina mina;
    private List<PrecioMejora> preciosMejoras = Constantes.Aldea.PRECIOS_MEJORAS;

    private Aldea() {
        recursos = new HashMap<>();
        setMaximoPoblacionSegunNivel();

        // Edificios
        cabaniaCaza = new CabaniaCaza(0, this);
        casetaLeniador = new CasetaLeniador(0, this);

        // Recursos
        recursos.put(RecursosEnum.COMIDA, Constantes.Aldea.COMIDA_INICIAL);
        recursos.put(RecursosEnum.TRONCOS_MADERA, 0);
    }

    // Método estático para obtener la instancia única de Aldea
    public static Aldea getInstance() {
        if (instance == null) {
            instance = new Aldea();
        }
        return instance;
    }
    public void generarAldeano() {
        if (poblacion+1 <= poblacionMaxima && (poblacion+poblacionAsignada) < poblacionMaxima) {
            if (ControladorRecursos.consumirRecurso(recursos, RecursosEnum.COMIDA, 1)) {
                poblacion++;
                System.out.println("Aldeano generado");
            }
        }
    }

    protected void setMaximoPoblacionSegunNivel() throws IllegalArgumentException {
        // TODO Capturar la excepcion donde se llame la funcion
        if (nivel <= Constantes.Edificio.NIVEL_MAXIMO)
            poblacionMaxima += Constantes.Aldea.AUMENTO_MAX_POBLACION_POR_NIVEL;
        else
            throw new IllegalArgumentException(nivel+" es mayor al nivel maximo permitido ("+Constantes.Edificio.NIVEL_MAXIMO+")");
    }

    @Override
    public void run() {
        ListaHilos.add(thread);

        // Iniciar todos los edificios
        casetaLeniador.iniciarProduccion();

        int poblacionInicial = poblacion;
        Map<RecursosEnum, Integer> recursosIniciales = new HashMap<>(recursos);
        try {
            // Se ejecuta mientras la activity este activa
            while (JuegoActivity.enEjecucion) {
                poblacionInicial = poblacion;
                recursosIniciales = new HashMap<>(recursos);
                // Aqui se gestiona la logica prinicpal del juego
                Thread.sleep(1000);
                generarAldeano();
            }
        } catch (InterruptedException e) {
            poblacion = poblacionInicial;
            recursos = recursosIniciales;
            ListaHilos.remove(thread);
        }
    }

    public void iniciarAldea() {
        thread = new Thread(this);
        thread.start();
    }

    public boolean aumentarNivel() {
        int proximoNivel = nivel+1;
        if (puedeSubirDeNivel(preciosMejoras.get(proximoNivel-2))) {
            nivel++;
            setMaximoPoblacionSegunNivel();
            return true;
        }
        return false;
    }

    private synchronized boolean puedeSubirDeNivel(PrecioMejora precio) {
        Map<RecursosEnum, Integer> recursosInicialesAldea = new HashMap<>(recursos);
        for (Map.Entry<RecursosEnum, Integer> entry : precio.getRecursos().entrySet()) {
            Integer cantidadAldea = recursos.get(entry.getKey());
            if (cantidadAldea != null && cantidadAldea < entry.getValue()) {
                recursos = recursosInicialesAldea;
                return false;
            } else {
                ControladorRecursos.consumirRecurso(recursos, entry.getKey(), entry.getValue());
            }
        }
        return true;
    }
}
