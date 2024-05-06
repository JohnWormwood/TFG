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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Data
@EqualsAndHashCode(callSuper = true)
@Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
public class Aldea extends EstructuraBase implements Runnable {
    private static Aldea instance; // Campo estático para almacenar la instancia única

    private int poblacion;
    private CabaniaCaza cabaniaCaza;
    private Carpinteria carpinteria;
    private CasetaLeniador casetaLeniador;
    private Granja granja;
    private Mina mina;

    private Aldea() {
        super();
        poblacion = Constantes.Aldea.POBLACION_INICIAL;

        // Edificios
        cabaniaCaza = new CabaniaCaza(0, this);
        casetaLeniador = new CasetaLeniador(0, this);
        carpinteria = new Carpinteria(0, this);
        granja = new Granja(0, this);
        mina = new Mina(0, this);
    }

    // Método estático para obtener la instancia única de Aldea
    public static Aldea getInstance() {
        if (instance == null) {
            instance = new Aldea();
        }
        return instance;
    }
    public void generarAldeano() {
        if (poblacion+1 <= aldeanosMaximos && (poblacion+aldeanosAsignados) < aldeanosMaximos) {
            if (ControladorRecursos.consumirRecurso(recursos, RecursosEnum.COMIDA, 1)) {
                poblacion++;
                System.out.println("Aldeano generado");
            }
        }
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
                Thread.sleep(5000);
                generarAldeano();
            }
        } catch (InterruptedException e) {
            // En caso de interrupcion se vuelve al estado anterior, para evitar que se dupliquen recursos
            //poblacion = poblacionInicial;
            recursos = recursosIniciales;
            ListaHilos.remove(thread);
        }
    }

    public void iniciarAldea() {
        thread = new Thread(this);
        thread.start();
    }

    public void ajustarSegunDatosCargados() {
        setMaximoAldeanosSegunNivel();
        cabaniaCaza.ajustarSegunDatosCargados();
        carpinteria.ajustarSegunDatosCargados();
        casetaLeniador.ajustarSegunDatosCargados();
        granja.ajustarSegunDatosCargados();
        mina.ajustarSegunDatosCargados();

        setAldeanosAsignados(cabaniaCaza.getAldeanosAsignados() + carpinteria.getAldeanosAsignados()
                + casetaLeniador.getAldeanosAsignados() + granja.getAldeanosAsignados()
                + mina.getAldeanosAsignados());
    }
}
