package com.tfg.bbdd.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "datos_aldea",
        foreignKeys = @ForeignKey(
                entity = Usuario.class,
                parentColumns = "email",
                childColumns = "usuario",
                onDelete = ForeignKey.CASCADE
        )
)
public class Aldea {
    @PrimaryKey @NonNull
    private String usuario;

    @ColumnInfo(name = "nivel") @NonNull
    private Integer nivel;

    @ColumnInfo(name = "poblacion") @NonNull
    private Integer poblacion;
}
