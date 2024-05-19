package com.tfg.bbdd.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String email;
    private boolean sincronizadoConFirebase;
}
