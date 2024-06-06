package com.tfg.bbdd.firebase;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tfg.R;
import com.tfg.bbdd.dto.AldeaDTO;
import com.tfg.bbdd.dto.CabaniaCazaDTO;
import com.tfg.bbdd.dto.EdificioDTO;
import com.tfg.bbdd.dto.RecursosDTO;
import com.tfg.bbdd.dto.UsuarioDTO;
import com.tfg.bbdd.mapper.MapeoDTO;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.eventos.callbacks.AtaqueCallback;
import com.tfg.eventos.callbacks.OperacionesDatosCallback;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.PopupManager;
import com.tfg.utilidades.Utilidades;

import java.util.HashMap;
import java.util.Map;

public class GestorFirestore {
    private final Aldea aldea = Aldea.getInstance();
    private final GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
    private final MapeoDTO mapeoDTO = new MapeoDTO();

    public void guardarDatos(String email, OperacionesDatosCallback callback) {
        FirestoreCRUD.actualizarConCallback(
                Constantes.BaseDatos.COLECCION_USUARIOS, email, mapearDatosAldea(), callback);
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
                        AldeaDTO aldeaDTO = document.get(
                                Constantes.BaseDatos.ALDEA, AldeaDTO.class);
                        RecursosDTO recursosDTO = document.get(
                                Constantes.BaseDatos.RECURSOS, RecursosDTO.class);
                        CabaniaCazaDTO cabaniaCazaDTO = document.get(
                                Constantes.BaseDatos.CABANIA_CAZA, CabaniaCazaDTO.class);
                        EdificioDTO casetaLeniadorDTO = document.get(
                                Constantes.BaseDatos.CASETA_LENIADOR, EdificioDTO.class);
                        EdificioDTO carpinteriaDTO = document.get(
                                Constantes.BaseDatos.CARPINTERIA, EdificioDTO.class);
                        EdificioDTO granjaDTO = document.get(
                                Constantes.BaseDatos.GRANJA, EdificioDTO.class);
                        EdificioDTO minaDTO = document.get(
                                Constantes.BaseDatos.MINA, EdificioDTO.class);
                        EdificioDTO castilloDTO = document.get(
                                Constantes.BaseDatos.CASTILLO, EdificioDTO.class);
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
                        popupManager.showPopup(
                                ((AppCompatActivity) callback).getString(R.string.bienvenida));
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
                    RecursosDTO recursosDTO = document.get(
                            Constantes.BaseDatos.RECURSOS, RecursosDTO.class);
                    EdificioDTO castilloDTO = document.get(
                            Constantes.BaseDatos.CASTILLO, EdificioDTO.class);

                    if (castilloDTO != null && recursosDTO != null) {
                        int defensas = castilloDTO.getAldeanosAsignados();
                        Log.d(getClass().getSimpleName(), "Defensas de " + victima.getEmail() + ": " + defensas);
                        Log.d(getClass().getSimpleName(), "Soldados enviados: " + soldadosEnviados);
                        boolean victoria;
                        if (defensas > soldadosEnviados) victoria = false;
                        else if (defensas < soldadosEnviados) victoria = true;
                        else {
                            // Si hay empate se decice aleatoriamente
                            int random = Utilidades.generarIntRandom(0, 1);
                            victoria = random != 0;
                        }

                        if (victoria) {
                            // Calcular cuantos recursos se roban
                            Map<RecursosEnum, Integer> recursosRobables = getRecurosRobables(recursosDTO);
                            // Quitar recursos a la victima
                            quitarRecursosVictima(recursosRobables, recursosDTO, castilloDTO, victima);
                            // Darle los recursos al atacante
                            darRecursosAtacante(recursosRobables);
                            // Aumentar puntos al ganar
                            gestorRealTimeDatabase.modificarPuntuacionUsuarioActual(
                                    Constantes.Castillo.PUNTOS_VICTORIA);
                        } else {
                            // Si el atacante pierde los soldados enviados mueren y bajan los puntos
                            aldea.setPoblacion(aldea.getPoblacion() - soldadosEnviados);
                            gestorRealTimeDatabase.modificarPuntuacionUsuarioActual(
                                    Constantes.Castillo.PUNTOS_DERROTA);
                        }
                        gestorRealTimeDatabase.guardarUltimoAtaque(System.currentTimeMillis());

                        callback.onAtaqueTerminado(victima, victoria);
                    } else callback.onError(new FirebaseFirestoreException(
                            "Error al obtener los datos necesarios para el ataque",
                            FirebaseFirestoreException.Code.NOT_FOUND));
                }).addOnFailureListener(callback::onError);
    }

    private Map<RecursosEnum, Integer> getRecurosRobables(RecursosDTO recursosDTO) {
        Map<RecursosEnum, Integer> recursosRobables = new HashMap<>();
        recursosRobables.put(RecursosEnum.TRONCOS_MADERA, Math.min(recursosDTO.getTroncos(),
                Constantes.Castillo.CANTIDAD_RECURSOS_ROBADOS));
        recursosRobables.put(RecursosEnum.TABLONES_MADERA, Math.min(recursosDTO.getTablones(),
                Constantes.Castillo.CANTIDAD_RECURSOS_ROBADOS));
        recursosRobables.put(RecursosEnum.COMIDA, Math.min(recursosDTO.getComida(),
                Constantes.Castillo.CANTIDAD_RECURSOS_ROBADOS));
        recursosRobables.put(RecursosEnum.PIEDRA, Math.min(recursosDTO.getPiedra(),
                Constantes.Castillo.CANTIDAD_RECURSOS_ROBADOS));
        recursosRobables.put(RecursosEnum.HIERRO, Math.min(recursosDTO.getHierro(),
                Constantes.Castillo.CANTIDAD_RECURSOS_ROBADOS));
        recursosRobables.put(RecursosEnum.ORO, Math.min(recursosDTO.getOro(),
                Constantes.Castillo.CANTIDAD_RECURSOS_ROBADOS));

        return recursosRobables;
    }

    private void quitarRecursosVictima(Map<RecursosEnum, Integer> recursosRobados,
                                       RecursosDTO recursosDTO, EdificioDTO castilloDTO, UsuarioDTO victima) {
        recursosDTO.setTroncos(recursosDTO.getTroncos() - ControladorRecursos.getCantidadRecurso(
                recursosRobados, RecursosEnum.TRONCOS_MADERA));
        recursosDTO.setTablones(recursosDTO.getTablones() - ControladorRecursos.getCantidadRecurso(
                recursosRobados, RecursosEnum.TABLONES_MADERA));
        recursosDTO.setComida(recursosDTO.getComida() - ControladorRecursos.getCantidadRecurso(
                recursosRobados, RecursosEnum.COMIDA));
        recursosDTO.setPiedra(recursosDTO.getPiedra() - ControladorRecursos.getCantidadRecurso(
                recursosRobados, RecursosEnum.PIEDRA));
        recursosDTO.setHierro(recursosDTO.getHierro() - ControladorRecursos.getCantidadRecurso(
                recursosRobados, RecursosEnum.HIERRO));
        recursosDTO.setOro(recursosDTO.getOro() - ControladorRecursos.getCantidadRecurso(
                recursosRobados, RecursosEnum.ORO));

        castilloDTO.setAldeanosAsignados(0);

        HashMap<String, Object> datos = new HashMap<>();
        datos.put(Constantes.BaseDatos.CASTILLO, castilloDTO);
        datos.put(Constantes.BaseDatos.RECURSOS, recursosDTO);

        FirestoreCRUD.actualizar(Constantes.BaseDatos.COLECCION_USUARIOS, victima.getEmail(), datos);
    }

    private void darRecursosAtacante(Map<RecursosEnum, Integer> recursosRobados) {
        ControladorRecursos.agregarRecursoSinExcederMax(
                aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA,
                ControladorRecursos.getCantidadRecurso(recursosRobados, RecursosEnum.TRONCOS_MADERA));
        ControladorRecursos.agregarRecursoSinExcederMax(
                aldea.getRecursos(), RecursosEnum.COMIDA,
                ControladorRecursos.getCantidadRecurso(recursosRobados, RecursosEnum.COMIDA));

        if (aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_TABLONES)
            ControladorRecursos.agregarRecursoSinExcederMax(
                    aldea.getRecursos(), RecursosEnum.TABLONES_MADERA,
                    ControladorRecursos.getCantidadRecurso(recursosRobados, RecursosEnum.TABLONES_MADERA));
        if (aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_PIEDRA)
            ControladorRecursos.agregarRecursoSinExcederMax(
                    aldea.getRecursos(), RecursosEnum.PIEDRA,
                    ControladorRecursos.getCantidadRecurso(recursosRobados, RecursosEnum.PIEDRA));
        if (aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_HIERRO)
            ControladorRecursos.agregarRecursoSinExcederMax(
                    aldea.getRecursos(), RecursosEnum.HIERRO,
                    ControladorRecursos.getCantidadRecurso(recursosRobados, RecursosEnum.HIERRO));
        if (aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_ORO)
            ControladorRecursos.agregarRecursoSinExcederMax(
                    aldea.getRecursos(), RecursosEnum.ORO,
                    ControladorRecursos.getCantidadRecurso(recursosRobados, RecursosEnum.ORO));
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
