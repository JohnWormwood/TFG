package com.tfg.bbdd.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tfg.bbdd.entidades.EdificioEntidad;

@Dao
public interface EdificioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(EdificioEntidad edificioEntidad);

    @Query("SELECT * FROM edificio WHERE usuario = :usuario AND nombre = :nombre")
    EdificioEntidad getEdificioByUsuarioAndNombre(String usuario, String nombre);
}
