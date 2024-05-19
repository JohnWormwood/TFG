package com.tfg.bbdd.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tfg.bbdd.entidades.Edificio;
import com.tfg.bbdd.entidades.Usuario;

@Dao
public interface EdificioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(Edificio edificio);

    @Query("SELECT * FROM edificio WHERE usuario = :usuario AND nombre = :nombre")
    Edificio getEdificioByUsuarioAndNombre(String usuario, String nombre);
}
