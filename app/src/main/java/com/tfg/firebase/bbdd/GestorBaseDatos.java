package com.tfg.firebase.bbdd;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg.firebase.bbdd.dto.AldeaDTO;
import com.tfg.firebase.bbdd.dto.CabaniaCazaDTO;
import com.tfg.firebase.bbdd.dto.EdificioDTO;
import com.tfg.firebase.bbdd.dto.RecursosDTO;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.edificios.Edificio;
import com.tfg.modelos.enums.RecursosEnum;

import java.util.HashMap;
import java.util.Objects;

public class GestorBaseDatos {

    public void guardarDatos(String email) {
        FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(email)
                .set(mapearDatosAldea());
    }

    private HashMap<String, Object> mapearDatosAldea() {
        Aldea aldea = Aldea.getInstance();

        HashMap<String, Object> datos = new HashMap<>();
        datos.put("recursos", mapearRecursos(aldea));
        datos.put("datos_aldea", mapearAldea(aldea));
        datos.put("cabania_caza",mapearCabaniaCaza(aldea.getCabaniaCaza()));
        datos.put("caseta_leniador", mapearEdificio(aldea.getCasetaLeniador()));
        datos.put("carpinteria",mapearEdificio(aldea.getCarpinteria()));
        datos.put("granja",mapearEdificio(aldea.getGranja()));
        datos.put("mina",mapearEdificio(aldea.getMina()));

        return datos;
    }

    private AldeaDTO mapearAldea(Aldea aldea) {
        AldeaDTO aldeaDTO = new AldeaDTO();
        aldeaDTO.setNivel(aldea.getNivel());
        aldeaDTO.setPoblacion(aldea.getPoblacion());
        aldeaDTO.setDefensas(0);

        return aldeaDTO;
    }

    private EdificioDTO mapearEdificio(Edificio edificio) {
        EdificioDTO edificioDTO = new EdificioDTO();
        edificioDTO.setNivel(edificio.getNivel());
        edificioDTO.setAldeanosAsignados(edificio.getAldeanosAsignados());

        return edificioDTO;
    }

    private CabaniaCazaDTO mapearCabaniaCaza(CabaniaCaza cabaniaCaza) {
        CabaniaCazaDTO cabaniaCazaDTO = new CabaniaCazaDTO();
        cabaniaCazaDTO.setNivel(cabaniaCaza.getNivel());
        cabaniaCazaDTO.setAldeanosAsignados(cabaniaCaza.getAldeanosAsignados());
        if (cabaniaCaza.getTimerPartidaCaza() != null)
            cabaniaCazaDTO.setSegundosRestantes(cabaniaCaza.getTimerPartidaCaza().getSegundosRestantes());
        cabaniaCazaDTO.setAldeanosMuertosEnPartida(cabaniaCaza.getAldeanosMuertosEnPartida());
        cabaniaCazaDTO.setPartidaActiva(cabaniaCaza.isPartidaActiva());

        return cabaniaCazaDTO;
    }

    private RecursosDTO mapearRecursos(Aldea aldea) {
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
}
