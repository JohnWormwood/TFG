package com.tfg.modelos;

import android.util.Log;

import com.tfg.activities.JuegoActivity;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.edificios.Carpinteria;
import com.tfg.modelos.edificios.CasetaLeniador;
import com.tfg.modelos.edificios.Castillo;
import com.tfg.modelos.edificios.Granja;
import com.tfg.modelos.edificios.Mina;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.ListaHilos;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Getter(onMethod_ = {@Synchronized})
@Setter(onMethod_ = {@Synchronized})
public class Aldea extends EstructuraBase implements Runnable {
    private static Aldea instance; // Campo estático para almacenar la instancia única

    private int poblacion;
    private CabaniaCaza cabaniaCaza;
    private Carpinteria carpinteria;
    private CasetaLeniador casetaLeniador;
    private Granja granja;
    private Mina mina;
    private Castillo castillo;

    // Constructor privado para evitar multiples instancias
    private Aldea() {
        super();
        poblacion = Constantes.Aldea.POBLACION_INICIAL;
        multiplicadorAldeanosSegunNivel = Constantes.Aldea.AUMENTO_MAX_ALDEANOS_POR_NIVEL;

        // Edificios
        cabaniaCaza = new CabaniaCaza(0, this);
        casetaLeniador = new CasetaLeniador(0, this);
        carpinteria = new Carpinteria(0, this);
        granja = new Granja(0, this);
        mina = new Mina(0, this);
        castillo = new Castillo(0, this);

        recursos.put(RecursosEnum.COMIDA, Constantes.Aldea.COMIDA_INICIAL);
    }

    // Método estático para obtener la instancia única de Aldea
    public static Aldea getInstance() {
        if (instance == null) {
            instance = new Aldea();
        }
        return instance;
    }

    @Override
    public void reiniciarDatos() {
        super.reiniciarDatos();
        poblacion = Constantes.Aldea.POBLACION_INICIAL;
        multiplicadorAldeanosSegunNivel = Constantes.Aldea.AUMENTO_MAX_ALDEANOS_POR_NIVEL;

        // Edificios
        cabaniaCaza.reiniciarDatos();
        casetaLeniador.reiniciarDatos();
        carpinteria.reiniciarDatos();
        granja.reiniciarDatos();
        mina.reiniciarDatos();
        castillo.reiniciarDatos();

        recursos.put(RecursosEnum.COMIDA, Constantes.Aldea.COMIDA_INICIAL);
    }

    public void generarAldeano() {
        if (poblacion + 1 <= aldeanosMaximos && (poblacion + aldeanosAsignados) < aldeanosMaximos) {
            if (ControladorRecursos.consumirRecurso(recursos, RecursosEnum.COMIDA, 1)) {
                poblacion++;
                Log.d(getClass().getSimpleName(), "Se ha generado un aldeano");
            }
        }
    }

    public synchronized void setPoblacion(int poblacion) {
        if (aldeanosAsignados + poblacion > 0) {
            this.poblacion = poblacion;
        }
    }


    @Override
    public void run() {
        ListaHilos.add(thread);

        // Iniciar todos los edificios
        casetaLeniador.iniciarProduccion();
        mina.iniciarProduccion();
        carpinteria.iniciarProduccion();
        granja.iniciarProduccion();

        try {
            // Se ejecuta mientras la activity este activa
            while (JuegoActivity.enEjecucion) {
                Thread.sleep(5000);
                generarAldeano();
            }
        } catch (InterruptedException e) {
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
        castillo.ajustarSegunDatosCargados();

        setAldeanosAsignados(cabaniaCaza.getAldeanosAsignados() + carpinteria.getAldeanosAsignados()
                + casetaLeniador.getAldeanosAsignados() + granja.getAldeanosAsignados()
                + mina.getAldeanosAsignados() +  castillo.getAldeanosAsignados());
    }
}
