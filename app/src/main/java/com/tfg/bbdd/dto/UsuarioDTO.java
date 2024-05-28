package com.tfg.bbdd.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String uid;
    private String email;
    private String tokenFmc;
    private boolean online;
    private boolean castillo;
    private int puntos;
    private long ultimoAtaque;
}
