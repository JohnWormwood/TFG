package com.tfg.modelos.edificios;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tfg.bbdd.dto.UsuarioDTO;
import com.tfg.bbdd.firebase.GestorFirestore;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;
import com.tfg.bbdd.firebase.service.NotificacionesService;
import com.tfg.eventos.LanzadorEventos;
import com.tfg.eventos.callbacks.AtaqueCallback;
import com.tfg.eventos.callbacks.ObtenerUsuarioCallback;
import com.tfg.eventos.listeners.AtaqueEventListener;
import com.tfg.modelos.Aldea;

import lombok.Getter;

@Getter
public class Castillo extends Edificio implements ObtenerUsuarioCallback, AtaqueCallback {

    private String victimaAtaque;
    private int soldadosEnviados;
    private LanzadorEventos<AtaqueEventListener> lanzadorEventos;

    public Castillo(int aldeanosAsignados, Aldea aldea) {
        super(aldeanosAsignados, aldea);
        generarRecursosConstantemente = false;
        desbloqueado = false;
        lanzadorEventos = new LanzadorEventos<>();
    }

    public void iniciarIncursion(int soldadosEnviados) {
        GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
        gestorRealTimeDatabase.getUsuarioAtacableAleatorio(this);
        this.soldadosEnviados = soldadosEnviados;
    }

    // Manejar eventos de ObtenerUsuarioCallback
    @Override
    public void onExito(UsuarioDTO usuarioDTO) {
        victimaAtaque = usuarioDTO.getEmail();
        if (victimaAtaque != null) {
            GestorFirestore gestorFirestore = new GestorFirestore();
            gestorFirestore.gestionarAtaque(usuarioDTO, soldadosEnviados, this);
        } else {
            lanzadorEventos.lanzarEvento(evento -> evento.onError(
                    new FirebaseFirestoreException(
                            "En estos momentos no hay ningun usuario al que se pueda atacar, intentalo mas tarde",
                            FirebaseFirestoreException.Code.NOT_FOUND
                    )
            ));
        }
    }

    @Override
    public void onError(DatabaseError databaseError) {
        lanzadorEventos.lanzarEvento(evento -> evento.onError(databaseError.toException()));
    }

    // Manejar eventos de AtaqueCallback
    @Override
    public void onAtaqueTerminado(UsuarioDTO usuarioDTO, boolean victoria) {
        NotificacionesService notificacionesService = new NotificacionesService();
        notificacionesService.enviarNotificacionAtaque(usuarioDTO.getTokenFmc(), victoria);
        lanzadorEventos.lanzarEvento(evento -> evento.onAtaqueTerminado(victoria));
    }

    @Override
    public void onError(Exception e) {
        lanzadorEventos.lanzarEvento(evento -> evento.onError(e));
    }
}
