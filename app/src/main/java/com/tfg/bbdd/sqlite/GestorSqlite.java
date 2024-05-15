package com.tfg.bbdd.sqlite;

import android.content.Context;

import com.tfg.bbdd.mapper.MapeoDatos;
import com.tfg.modelos.Aldea;
import com.tfg.utilidades.Constantes;

public class GestorSqlite {

    private MapeoDatos mapeoDatos;
    private Context context;
    private Aldea aldea = Aldea.getInstance();

    public GestorSqlite(Context context) {
        this.context = context;
        mapeoDatos = new MapeoDatos();
    }

    public void guardarDatos() {
        try (SqliteHelper sqliteHelper = new SqliteHelper(context)) {
            sqliteHelper.insertarRecursos(mapeoDatos.mapearRecursos(aldea));
            sqliteHelper.insertarDatosAldea(mapeoDatos.mapearAldea(aldea));
            sqliteHelper.insertarCabaniaCaza(mapeoDatos.mapearCabaniaCaza(aldea.getCabaniaCaza()));
            sqliteHelper.insertarEdificio(mapeoDatos.mapearEdificio(aldea.getCasetaLeniador()), Constantes.BaseDatos.CASETA_LENIADOR);
            sqliteHelper.insertarEdificio(mapeoDatos.mapearEdificio(aldea.getCarpinteria()), Constantes.BaseDatos.CARPINTERIA);
            sqliteHelper.insertarEdificio(mapeoDatos.mapearEdificio(aldea.getGranja()), Constantes.BaseDatos.GRANJA);
            sqliteHelper.insertarEdificio(mapeoDatos.mapearEdificio(aldea.getMina()), Constantes.BaseDatos.MINA);
        }
    }

    public void cargarDatos() {
        try (SqliteHelper sqliteHelper = new SqliteHelper(context)) {
            // Aldea
            mapeoDatos.cargarDatosEnAldea(sqliteHelper.obtenerAldea(), sqliteHelper.obtenerRecursos());
            // Cabania Caza
            mapeoDatos.cargarDatosEnCabaniaCaza(aldea.getCabaniaCaza(), sqliteHelper.obtenerCabaniaCaza());
            // Caseta Leniador
            mapeoDatos.cargarDatosEnEdificio(aldea.getCasetaLeniador(), sqliteHelper.obtenerEdificio(Constantes.BaseDatos.CASETA_LENIADOR));
            // Carpinteria
            mapeoDatos.cargarDatosEnEdificio(aldea.getCarpinteria(), sqliteHelper.obtenerEdificio(Constantes.BaseDatos.CARPINTERIA));
            // Granja
            mapeoDatos.cargarDatosEnEdificio(aldea.getGranja(), sqliteHelper.obtenerEdificio(Constantes.BaseDatos.GRANJA));
            // Mina
            mapeoDatos.cargarDatosEnEdificio(aldea.getMina(), sqliteHelper.obtenerEdificio(Constantes.BaseDatos.MINA));
        }
    }
}
