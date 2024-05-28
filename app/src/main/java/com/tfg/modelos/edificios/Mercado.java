package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;

public class Mercado extends Edificio{
    public Mercado(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        desbloqueado = false;
        generarRecursosConstantemente = false;
    }

}
