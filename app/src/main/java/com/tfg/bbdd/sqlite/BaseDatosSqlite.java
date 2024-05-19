package com.tfg.bbdd.sqlite;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tfg.bbdd.dao.AldeaDao;
import com.tfg.bbdd.dao.CabaniaCazaDao;
import com.tfg.bbdd.dao.EdificioDao;
import com.tfg.bbdd.dao.RecursosDao;
import com.tfg.bbdd.dao.UsuarioDao;
import com.tfg.bbdd.entidades.Aldea;
import com.tfg.bbdd.entidades.CabaniaCaza;
import com.tfg.bbdd.entidades.Edificio;
import com.tfg.bbdd.entidades.Recursos;
import com.tfg.bbdd.entidades.Usuario;

@Database(
        entities = {Usuario.class, Aldea.class, Recursos.class, Edificio.class, CabaniaCaza.class},
        version = 1
)
public abstract class BaseDatosSqlite extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
    public abstract AldeaDao aldeaDao();
    public abstract RecursosDao recursosDao();
    public abstract EdificioDao edificioDao();
    public abstract CabaniaCazaDao cabaniaCazaDao();
}
