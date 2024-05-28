package com.tfg.bbdd.mapper;

import com.tfg.bbdd.dto.AldeaDTO;
import com.tfg.bbdd.dto.CabaniaCazaDTO;
import com.tfg.bbdd.dto.EdificioDTO;
import com.tfg.bbdd.dto.RecursosDTO;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.edificios.Edificio;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;

import java.util.HashMap;
import java.util.Map;

public class MapeoDTO {
    public AldeaDTO aldeaToAldeaDTO(Aldea aldea) {
        AldeaDTO aldeaDTO = new AldeaDTO();
        aldeaDTO.setNivel(aldea.getNivel());
        aldeaDTO.setPoblacion(aldea.getPoblacion());
        aldeaDTO.setDefensas(0);

        return aldeaDTO;
    }

    public EdificioDTO edificioToEdificioDTO(Edificio edificio) {
        EdificioDTO edificioDTO = new EdificioDTO();
        System.out.println(edificio.getClass().getName()+" nivel = "+edificio.getNivel());
        edificioDTO.setNivel(edificio.getNivel());
        edificioDTO.setAldeanosAsignados(edificio.getAldeanosAsignados());
        edificioDTO.setDesbloqueado(edificio.isDesbloqueado());

        return edificioDTO;
    }

    public CabaniaCazaDTO cabaniaCazaToCabaniaCazaDTO(CabaniaCaza cabaniaCaza) {
        CabaniaCazaDTO cabaniaCazaDTO = new CabaniaCazaDTO();
        cabaniaCazaDTO.setNivel(cabaniaCaza.getNivel());
        cabaniaCazaDTO.setAldeanosAsignados(cabaniaCaza.getAldeanosAsignados());
        cabaniaCazaDTO.setDesbloqueado(cabaniaCaza.isDesbloqueado());
        cabaniaCazaDTO.setSegundosRestantes(cabaniaCaza.getTimerPartidaCaza().getSegundosRestantes());
        cabaniaCazaDTO.setAldeanosMuertosEnPartida(cabaniaCaza.getAldeanosMuertosEnPartida());
        cabaniaCazaDTO.setPartidaActiva(cabaniaCaza.isPartidaActiva());

        return cabaniaCazaDTO;
    }

    public RecursosDTO mapearRecursos(Aldea aldea) {
        RecursosDTO recursosDTO = new RecursosDTO();
        Integer cantidad;
        cantidad = aldea.getRecursos().get(RecursosEnum.TRONCOS_MADERA);
        if (cantidad != null) recursosDTO.setTroncos(cantidad);
        cantidad = aldea.getRecursos().get(RecursosEnum.TABLONES_MADERA);
        if (cantidad != null) recursosDTO.setTablones(cantidad);
        cantidad = aldea.getRecursos().get(RecursosEnum.COMIDA);
        if (cantidad != null) recursosDTO.setComida(cantidad);
        cantidad = aldea.getRecursos().get(RecursosEnum.PIEDRA);
        if (cantidad != null) recursosDTO.setPiedra(cantidad);
        cantidad = aldea.getRecursos().get(RecursosEnum.HIERRO);
        if (cantidad != null) recursosDTO.setHierro(cantidad);
        cantidad = aldea.getRecursos().get(RecursosEnum.ORO);
        if (cantidad != null) recursosDTO.setOro(cantidad);

        return recursosDTO;
    }

    public void cargarDatosEnAldea(AldeaDTO aldeaDTO, RecursosDTO recursosDTO) {
        Aldea aldea = Aldea.getInstance();
        if (aldeaDTO != null) {
            aldea.setNivel(aldeaDTO.getNivel());
            aldea.setPoblacion(aldeaDTO.getPoblacion());
            //aldea.setDefensas(aldeaDTO.getDefensas());
            aldea.setRecursos(cargarRecursos(recursosDTO));
        } else {
            // Nunca deberia ser null, pero si lo es se pone a la poblacion inicial
            aldea.setPoblacion(Constantes.Aldea.POBLACION_INICIAL);
        }
    }

    public void cargarDatosEnEdificio(Edificio edificio, EdificioDTO edificioDTO) {
        if (edificioDTO != null) {
            edificio.setNivel(edificioDTO.getNivel());
            edificio.setAldeanosAsignados(edificioDTO.getAldeanosAsignados());
            edificio.setDesbloqueado(edificioDTO.isDesbloqueado());
        }
    }

    public void cargarDatosEnCabaniaCaza(CabaniaCaza cabaniaCaza, CabaniaCazaDTO cabaniaCazaDTO) {
        if (cabaniaCazaDTO != null) {
            cargarDatosEnEdificio(cabaniaCaza, cabaniaCazaDTO);
            cabaniaCaza.getTimerPartidaCaza().setSegundosRestantes(cabaniaCazaDTO.getSegundosRestantes());
            cabaniaCaza.setAldeanosMuertosEnPartida(cabaniaCazaDTO.getAldeanosMuertosEnPartida());
            cabaniaCaza.setPartidaActiva(cabaniaCazaDTO.isPartidaActiva());
        }
    }

    public Map<RecursosEnum, Integer> cargarRecursos(RecursosDTO recursosDTO) {
        Map<RecursosEnum, Integer> recursos = new HashMap<>();
        if (recursosDTO != null) {
            recursos.put(RecursosEnum.TRONCOS_MADERA, recursosDTO.getTroncos());
            recursos.put(RecursosEnum.TABLONES_MADERA, recursosDTO.getTablones());
            recursos.put(RecursosEnum.COMIDA, recursosDTO.getComida());
            recursos.put(RecursosEnum.PIEDRA, recursosDTO.getPiedra());
            recursos.put(RecursosEnum.HIERRO, recursosDTO.getHierro());
            recursos.put(RecursosEnum.ORO, recursosDTO.getOro());
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
