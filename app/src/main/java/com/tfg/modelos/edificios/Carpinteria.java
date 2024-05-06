package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;
import com.tfg.utilidades.Constantes;

public class Carpinteria extends Edificio {
    public Carpinteria(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = true;
        desbloqueado = false;
    }

}
