package com.tfg.bbdd.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "cabania_caza",
        foreignKeys = @ForeignKey(
                entity = Usuario.class,
                parentColumns = "email",
                childColumns = "usuario",
                onDelete = ForeignKey.CASCADE
        )
)
public class CabaniaCazaEntidad {
    @PrimaryKey
    @NonNull
    private String usuario;

    @ColumnInfo(name = "nivel")
    @NonNull
    private Integer nivel;

    @ColumnInfo(name = "aldeanos_asignados")
    @NonNull
    private Integer aldeanosAsignados;

    @ColumnInfo(name = "desbloqueado")
    @NonNull
    private Boolean desbloqueado;

    @ColumnInfo(name = "segundos_restantes")
    @NonNull
    private Long segundosRestantes;

    @ColumnInfo(name = "aldeanos_muertos_en_partida")
    @NonNull
    private Integer aldeanosMuertosEnPartida;

    @ColumnInfo(name = "partida_activa")
    @NonNull
    private Boolean partidaActiva;
}
