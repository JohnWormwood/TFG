package com.tfg.bbdd.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "usuarios")
public class Usuario {
    @PrimaryKey
    @NonNull
    private String email;
    @ColumnInfo(name = "sincornizado_con_firebase")
    @NonNull
    private Boolean sincronizadoConFirebase;
}
