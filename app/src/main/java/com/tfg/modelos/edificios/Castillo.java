package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;

public class Castillo extends Edificio{
    public Castillo(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = false;
        desbloqueado = false;
    }
}
