package com.tfg.bbdd.firebase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tfg.bbdd.mapper.MapeoDTO;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.eventos.callbacks.AtaqueCallback;
import com.tfg.eventos.callbacks.OperacionesDatosCallback;
import com.tfg.bbdd.dto.AldeaDTO;
import com.tfg.bbdd.dto.CabaniaCazaDTO;
import com.tfg.bbdd.dto.EdificioDTO;
import com.tfg.bbdd.dto.RecursosDTO;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.edificios.Carpinteria;
import com.tfg.modelos.edificios.CasetaLeniador;
import com.tfg.modelos.edificios.Castillo;
import com.tfg.modelos.edificios.Granja;
import com.tfg.modelos.edificios.Mina;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.Utilidades;

import java.util.HashMap;
import java.util.Map;

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
                    } else {
                        aldea.reiniciarDatos();
                    }
                    aldea.ajustarSegunDatosCargados();
                    gestorRealTimeDatabase.actualizarEstadoConexion(true);
                    callback.onDatosCargados();
                });
    }

    public void gestionarAtaque(String emailVictima, int soldadosEnviados, AtaqueCallback callback) {
        FirebaseFirestore.getInstance()
                .collection(Constantes.BaseDatos.COLECCION_USUARIOS)
                .document(emailVictima).get().addOnSuccessListener(document -> {
                    RecursosDTO recursosDTO = document.get(Constantes.BaseDatos.RECURSOS, RecursosDTO.class);
                    EdificioDTO castilloDTO = document.get(Constantes.BaseDatos.CASTILLO, EdificioDTO.class);

                    if (castilloDTO != null && recursosDTO != null) {
                        int defensas = castilloDTO.getAldeanosAsignados();
                        System.out.println("Defensas de "+emailVictima+": "+defensas);
                        System.out.println("Soldados enviados: "+soldadosEnviados);
                        boolean victoria;
                        if (defensas > soldadosEnviados) {
                            victoria = false;
                        } else if (defensas < soldadosEnviados) {
                            victoria = true;
                        } else {
                            // En caso de que haya los mismos defensores que atacantes
                            // se decide aleatoriamente con un 50% para cada jugador
                            int random = Utilidades.generarIntRandom(0, 1);
                            victoria = random != 0;
                        }

                        if (victoria) {
                            int troncos = Math.max(recursosDTO.getTroncos()-10, 0);
                            int tablones = Math.max(recursosDTO.getTablones()-10, 0);
                            int comida = Math.max(recursosDTO.getComida()-10, 0);
                            int piedra = Math.max(recursosDTO.getPiedra()-10, 0);
                            int hierro = Math.max(recursosDTO.getHierro()-10, 0);
                            int oro = Math.max(recursosDTO.getOro()-10, 0);

                            recursosDTO.setTroncos(troncos);
                            recursosDTO.setTablones(tablones);
                            recursosDTO.setComida(comida);
                            recursosDTO.setPiedra(piedra);
                            recursosDTO.setHierro(hierro);
                            recursosDTO.setOro(oro);

                            castilloDTO.setAldeanosAsignados(0);

                            // Quitar recursos a la victima
                            HashMap<String, Object> datos = new HashMap<>();
                            datos.put(Constantes.BaseDatos.CASTILLO, castilloDTO);
                            datos.put(Constantes.BaseDatos.RECURSOS, recursosDTO);

                            FirestoreCRUD.actualizar(Constantes.BaseDatos.COLECCION_USUARIOS, emailVictima, datos);

                            // Darle los recursos al atacante
                            ControladorRecursos.agregarRecurso(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA, 10);
                            ControladorRecursos.agregarRecurso(aldea.getRecursos(), RecursosEnum.TABLONES_MADERA, 10);
                            ControladorRecursos.agregarRecurso(aldea.getRecursos(), RecursosEnum.COMIDA, 10);
                            ControladorRecursos.agregarRecurso(aldea.getRecursos(), RecursosEnum.PIEDRA, 10);
                            ControladorRecursos.agregarRecurso(aldea.getRecursos(), RecursosEnum.HIERRO, 10);
                            ControladorRecursos.agregarRecurso(aldea.getRecursos(), RecursosEnum.ORO, 10);


                        } else {

                        }
                        callback.onAtaqueTerminado(victoria);
                    } else {
                        callback.onError(
                                new FirebaseFirestoreException(
                                        "Error al obtener los datos necesarios para el ataque",
                                        FirebaseFirestoreException.Code.NOT_FOUND
                                )
                        );
                    }
                }).addOnFailureListener(callback::onError);
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
