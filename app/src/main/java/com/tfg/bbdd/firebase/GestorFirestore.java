package com.tfg.bbdd.firebase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg.bbdd.mapper.MapeoDatos;
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
    private MapeoDatos mapeoDatos = new MapeoDatos();

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
                        mapeoDatos.cargarDatosEnAldea(aldeaDTO, recursosDTO);
                        // Cabania Caza
                        mapeoDatos.cargarDatosEnCabaniaCaza(aldea.getCabaniaCaza(), cabaniaCazaDTO);
                        // Caseta Leniador
                        mapeoDatos.cargarDatosEnEdificio(aldea.getCasetaLeniador(), casetaLeniadorDTO);
                        // Carpinteria
                        mapeoDatos.cargarDatosEnEdificio(aldea.getCarpinteria(), carpinteriaDTO);
                        // Granja
                        mapeoDatos.cargarDatosEnEdificio(aldea.getGranja(), granjaDTO);
                        // Mina
                        mapeoDatos.cargarDatosEnEdificio(aldea.getMina(), minaDTO);
                        // Castillo
                        mapeoDatos.cargarDatosEnEdificio(aldea.getCastillo(), castilloDTO);
                    }
                    aldea.ajustarSegunDatosCargados();
                    gestorRealTimeDatabase.actualizarEstadoConexion(true);
                    callback.onDatosCargados();
                });
    }

    private HashMap<String, Object> mapearDatosAldea() {

        HashMap<String, Object> datos = new HashMap<>();
        datos.put(Constantes.BaseDatos.RECURSOS, mapeoDatos.mapearRecursos(aldea));
        datos.put(Constantes.BaseDatos.ALDEA, mapeoDatos.mapearAldea(aldea));
        datos.put(Constantes.BaseDatos.CABANIA_CAZA, mapeoDatos.mapearCabaniaCaza(aldea.getCabaniaCaza()));
        datos.put(Constantes.BaseDatos.CASETA_LENIADOR, mapeoDatos.mapearEdificio(aldea.getCasetaLeniador()));
        datos.put(Constantes.BaseDatos.CARPINTERIA, mapeoDatos.mapearEdificio(aldea.getCarpinteria()));
        datos.put(Constantes.BaseDatos.GRANJA, mapeoDatos.mapearEdificio(aldea.getGranja()));
        datos.put(Constantes.BaseDatos.MINA, mapeoDatos.mapearEdificio(aldea.getMina()));
        datos.put(Constantes.BaseDatos.CASTILLO, mapeoDatos.mapearEdificio(aldea.getCastillo()));

        return datos;
    }
}
