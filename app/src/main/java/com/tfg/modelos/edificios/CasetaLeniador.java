package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.interfaces.impl.GeneradorEstandar;
import com.tfg.utilidades.Constantes;


public class CasetaLeniador extends Edificio {

    public CasetaLeniador(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = true;
        recursosGenerados.put(RecursosEnum.TRONCOS_MADERA, 0);
        generadorRecursos = new GeneradorEstandar(RecursosEnum.TRONCOS_MADERA);
    }



}
