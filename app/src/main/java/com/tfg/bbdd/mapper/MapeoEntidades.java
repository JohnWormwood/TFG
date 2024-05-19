package com.tfg.bbdd.mapper;

import com.tfg.bbdd.dto.AldeaDTO;
import com.tfg.bbdd.dto.CabaniaCazaDTO;
import com.tfg.bbdd.dto.EdificioDTO;
import com.tfg.bbdd.dto.RecursosDTO;
import com.tfg.bbdd.entidades.Recursos;
import com.tfg.bbdd.entidades.Usuario;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.edificios.Edificio;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;

import java.util.HashMap;
import java.util.Map;

public class MapeoEntidades {
    public com.tfg.bbdd.entidades.Aldea mapearAldea(Aldea aldea, Usuario usuario) {
        return new com.tfg.bbdd.entidades.Aldea(
                usuario.getEmail(),
                aldea.getNivel(),
                aldea.getPoblacion()
        );
    }

    public com.tfg.bbdd.entidades.Edificio mapearEdificio(Edificio edificio, com.tfg.bbdd.entidades.Edificio.EdificioId edificioId) {
        return new com.tfg.bbdd.entidades.Edificio(
                edificioId,
                edificio.getNivel(),
                edificio.getAldeanosAsignados(),
                edificio.isDesbloqueado()
        );
    }

    public com.tfg.bbdd.entidades.CabaniaCaza mapearCabaniaCaza(CabaniaCaza cabaniaCaza, Usuario usuario) {
        return new com.tfg.bbdd.entidades.CabaniaCaza(
                usuario.getEmail(),
                cabaniaCaza.getNivel(),
                cabaniaCaza.getAldeanosAsignados(),
                cabaniaCaza.isDesbloqueado(),
                cabaniaCaza.getTimerPartidaCaza().getSegundosRestantes(),
                cabaniaCaza.getAldeanosMuertosEnPartida(),
                cabaniaCaza.isPartidaActiva()
        );
    }

    public Recursos mapearRecursos(Aldea aldea, Usuario usuario) {
        return new Recursos(
                usuario.getEmail(),
                ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA),
                ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.TABLONES_MADERA),
                ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.COMIDA),
                ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.PIEDRA),
                ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.HIERRO),
                ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.ORO)
        );
    }

    public void cargarDatosEnAldea(com.tfg.bbdd.entidades.Aldea aldeaEntidad, Recursos recursos) {
        Aldea aldea = Aldea.getInstance();
        if (aldeaEntidad != null) {
            aldea.setNivel(aldeaEntidad.getNivel());
            aldea.setPoblacion(aldeaEntidad.getPoblacion());
            aldea.setRecursos(cargarRecursos(recursos));
        } else {
            // Nunca deberia ser null, pero si lo es se pone a la poblacion inicial
            aldea.setPoblacion(Constantes.Aldea.POBLACION_INICIAL);
        }
    }

    public void cargarDatosEnEdificio(Edificio edificio, com.tfg.bbdd.entidades.Edificio edificioEntidad) {
        if (edificioEntidad != null) {
            edificio.setNivel(edificioEntidad.getNivel());
            edificio.setAldeanosAsignados(edificioEntidad.getAldeanosAsignados());
            edificio.setDesbloqueado(edificioEntidad.getDesbloqueado());
        }
    }

    public void cargarDatosEnCabaniaCaza(CabaniaCaza cabaniaCaza, com.tfg.bbdd.entidades.CabaniaCaza cabaniaCazaEntidad) {
        if (cabaniaCazaEntidad != null) {
            cabaniaCaza.setNivel(cabaniaCazaEntidad.getNivel());
            cabaniaCaza.setAldeanosAsignados(cabaniaCazaEntidad.getAldeanosAsignados());
            cabaniaCaza.setDesbloqueado(cabaniaCazaEntidad.getDesbloqueado());
            cabaniaCaza.getTimerPartidaCaza().setSegundosRestantes(cabaniaCazaEntidad.getSegundosRestantes());
            cabaniaCaza.setAldeanosMuertosEnPartida(cabaniaCazaEntidad.getAldeanosMuertosEnPartida());
            cabaniaCaza.setPartidaActiva(cabaniaCazaEntidad.getPartidaActiva());
        }
    }

    public Map<RecursosEnum, Integer> cargarRecursos(Recursos recursosEntidad) {
        Map<RecursosEnum, Integer> recursos = new HashMap<>();
        if (recursosEntidad != null) {
            recursos.put(RecursosEnum.TRONCOS_MADERA, recursosEntidad.getTroncos());
            recursos.put(RecursosEnum.TABLONES_MADERA, recursosEntidad.getTablones());
            recursos.put(RecursosEnum.COMIDA, recursosEntidad.getComida());
            recursos.put(RecursosEnum.PIEDRA, recursosEntidad.getPiedra());
            recursos.put(RecursosEnum.HIERRO, recursosEntidad.getHierro());
            recursos.put(RecursosEnum.ORO, recursosEntidad.getOro());
        } else {
            // Nunca deberia ser null, pero si lo es se pone a 0 todos los recursos para evitar problemas
            recursos.put(RecursosEnum.TRONCOS_MADERA, 0);
            recursos.put(RecursosEnum.TABLONES_MADERA, 0);
            recursos.put(RecursosEnum.COMIDA, 0);
            recursos.put(RecursosEnum.PIEDRA, 0);
            recursos.put(RecursosEnum.HIERRO, 0);
            recursos.put(RecursosEnum.ORO, 0);
        }
        return recursos;
    }
}
