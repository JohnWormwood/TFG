package com.tfg.bbdd.sqlite;

import android.content.Context;

import androidx.room.Room;

import com.tfg.bbdd.entidades.Edificio;
import com.tfg.bbdd.entidades.Usuario;
import com.tfg.bbdd.mapper.MapeoEntidades;
import com.tfg.modelos.Aldea;
import com.tfg.utilidades.Constantes;

public class GestorSqlite {
    private BaseDatosSqlite baseDatosSqlite;
    private MapeoEntidades mapeoEntidades;
    private Aldea aldea;
    private String email;

    public GestorSqlite(Context context, String email) {
        baseDatosSqlite = Room.databaseBuilder(context, BaseDatosSqlite.class, "local.db")
                .fallbackToDestructiveMigrationOnDowngrade().allowMainThreadQueries().build();
        mapeoEntidades = new MapeoEntidades();
        aldea = Aldea.getInstance();
        this.email = email;
    }

    public boolean estaSincronizado() {
        return baseDatosSqlite.usuarioDao().getUsuarioByEmail(email).getSincronizadoConFirebase();
    }

    public void guardarDatos() {
        Usuario usuario = new Usuario(email, false);

        baseDatosSqlite.usuarioDao().insertar(usuario);
        baseDatosSqlite.recursosDao().insertar(mapeoEntidades.mapearRecursos(aldea, usuario));
        baseDatosSqlite.aldeaDao().insertar(mapeoEntidades.mapearAldea(aldea, usuario));
        baseDatosSqlite.cabaniaCazaDao().insertar(mapeoEntidades.mapearCabaniaCaza(aldea.getCabaniaCaza(), usuario));
        baseDatosSqlite.edificioDao().insertar(mapeoEntidades.mapearEdificio(
                aldea.getCasetaLeniador(),
                new Edificio.EdificioId(usuario.getEmail(), Constantes.BaseDatos.CASETA_LENIADOR)
        ));
        baseDatosSqlite.edificioDao().insertar(mapeoEntidades.mapearEdificio(
                aldea.getCarpinteria(),
                new Edificio.EdificioId(usuario.getEmail(), Constantes.BaseDatos.CARPINTERIA)
        ));
        baseDatosSqlite.edificioDao().insertar(mapeoEntidades.mapearEdificio(
                aldea.getMina(),
                new Edificio.EdificioId(usuario.getEmail(), Constantes.BaseDatos.MINA)
        ));
        baseDatosSqlite.edificioDao().insertar(mapeoEntidades.mapearEdificio(
                aldea.getGranja(),
                new Edificio.EdificioId(usuario.getEmail(), Constantes.BaseDatos.GRANJA)
        ));
        baseDatosSqlite.edificioDao().insertar(mapeoEntidades.mapearEdificio(
                aldea.getCastillo(),
                new Edificio.EdificioId(usuario.getEmail(), Constantes.BaseDatos.CASTILLO)
        ));
    }

    public void cargarDatos() {
        mapeoEntidades.cargarDatosEnAldea(baseDatosSqlite.aldeaDao().getAldeaByEmail(email), baseDatosSqlite.recursosDao().getRecurosByEmail(email));
        mapeoEntidades.cargarDatosEnCabaniaCaza(aldea.getCabaniaCaza(), baseDatosSqlite.cabaniaCazaDao().getCabaniaCazaByEmail(email));
        mapeoEntidades.cargarDatosEnEdificio(aldea.getCasetaLeniador(), baseDatosSqlite.edificioDao().getEdificioByUsuarioAndNombre(email, Constantes.BaseDatos.CASETA_LENIADOR));
        mapeoEntidades.cargarDatosEnEdificio(aldea.getCarpinteria(), baseDatosSqlite.edificioDao().getEdificioByUsuarioAndNombre(email, Constantes.BaseDatos.CARPINTERIA));
        mapeoEntidades.cargarDatosEnEdificio(aldea.getGranja(), baseDatosSqlite.edificioDao().getEdificioByUsuarioAndNombre(email, Constantes.BaseDatos.GRANJA));
        mapeoEntidades.cargarDatosEnEdificio(aldea.getMina(), baseDatosSqlite.edificioDao().getEdificioByUsuarioAndNombre(email, Constantes.BaseDatos.MINA));
        mapeoEntidades.cargarDatosEnEdificio(aldea.getCastillo(), baseDatosSqlite.edificioDao().getEdificioByUsuarioAndNombre(email, Constantes.BaseDatos.CASTILLO));

        // Actualizar el estado de la sincronizacion
        Usuario usuario = new Usuario(email, true);
        baseDatosSqlite.usuarioDao().insertar(usuario);
    }
}
