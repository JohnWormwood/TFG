package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.impl.GeneradorEstandar;

public class Mina extends Edificio {
    public Mina(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        desbloqueado = false;
        generarRecursosConstantemente = true;
        setGeneradoresRecursosSegunNivelAldea();
    }

    private void setGeneradoresRecursosSegunNivelAldea() {
        int nivelAldea = aldea.getNivel();
        if (nivelAldea >= 2) generadoresRecursos.add(new GeneradorEstandar(RecursosEnum.PIEDRA));
        if (nivelAldea >= 5) generadoresRecursos.add(new GeneradorEstandar(RecursosEnum.HIERRO));
        if (nivelAldea >= 7) generadoresRecursos.add(new GeneradorEstandar(RecursosEnum.ORO));
    }

    @Override
    public void ajustarSegunDatosCargados() {
        setGeneradoresRecursosSegunNivelAldea();
        super.ajustarSegunDatosCargados();
    }
}
