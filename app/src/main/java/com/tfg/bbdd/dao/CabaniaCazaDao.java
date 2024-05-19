package com.tfg.bbdd.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tfg.bbdd.entidades.CabaniaCaza;
import com.tfg.bbdd.entidades.Usuario;

@Dao
public interface CabaniaCazaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(CabaniaCaza cabaniaCaza);

    @Query("SELECT * FROM cabania_caza WHERE usuario = :usuario")
    CabaniaCaza getCabaniaCazaByEmail(String usuario);
}
