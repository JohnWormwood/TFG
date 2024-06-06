package com.tfg.bbdd.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tfg.bbdd.entidades.AldeaEntidad;

@Dao
public interface AldeaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(AldeaEntidad aldeaEntidad);

    @Query("SELECT * FROM datos_aldea WHERE usuario = :usuario")
    AldeaEntidad getAldeaByEmail(String usuario);
}
