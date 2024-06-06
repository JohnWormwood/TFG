package com.tfg.bbdd.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tfg.bbdd.entidades.CabaniaCazaEntidad;

@Dao
public interface CabaniaCazaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(CabaniaCazaEntidad cabaniaCazaEntidad);

    @Query("SELECT * FROM cabania_caza WHERE usuario = :usuario")
    CabaniaCazaEntidad getCabaniaCazaByEmail(String usuario);
}
