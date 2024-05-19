package com.tfg.bbdd.firebase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg.bbdd.mapper.MapeoDTO;
import com.tfg.eventos.callbacks.OperacionesDatosCallback;
import com.tfg.bbdd.dto.AldeaDTO;
import com.tfg.bbdd.dto.CabaniaCazaDTO;
import com.tfg.bbdd.dto.EdificioDTO;
import com.tfg.bbdd.dto.RecursosDTO;
import com.tfg.modelos.Aldea;
import com.tfg.utilidades.Constantes;

import java.util.HashMap;

public class GestorFirestore {
    private Aldea aldea = Aldea.getInstance();
    private GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
    private MapeoDTO mapeoDTO = new MapeoDTO();

    public void guardarDatos(String email, OperacionesDatosCallback callback) {
        gestorRealTimeDatabase.actualizarEstadoConexion(false);
        FirestoreCRUD.actualizarConCallback(Constantes.BaseDatos.COLECCION_USUARIOS, email, mapearDatosAldea(), callback);
    }

    public void cargarDatos(String email, OperacionesDatosCallback callback) {
        /* Esto se hace aqui, en lugar de tener un metodo obtener en FirestoreCRUD
         * porque hay que asegurar que se lanza el callback cuando todos los datos
         * se han cargado en los edificios y en la aldea
         */
        FirebaseFirestore.getInstance()
                .collection(Constantes.BaseDatos.COLECCION_USUARIOS)
                .document(email).get().addOnSuccessListener(document -> {
                    if (document.exists()) {
                        AldeaDTO aldeaDTO = document.get(Constantes.BaseDatos.ALDEA, AldeaDTO.class);
                        RecursosDTO recursosDTO = document.get(Constantes.BaseDatos.RECURSOS, RecursosDTO.class);
                        CabaniaCazaDTO cabaniaCazaDTO = document.get(Constantes.BaseDatos.CABANIA_CAZA, CabaniaCazaDTO.class);
                        EdificioDTO casetaLeniadorDTO = document.get(Constantes.BaseDatos.CASETA_LENIADOR, EdificioDTO.class);
                        EdificioDTO carpinteriaDTO = document.get(Constantes.BaseDatos.CARPINTERIA, EdificioDTO.class);
                        EdificioDTO granjaDTO = document.get(Constantes.BaseDatos.GRANJA, EdificioDTO.class);
                        EdificioDTO minaDTO = document.get(Constantes.BaseDatos.MINA, EdificioDTO.class);
                        EdificioDTO castilloDTO = document.get(Constantes.BaseDatos.CASTILLO, EdificioDTO.class);
                        // Aldea
                        mapeoDTO.cargarDatosEnAldea(aldeaDTO, recursosDTO);
                        // Cabania Caza
                        mapeoDTO.cargarDatosEnCabaniaCaza(aldea.getCabaniaCaza(), cabaniaCazaDTO);
                        // Caseta Leniador
                        mapeoDTO.cargarDatosEnEdificio(aldea.getCasetaLeniador(), casetaLeniadorDTO);
                        // Carpinteria
                        mapeoDTO.cargarDatosEnEdificio(aldea.getCarpinteria(), carpinteriaDTO);
                        // Granja
                        mapeoDTO.cargarDatosEnEdificio(aldea.getGranja(), granjaDTO);
                        // Mina
                        mapeoDTO.cargarDatosEnEdificio(aldea.getMina(), minaDTO);
                        // Castillo
                        mapeoDTO.cargarDatosEnEdificio(aldea.getCastillo(), castilloDTO);
                    }
                    aldea.ajustarSegunDatosCargados();
                    gestorRealTimeDatabase.actualizarEstadoConexion(true);
                    callback.onDatosCargados();
                });
    }

    private HashMap<String, Object> mapearDatosAldea() {

        HashMap<String, Object> datos = new HashMap<>();
        datos.put(Constantes.BaseDatos.RECURSOS, mapeoDTO.mapearRecursos(aldea));
        datos.put(Constantes.BaseDatos.ALDEA, mapeoDTO.aldeaToAldeaDTO(aldea));
        datos.put(Constantes.BaseDatos.CABANIA_CAZA, mapeoDTO.cabaniaCazaToCabaniaCazaDTO(aldea.getCabaniaCaza()));
        datos.put(Constantes.BaseDatos.CASETA_LENIADOR, mapeoDTO.edificioToEdificioDTO(aldea.getCasetaLeniador()));
        datos.put(Constantes.BaseDatos.CARPINTERIA, mapeoDTO.edificioToEdificioDTO(aldea.getCarpinteria()));
        datos.put(Constantes.BaseDatos.GRANJA, mapeoDTO.edificioToEdificioDTO(aldea.getGranja()));
        datos.put(Constantes.BaseDatos.MINA, mapeoDTO.edificioToEdificioDTO(aldea.getMina()));
        datos.put(Constantes.BaseDatos.CASTILLO, mapeoDTO.edificioToEdificioDTO(aldea.getCastillo()));

        return datos;
    }
}
