package com.tfg.bbdd.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tfg.bbdd.entidades.Usuario;

@Dao
public interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE email = :email")
    Usuario getUsuarioByEmail(String email);
}
