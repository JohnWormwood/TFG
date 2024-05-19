package com.tfg.bbdd.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "recursos",
        foreignKeys = @ForeignKey(
                entity = Usuario.class,
                parentColumns = "email",
                childColumns = "usuario",
                onDelete = ForeignKey.CASCADE
        )
)
public class Recursos {
    @PrimaryKey @NonNull
    private String usuario;

    @ColumnInfo(name = "troncos") @NonNull
    private Integer troncos;

    @ColumnInfo(name = "tablones") @NonNull
    private Integer tablones;

    @ColumnInfo(name = "comida") @NonNull
    private Integer comida;

    @ColumnInfo(name = "piedra") @NonNull
    private Integer piedra;

    @ColumnInfo(name = "hierro") @NonNull
    private Integer hierro;

    @ColumnInfo(name = "oro") @NonNull
    private Integer oro;
}
