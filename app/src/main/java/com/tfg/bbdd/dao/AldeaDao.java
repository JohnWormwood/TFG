package com.tfg.bbdd.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tfg.bbdd.entidades.Aldea;
import com.tfg.bbdd.entidades.Usuario;

@Dao
public interface AldeaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(Aldea aldea);

    @Query("SELECT * FROM datos_aldea WHERE usuario = :usuario")
    Aldea getAldeaByEmail(String usuario);
}
