package com.tfg.bbdd.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "edificio",
        foreignKeys = @ForeignKey(
                entity = Usuario.class,
                parentColumns = "email",
                childColumns = "usuario",
                onDelete = ForeignKey.CASCADE
        )
)
public class EdificioEntidad {
    @Embedded
    @PrimaryKey
    @NonNull
    private EdificioId edificioId;

    @ColumnInfo(name = "nivel")
    @NonNull
    private Integer nivel;

    @ColumnInfo(name = "aldeanos_asignados")
    @NonNull
    private Integer aldeanosAsignados;

    @ColumnInfo(name = "desbloqueado")
    @NonNull
    private Boolean desbloqueado;

    public static class EdificioId {

        @ColumnInfo(name = "usuario")
        @NonNull
        public String usuario;

        @ColumnInfo(name = "nombre")
        @NonNull
        public String nombre;

        public EdificioId(@NonNull String usuario, @NonNull String nombre) {
            this.usuario = usuario;
            this.nombre = nombre;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EdificioId that = (EdificioId) o;

            return (this.usuario.equals(that.usuario) && this.nombre.equals(that.nombre));
        }

        @Override
        public int hashCode() {
            return usuario.hashCode() + nombre.hashCode();
        }
    }
}

