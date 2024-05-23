package com.tfg.eventos.callbacks;

import com.tfg.bbdd.dto.UsuarioDTO;

public interface AtaqueCallback {
    void onAtaqueTerminado(UsuarioDTO usuarioDTO, boolean victoria);
    void onError(Exception e);
}