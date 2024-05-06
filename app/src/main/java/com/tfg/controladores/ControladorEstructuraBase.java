package com.tfg.controladores;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.EstructuraBase;
import com.tfg.modelos.PrecioMejora;
import com.tfg.modelos.enums.RecursosEnum;

import java.util.HashMap;
import java.util.Map;

public final class ControladorEstructuraBase {
    public static boolean puedeSubirDeNivel(PrecioMejora precio) {
        Map<RecursosEnum, Integer> recursosInicialesAldea = new HashMap<>(Aldea.getInstance().getRecursos());
        for (Map.Entry<RecursosEnum, Integer> entry : precio.getRecursos().entrySet()) {
            Integer cantidadAldea = Aldea.getInstance().getRecursos().get(entry.getKey());
            if (cantidadAldea != null && cantidadAldea < entry.getValue()) {
                Aldea.getInstance().setRecursos(recursosInicialesAldea);
                return false;
            } else {
                ControladorRecursos.consumirRecurso(Aldea.getInstance().getRecursos(), entry.getKey(), entry.getValue());
            }
        }
        return true;
    }

    public static Map<RecursosEnum, Integer> getPreciosMejoraEstructura(EstructuraBase estructura) {
        return estructura.getPreciosMejoras().get(estructura.getNivel()-1).getRecursos();
    }

}
