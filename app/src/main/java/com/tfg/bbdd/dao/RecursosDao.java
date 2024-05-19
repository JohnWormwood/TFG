package com.tfg.bbdd.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tfg.bbdd.entidades.Recursos;
import com.tfg.bbdd.entidades.Usuario;

@Dao
public interface RecursosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(Recursos recursos);

    @Query("SELECT * FROM recursos WHERE usuario = :usuario")
    Recursos getRecurosByEmail(String usuario);
}
