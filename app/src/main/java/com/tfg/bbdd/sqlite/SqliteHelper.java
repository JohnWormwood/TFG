package com.tfg.bbdd.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tfg.bbdd.dto.AldeaDTO;
import com.tfg.bbdd.dto.CabaniaCazaDTO;
import com.tfg.bbdd.dto.EdificioDTO;
import com.tfg.bbdd.dto.RecursosDTO;
import com.tfg.utilidades.Constantes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final String BASE_DATOS = "local.db";
    private static final int VERSION = 1;
    private static final String SCRIPT = "local.sql";

    private Context context;

    public SqliteHelper(Context context) {
        super(context, BASE_DATOS, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ejecutarScript(db, SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    private void ejecutarScript(SQLiteDatabase db, String script) {
         try {
            InputStream inputStream = context.getAssets().open(script);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                stringBuilder.append(linea);
                stringBuilder.append("\n");
            }
            bufferedReader.close();
            String[] sentenciasSql = stringBuilder.toString().split(";");
            for (String sentencia : sentenciasSql) {
                sentencia = sentencia.trim();
                if (!sentencia.isEmpty()) {
                    db.execSQL(sentencia);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void insertar(String tabla, ContentValues contentValues) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(tabla, null, contentValues);
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    private void consultar() {

    }

    public void insertarRecursos(RecursosDTO recursosDTO) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("troncos", recursosDTO.getTroncos());
        contentValues.put("tablones" , recursosDTO.getTablones());
        contentValues.put("comida", recursosDTO.getComida());
        contentValues.put("piedra", recursosDTO.getPiedra());
        contentValues.put("hierro", recursosDTO.getHierro());
        contentValues.put("oro", recursosDTO.getOro());

        insertar(Constantes.BaseDatos.RECURSOS, contentValues);
    }

    public void insertarDatosAldea(AldeaDTO aldeaDTO) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nivel", aldeaDTO.getNivel());
        contentValues.put("poblacion", aldeaDTO.getPoblacion());
        contentValues.put("defensas", aldeaDTO.getDefensas());

        insertar(Constantes.BaseDatos.ALDEA, contentValues);
    }

    public void insertarEdificio(EdificioDTO edificioDTO, String nombre) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", nombre);
        contentValues.put("nivel", edificioDTO.getNivel());
        contentValues.put("aldeanos_asignados", edificioDTO.getAldeanosAsignados());
        contentValues.put("desbloqueado", edificioDTO.isDesbloqueado());

        insertar(Constantes.BaseDatos.EDIFICIO, contentValues);
    }

    public void insertarCabaniaCaza(CabaniaCazaDTO cabaniaCazaDTO) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nivel", cabaniaCazaDTO.getNivel());
        contentValues.put("aldeanos_asignados", cabaniaCazaDTO.getAldeanosAsignados());
        contentValues.put("desbloqueado", cabaniaCazaDTO.isDesbloqueado());
        contentValues.put("aldeanos_muertos_en_partida", cabaniaCazaDTO.getAldeanosMuertosEnPartida());
        contentValues.put("partida_activa", cabaniaCazaDTO.isPartidaActiva());
        contentValues.put("segundos_restantes", cabaniaCazaDTO.getSegundosRestantes());

        insertar(Constantes.BaseDatos.CABANIA_CAZA, contentValues);
    }

    public RecursosDTO obtenerRecursos() {
        RecursosDTO recursosDTO = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnas = {"troncos", "tablones", "comida", "piedra", "hierro", "oro"};
        Cursor cursor = db.query(Constantes.BaseDatos.RECURSOS, columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int troncos = cursor.getInt(cursor.getColumnIndexOrThrow("troncos"));
            int tablones = cursor.getInt(cursor.getColumnIndexOrThrow("tablones"));
            int comida = cursor.getInt(cursor.getColumnIndexOrThrow("comida"));
            int piedra = cursor.getInt(cursor.getColumnIndexOrThrow("piedra"));
            int hierro = cursor.getInt(cursor.getColumnIndexOrThrow("hierro"));
            int oro = cursor.getInt(cursor.getColumnIndexOrThrow("oro"));

            recursosDTO = new RecursosDTO();
            recursosDTO.setTroncos(troncos);
            recursosDTO.setTablones(tablones);
            recursosDTO.setComida(comida);
            recursosDTO.setPiedra(piedra);
            recursosDTO.setHierro(hierro);
            recursosDTO.setOro(oro);
        }

        cursor.close();
        db.close();

        return recursosDTO;
    }

    public AldeaDTO obtenerAldea() {
        AldeaDTO aldeaDTO = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnas = {"nivel", "poblacion", "defensas"};
        Cursor cursor = db.query(Constantes.BaseDatos.ALDEA, columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int nivel = cursor.getInt(cursor.getColumnIndexOrThrow("nivel"));
            int poblacion = cursor.getInt(cursor.getColumnIndexOrThrow("poblacion"));
            int defensas = cursor.getInt(cursor.getColumnIndexOrThrow("defensas"));

            aldeaDTO = new AldeaDTO();
            aldeaDTO.setNivel(nivel);
            aldeaDTO.setPoblacion(poblacion);
            aldeaDTO.setDefensas(defensas);
        }

        cursor.close();
        db.close();

        return aldeaDTO;
    }

    public EdificioDTO obtenerEdificio(String nombre) {
        EdificioDTO edificioDTO = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnas = {"nivel", "aldeanos_asignados", "desbloqueado"};
        String seleccion = "nombre = ?";
        String[] argumentosSeleccion = {nombre};

        Cursor cursor = db.query(Constantes.BaseDatos.EDIFICIO, columnas, seleccion, argumentosSeleccion, null, null, null);

        if (cursor.moveToFirst()) {
            int nivel = cursor.getInt(cursor.getColumnIndexOrThrow("nivel"));
            int aldeanosAsignados = cursor.getInt(cursor.getColumnIndexOrThrow("aldeanos_asignados"));
            int desbloqueado = cursor.getInt(cursor.getColumnIndexOrThrow("desbloqueado"));

            edificioDTO = new EdificioDTO();
            edificioDTO.setNivel(nivel);
            edificioDTO.setAldeanosAsignados(aldeanosAsignados);
            edificioDTO.setDesbloqueado(desbloqueado != 0);
        }

        cursor.close();
        db.close();

        return edificioDTO;
    }

    public CabaniaCazaDTO obtenerCabaniaCaza() {
        CabaniaCazaDTO cabaniaCazaDTO = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnas = {"nivel", "aldeanos_asignados", "desbloqueado", "aldeanos_muertos_en_partida", "partida_activa", "segundos_restantes"};
        Cursor cursor = db.query(Constantes.BaseDatos.CABANIA_CAZA, columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int nivel = cursor.getInt(cursor.getColumnIndexOrThrow("nivel"));
            int aldeanosAsignados = cursor.getInt(cursor.getColumnIndexOrThrow("aldeanos_asignados"));
            int desbloqueado = cursor.getInt(cursor.getColumnIndexOrThrow("desbloqueado"));
            int aldeanosMuertosEnPartida = cursor.getInt(cursor.getColumnIndexOrThrow("aldeanos_muertos_en_partida"));
            int partidaActiva = cursor.getInt(cursor.getColumnIndexOrThrow("partida_activa"));
            int segundosRestantes = cursor.getInt(cursor.getColumnIndexOrThrow("segundos_restantes"));

            cabaniaCazaDTO = new CabaniaCazaDTO();
            cabaniaCazaDTO.setNivel(nivel);
            cabaniaCazaDTO.setAldeanosAsignados(aldeanosAsignados);
            cabaniaCazaDTO.setDesbloqueado(desbloqueado != 0);
            cabaniaCazaDTO.setAldeanosMuertosEnPartida(aldeanosMuertosEnPartida);
            cabaniaCazaDTO.setPartidaActiva(partidaActiva != 0);
            cabaniaCazaDTO.setSegundosRestantes(segundosRestantes);
        }

        cursor.close();
        db.close();

        return cabaniaCazaDTO;
    }
}
