package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;

public class Granja extends Edificio {
    public Granja(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        desbloqueado = false;
        generarRecursosConstantemente = true;
    }

}
