package com.tfg.firebase.bbdd;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tfg.eventos.callbacks.OperacionesDatosCallback;
import com.tfg.firebase.bbdd.dto.AldeaDTO;
import com.tfg.firebase.bbdd.dto.CabaniaCazaDTO;
import com.tfg.firebase.bbdd.dto.EdificioDTO;
import com.tfg.firebase.bbdd.dto.RecursosDTO;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.edificios.Edificio;
import com.tfg.modelos.enums.RecursosEnum;

import java.util.HashMap;
import java.util.Map;

public class GestorBaseDatos {

    private final String COLECCION_USUARIOS = "usuarios";
    private final String ALDEA = "datos_aldea";
    private final String RECURSOS = "recursos";
    private final String CABANIA_CAZA = "cabania_caza";
    private final String CASETA_LENIADOR = "caseta_leniador";
    private final String CARPINTERIA = "carpinteria";
    private final String GRANJA = "granja";
    private final String MINA = "mina";
    Aldea aldea = Aldea.getInstance();
    public void guardarDatos(String email, OperacionesDatosCallback callback) {
        FirebaseFirestore.getInstance()
                .collection(COLECCION_USUARIOS)
                .document(email)
                .set(mapearDatosAldea());
        callback.onDatosGuardados();
    }

    public void cargarDatos(String email, OperacionesDatosCallback callback) {
        Aldea aldea = Aldea.getInstance();

        FirebaseFirestore.getInstance()
                .collection(COLECCION_USUARIOS)
                .document(email).get().addOnSuccessListener(document -> {
                    if (document.exists()) {
                        AldeaDTO aldeaDTO = document.get(ALDEA, AldeaDTO.class);
                        RecursosDTO recursosDTO = document.get(RECURSOS, RecursosDTO.class);
                        CabaniaCazaDTO cabaniaCazaDTO = document.get(CABANIA_CAZA, CabaniaCazaDTO.class);
                        EdificioDTO casetaLeniadorDTO = document.get(CASETA_LENIADOR, EdificioDTO.class);
                        EdificioDTO carpinteriaDTO = document.get(CARPINTERIA, EdificioDTO.class);
                        EdificioDTO granjaDTO = document.get(GRANJA, EdificioDTO.class);
                        EdificioDTO minaDTO = document.get(MINA, EdificioDTO.class);
                        // Aldea
                        cargarDatosEnAldea(aldeaDTO, recursosDTO);
                        // Cabania Caza
                        cargarDatosEnCabaniaCaza(aldea.getCabaniaCaza(), cabaniaCazaDTO);
                        // Caseta Leniador
                        cargarDatosEnEdificio(aldea.getCasetaLeniador(), casetaLeniadorDTO);
                        // Carpinteria
                        cargarDatosEnEdificio(aldea.getCarpinteria(), carpinteriaDTO);
                        // Granja
                        cargarDatosEnEdificio(aldea.getGranja(), granjaDTO);
                        // Mina
                        cargarDatosEnEdificio(aldea.getMina(), minaDTO);
                    }
                    aldea.ajustarSegunDatosCargados();
                    callback.onDatosCargados();
                });
    }

    private void cargarDatosEnAldea(AldeaDTO aldeaDTO, RecursosDTO recursosDTO) {
        if (aldeaDTO != null) {
            aldea.setNivel(aldeaDTO.getNivel());
            aldea.setPoblacion(aldeaDTO.getPoblacion());
            //aldea.setDefensas(aldeaDTO.getDefensas());
            aldea.setRecursos(cargarRecursos(recursosDTO));
        } else {
            // Nunca deberia ser null, pero si lo es se pone a 0 de poblacion para evitar problemas
            aldea.setPoblacion(0);
        }
    }

    private void cargarDatosEnEdificio(Edificio edificio, EdificioDTO edificioDTO) {
        if (edificioDTO != null) {
            edificio.setNivel(edificioDTO.getNivel());
            edificio.setAldeanosAsignados(edificioDTO.getAldeanosAsignados());
            edificio.setDesbloqueado(edificioDTO.isDesbloqueado());
        }
    }

    private void cargarDatosEnCabaniaCaza(CabaniaCaza cabaniaCaza, CabaniaCazaDTO cabaniaCazaDTO) {
        if (cabaniaCazaDTO != null) {
            cargarDatosEnEdificio(cabaniaCaza, cabaniaCazaDTO);
            cabaniaCaza.getTimerPartidaCaza().setSegundosRestantes(cabaniaCazaDTO.getSegundosRestantes());
            cabaniaCaza.setAldeanosMuertosEnPartida(cabaniaCazaDTO.getAldeanosMuertosEnPartida());
            cabaniaCaza.setPartidaActiva(cabaniaCazaDTO.isPartidaActiva());
        }
    }

    private Map<RecursosEnum, Integer> cargarRecursos(RecursosDTO recursosDTO) {
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


    private HashMap<String, Object> mapearDatosAldea() {
        Aldea aldea = Aldea.getInstance();

        HashMap<String, Object> datos = new HashMap<>();
        datos.put(RECURSOS, mapearRecursos(aldea));
        datos.put(ALDEA, mapearAldea(aldea));
        datos.put(CABANIA_CAZA,mapearCabaniaCaza(aldea.getCabaniaCaza()));
        datos.put(CASETA_LENIADOR, mapearEdificio(aldea.getCasetaLeniador()));
        datos.put(CARPINTERIA,mapearEdificio(aldea.getCarpinteria()));
        datos.put(GRANJA,mapearEdificio(aldea.getGranja()));
        datos.put(MINA,mapearEdificio(aldea.getMina()));

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
        edificioDTO.setDesbloqueado(edificio.isDesbloqueado());

        return edificioDTO;
    }

    private CabaniaCazaDTO mapearCabaniaCaza(CabaniaCaza cabaniaCaza) {
        CabaniaCazaDTO cabaniaCazaDTO = new CabaniaCazaDTO();
        cabaniaCazaDTO.setNivel(cabaniaCaza.getNivel());
        cabaniaCazaDTO.setAldeanosAsignados(cabaniaCaza.getAldeanosAsignados());
        cabaniaCazaDTO.setDesbloqueado(cabaniaCaza.isDesbloqueado());
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
