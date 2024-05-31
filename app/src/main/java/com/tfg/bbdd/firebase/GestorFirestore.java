package com.tfg.bbdd.firebase;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tfg.R;
import com.tfg.bbdd.dto.UsuarioDTO;
import com.tfg.bbdd.mapper.MapeoDTO;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.eventos.callbacks.AtaqueCallback;
import com.tfg.eventos.callbacks.OperacionesDatosCallback;
import com.tfg.bbdd.dto.AldeaDTO;
import com.tfg.bbdd.dto.CabaniaCazaDTO;
import com.tfg.bbdd.dto.EdificioDTO;
import com.tfg.bbdd.dto.RecursosDTO;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.PopupManager;
import com.tfg.utilidades.Utilidades;

import java.util.HashMap;

public class GestorFirestore {
    private Aldea aldea = Aldea.getInstance();
    private GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
    private MapeoDTO mapeoDTO = new MapeoDTO();

    public void guardarDatos(String email, OperacionesDatosCallback callback) {
        FirestoreCRUD.actualizarConCallback(Constantes.BaseDatos.COLECCION_USUARIOS, email, mapearDatosAldea(), callback);
        gestorRealTimeDatabase.actualizarEstadoConexion(false);
    }

    public void cargarDatos(String email, OperacionesDatosCallback callback) {
        /* Esto se hace aqui, en lugar de tener un metodo obtener en FirestoreCRUD
         * porque hay que asegurar que se lanza el callback cuando todos los datos
         * se han cargado en los edificios y en la aldea
         */
        FirebaseFirestore.getInstance()
                .collection(Constantes.BaseDatos.COLECCION_USUARIOS)
                .document(email).get().addOnSuccessListener(document -> {
                    aldea.reiniciarDatos();
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
                        PopupManager popupManager = new PopupManager((AppCompatActivity) callback);
                        popupManager.showPopup(((AppCompatActivity) callback).getString(R.string.bienvenida));
                    }
                    aldea.ajustarSegunDatosCargados();
                    gestorRealTimeDatabase.actualizarEstadoConexion(true);
                    callback.onDatosCargados();
                });
    }

    public void gestionarAtaque(UsuarioDTO victima, int soldadosEnviados, AtaqueCallback callback) {
        FirebaseFirestore.getInstance()
                .collection(Constantes.BaseDatos.COLECCION_USUARIOS)
                .document(victima.getEmail()).get().addOnSuccessListener(document -> {
                    RecursosDTO recursosDTO = document.get(Constantes.BaseDatos.RECURSOS, RecursosDTO.class);
                    EdificioDTO castilloDTO = document.get(Constantes.BaseDatos.CASTILLO, EdificioDTO.class);

                    if (castilloDTO != null && recursosDTO != null) {
                        int defensas = castilloDTO.getAldeanosAsignados();
                        System.out.println("Defensas de "+victima.getEmail()+": "+defensas);
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
                            final int CANTIDAD_RECURSO = 10;
                            // Calcular cuantos recursos se roban
                            int troncos = Math.min(recursosDTO.getTroncos(), CANTIDAD_RECURSO);
                            int tablones = Math.min(recursosDTO.getTablones(), CANTIDAD_RECURSO);
                            int comida = Math.min(recursosDTO.getComida(), CANTIDAD_RECURSO);
                            int piedra = Math.min(recursosDTO.getPiedra(), CANTIDAD_RECURSO);
                            int hierro = Math.min(recursosDTO.getHierro(), CANTIDAD_RECURSO);
                            int oro = Math.min(recursosDTO.getOro(), CANTIDAD_RECURSO);

                            // Quitar recursos a la victima
                            recursosDTO.setTroncos(recursosDTO.getTroncos()-troncos);
                            recursosDTO.setTablones(recursosDTO.getTablones()-tablones);
                            recursosDTO.setComida(recursosDTO.getComida()-comida);
                            recursosDTO.setPiedra(recursosDTO.getPiedra()-piedra);
                            recursosDTO.setHierro(recursosDTO.getHierro()-hierro);
                            recursosDTO.setOro(recursosDTO.getOro()-oro);

                            castilloDTO.setAldeanosAsignados(0);

                            HashMap<String, Object> datos = new HashMap<>();
                            datos.put(Constantes.BaseDatos.CASTILLO, castilloDTO);
                            datos.put(Constantes.BaseDatos.RECURSOS, recursosDTO);

                            FirestoreCRUD.actualizar(Constantes.BaseDatos.COLECCION_USUARIOS, victima.getEmail(), datos);

                            // Darle los recursos al atacante
                            ControladorRecursos.agregarRecursoSinExcederMax(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA, troncos);
                            ControladorRecursos.agregarRecursoSinExcederMax(aldea.getRecursos(), RecursosEnum.COMIDA, comida);

                            if (aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_TABLONES)
                                ControladorRecursos.agregarRecursoSinExcederMax(aldea.getRecursos(), RecursosEnum.TABLONES_MADERA, tablones);
                            if (aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_PIEDRA)
                                ControladorRecursos.agregarRecursoSinExcederMax(aldea.getRecursos(), RecursosEnum.PIEDRA, piedra);
                            if (aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_HIERRO)
                                ControladorRecursos.agregarRecursoSinExcederMax(aldea.getRecursos(), RecursosEnum.HIERRO, hierro);
                            if (aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_ORO)
                                ControladorRecursos.agregarRecursoSinExcederMax(aldea.getRecursos(), RecursosEnum.ORO, oro);

                            // Aumentar puntos al ganar
                            gestorRealTimeDatabase.modificarPuntuacionUsuarioActual(Constantes.Castillo.PUNTOS_VICTORIA);
                        } else {
                            // Si el atacante pierde los soldados enviados mueren y bajan los puntos
                            aldea.setPoblacion(aldea.getPoblacion()-soldadosEnviados);
                            gestorRealTimeDatabase.modificarPuntuacionUsuarioActual(Constantes.Castillo.PUNTOS_DERROTA);
                        }
                        gestorRealTimeDatabase.guardarUltimoAtaque(System.currentTimeMillis());

                        callback.onAtaqueTerminado(victima, victoria);
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
