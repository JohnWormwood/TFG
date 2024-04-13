package com.tfg.modelos.edificios;

import java.util.Map;

public class CasetaLeniador extends Edificio {
    private int troncos;

    public CasetaLeniador(int nivel, int aldeanosAsignados, int aldeanosMaximos, Map<String, Integer> recursosGenerados) {
        super(nivel, aldeanosAsignados, aldeanosMaximos, recursosGenerados);
    }

    @Override
    public void producirRecursos() {

    }

}
