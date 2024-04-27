package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.impl.GeneradorEstandar;
import com.tfg.utilidades.Constantes;

public class CasetaLeniador extends Edificio {

    public CasetaLeniador(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = true;
        generadoresRecursos.add(new GeneradorEstandar(RecursosEnum.TRONCOS_MADERA));
        preciosMejoras = Constantes.CasetaLeniador.PRECIOS_MEJORAS;
    }

}
