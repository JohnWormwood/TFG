package com.tfg.modelos.edificios;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.impl.GeneradorEstandar;
import com.tfg.utilidades.Constantes;

public class Mina extends Edificio {
    public Mina(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        desbloqueado = false;
        generarRecursosConstantemente = true;
        setGeneradoresRecursosSegunNivelAldea();
    }

    private void setGeneradoresRecursosSegunNivelAldea() {
        int nivelAldea = aldea.getNivel();
        generadoresRecursos.clear();
        if (nivelAldea >= Constantes.Aldea.NIVEL_DESBLOQUEO_PIEDRA) generadoresRecursos.add(new GeneradorEstandar(RecursosEnum.PIEDRA));
        if (nivelAldea >= Constantes.Aldea.NIVEL_DESBLOQUEO_HIERRO) generadoresRecursos.add(new GeneradorEstandar(RecursosEnum.HIERRO));
        if (nivelAldea >= Constantes.Aldea.NIVEL_DESBLOQUEO_ORO) generadoresRecursos.add(new GeneradorEstandar(RecursosEnum.ORO));
    }

    @Override
    public void ajustarSegunDatosCargados() {
        setGeneradoresRecursosSegunNivelAldea();
        super.ajustarSegunDatosCargados();
    }

    @Override
    public void reiniciarDatos() {
        super.reiniciarDatos();
        setGeneradoresRecursosSegunNivelAldea();
    }
}
