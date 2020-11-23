package com.example.proyectoandroid.databases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.model.Personaje;
import com.example.proyectoandroid.model.Transformacion;

import java.util.ArrayList;
import java.util.List;

public class DragonBallSQL extends SQLiteOpenHelper {

    //Creamos una copia a la referencia a la base de datos para poder
    //acceder a ella desde métodos que normalmente no lo permitirían,
    //como métodos existentes que no sobrescriben SQLiteOpenHelper.
    private SQLiteDatabase db;

    //Guardamos el context
    private final Context contexto;

    public int indicePersonajes;
    public int indiceTransformaciones;

    //Constructor
    public DragonBallSQL(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        contexto = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //definimos la sentencia de creación de una tabla
        String sentenciaCreacionPersonajes = "CREATE TABLE personajes (id INTEGRER PRIMARY KEY, nombre TEXT, descripcion TEXT," +
                "raza TEXT, ataqueEspecial TEXT, foto TEXT, fotoCompleta TEXT);";

        //Creamos la tabla transformaciones, que está relacionada con los personajes
        String sentenciaCreacionTranformaciones = "CREATE TABLE transformaciones (id INTEGER PRIMARY KEY, nombre TEXT, foto TEXT, id_personaje INTEGER, FOREIGN KEY(id_personaje) REFERENCES personajes(id))";

        //Ejecutamos las sentencias
        db.execSQL(sentenciaCreacionPersonajes);
        db.execSQL(sentenciaCreacionTranformaciones);

        //Inicializo la base de datos con algunos datos
        String sentenciaInsertPersonajes = "INSERT INTO personajes VALUES" +
                "(1, 'Goku', 'Son Gokū es el protagonista del manga y anime Dragon Ball creado por Akira Toriyama.', 'Saiyan', 'Kamehameha', " + R.drawable.goku + ", " + R.drawable.goku_completa + ")," +
                "(2, 'Gohan', 'Son Gohan es un personaje del manga y anime Dragon Ball creado por Akira Toriyama. Es el primer hijo de Son Gokū y Chi-Chi', 'Humano - Saiyan', 'Masenko', " + R.drawable.gohan + ", " + R.drawable.gohan_completa + ")," +
                "(3, 'Goten', 'Goten es un personaje ficticio de la serie de manga y anime Dragon Ball. Segundo hijo del protagonista, Goku, y Chichi/Milk.', 'Humano - Saiyan', 'Kamehameha', " + R.drawable.goten + ", " + R.drawable.goten_completa + ")," +
                "(4, 'Krilin', 'Krilin es un personaje de la serie de manga y anime Dragon Ball. Es el primer rival en artes marciales de Son Gokū aunque luego se convierte en su mejor amigo.', 'Humano', 'Kienzan', " + R.drawable.krilin + ", " + R.drawable.krilin_completa + ")," +
                "(5, 'Piccolo', 'Piccolo es un personaje de ficción de la serie de manga y anime Dragon Ball. Su padre, Piccolo Daimaō, surgió tras separarse de Kamisama.', 'Namekiano', 'Makankosappo', " + R.drawable.piccolo + ", " + R.drawable.piccolo_completa + ")," +
                "(6, 'Trunks', 'Trunks es un personaje de ficción de la serie de manga y anime Dragon Ball de Akira Toriyama. Hijo de Vegeta y Bulma.', 'Humano - Saiyan', 'Kamehameha', " + R.drawable.trunks + ", " + R.drawable.trunks_completa + ")," +
                "(7, 'Vegeta', 'Vegeta es un personaje ficticio perteneciente a la raza llamada saiyajin, del manga y anime Dragon Ball.', 'Saiyan', 'Final Flash', " + R.drawable.vegeta + ", " + R.drawable.vegeta_completa + ")";

        String sentenciaInsertTransformaciones = "INSERT INTO transformaciones VALUES" +
                "(1, 'Super Saiyan 1', " + R.drawable.goku_ssj1 + ", 1)," +
                "(2, 'Super Saiyan 2', " + R.drawable.goku_ssj2 + ", 1)," +
                "(3, 'Super Saiyan 3', " + R.drawable.goku_ssj3 + ", 1)," +
                "(4, 'Super Saiyan Dios', " + R.drawable.goku_ssjydios + ", 1)," +
                "(5, 'Super Saiyan Blue', " + R.drawable.goku_ssjyblue + ", 1)," +
                "(6, 'Super Saiyan 1 (Niño)', " + R.drawable.gohan_ssj1_peque + ", 2)," +
                "(7, 'Super Saiyan 2 (Niño)', " + R.drawable.gohan_ssj2_peque + ", 2)," +
                "(8, 'Super Saiyan 1 (Adulto)', " + R.drawable.gohan_ssj1 + ", 2)," +
                "(9, 'Super Saiyan 2 (Adulto)', " + R.drawable.gohan_ssj2 + ", 2)," +
                "(10, 'Super Saiyan', " + R.drawable.goten_ssj1 + ", 3)," +
                "(11, 'Super Saiyan (Niño)', " + R.drawable.trunks_ssj1 + ", 6)," +
                "(12, 'Super Saiyan (Adulto)', " + R.drawable.trunks_ssj1_adulto + ", 6)," +
                "(13, 'Super Saiyan 1', " + R.drawable.vegeta_ssj1 + ", 7)," +
                "(14, 'Super Saiyan 2', " + R.drawable.vegeta_ssj2 + ", 7)," +
                "(15, 'Super Saiyan Blue', " + R.drawable.vegeta_ssj_blue + ", 7)";

        db.execSQL(sentenciaInsertPersonajes);
        db.execSQL(sentenciaInsertTransformaciones);

        //copiamos la referencia
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // El proceso normal cuando actualizamos la base de datos es crear una nueva usando
        // los datos de la anterior, para este ejemplo borraremos la anterior.
        String sentenciaDestruccion = "DROP TABLE IF EXISTS personajes";

        //Ejecutamos la sentencia
        db.execSQL(sentenciaDestruccion);

    }


    /**
     * Método para reiniciar la base de datos
     */
    public void reiniciarDb(String nombreDb) {
        this.contexto.deleteDatabase(nombreDb);
    }

    /**
     * Método para obtener un personaje a través de su ID
     */
    public Personaje getPersonaje(int id) {
        db = getWritableDatabase();

        //Definimos el personaje que vamos a devolver
        Personaje personaje = new Personaje();

        //Campos para buscar en la tabla
        String[] CAMPOS = {"*"};

        //Cláusula WHERE para buscar el personaje del que queremos sacar las transformaciones
        String WHERE = "id = " + id;

        //Cursor
        @SuppressLint("Recycle") Cursor cursor = db.query("personajes", CAMPOS, WHERE, null, null, null, null, null);

        //Mientras existan resultados seguiremos recorriendo los resultados de la consulta
        if (cursor.getCount() > 0)
            while (cursor.moveToNext()) {
                //Añadimos los resultados al personaje
                personaje.setId(cursor.getInt(0));
                personaje.setNombre(cursor.getString(1));
                personaje.setDescripcion(cursor.getString(2));
                personaje.setRaza(cursor.getString(3));
                personaje.setAtaqueEspecial(cursor.getString(4));
                //Si recibo un entero en el objeto Object, le asigno el Integer
                if (cursor.getInt(5) != 0) {
                    personaje.setFoto(cursor.getInt(5));
                } else { //Si no, es porque recibo un Uri de la galeria
                    personaje.setFoto(Uri.parse(cursor.getString(5)));
                }
                //Si recibo un entero en el objeto Object, le asigno el Integer
                if (cursor.getInt(6) != 0) {
                    personaje.setFotoCompleta(cursor.getInt(6));
                } else { //Si no, es porque recibo un Uri de la galeria
                    personaje.setFotoCompleta(Uri.parse(cursor.getString(6)));
                }

            }

        // Cerramos la conexión
        db.close();

        // Se devuleven los resultados
        return personaje;
    }

    /**
     * Método para agregar un personaje a la base de datos
     */
    public void insertarPersonaje(Personaje p) {
        //Obtenemos el ultimo indice para insertar el siguiente en la base de datos
        indicePersonajes = obtenerUltimoIndice("personajes");

        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            String sentenciaInsertar = "INSERT INTO personajes VALUES (" + indicePersonajes + ", '" + p.getNombre() + "'," +
                    "'" + p.getDescripcion() + "', '" + p.getRaza() + "', '" + p.getAtaqueEspecial() + "'," +
                    "'" + p.getFoto() + "', '" + p.getFotoCompleta() + "')";

            //Ejecutamos la consulta
            db.execSQL(sentenciaInsertar);

            //Cerramos la conexión
            db.close();
        }
    }

    /**
     * Método para borrar un personaje de la base de datos
     */
    public void borrarPersonaje(Personaje p) {
        db = getWritableDatabase();

        //Sentencia de borrado
        String sentenciaBorrado = "DELETE FROM personajes WHERE (id='" + p.getId() + "');";

        //Ejecutamos la consulta
        db.execSQL(sentenciaBorrado);

        // Cerramos la conexión
        db.close();
    }

    /**
     * Método para obtener el listado de personajes
     */
    public List<Personaje> getListadoPersonajes() {
        db = getWritableDatabase();

        //Definimos un array para almacenar los resultados
        List<Personaje> resultadoConsulta = new ArrayList<>();

        //Campos para buscar en la tabla
        String[] CAMPOS = {"id", "nombre", "descripcion", "raza", "ataqueEspecial", "foto", "fotoCompleta"};

        //Cursor
        @SuppressLint("Recycle") Cursor cursor = db.query("personajes", CAMPOS, null, null, null, null, null, null);

        //Mientras existan resultados seguiremos recorriendo los resultados de la consulta
        if (cursor.getCount() > 0)
            while (cursor.moveToNext()) {
                //Se inicia el array temporal
                Personaje p = new Personaje();

                //Añadimos los resultados al personaje
                p.setId(cursor.getInt(0));
                p.setNombre(cursor.getString(1));
                p.setDescripcion(cursor.getString(2));
                p.setRaza(cursor.getString(3));
                p.setAtaqueEspecial(cursor.getString(4));

                //Si recibo un entero del Object, le asigno el Integer
                if (cursor.getInt(5) != 0) {
                    p.setFoto(cursor.getInt(5));
                } else { //Si recibo un Uri porque es de la galería, le asigno el Uri
                    p.setFoto(Uri.parse(cursor.getString(5)));
                }

                //Si recibo un entero del Object, le asigno el Integer
                if (cursor.getInt(6) != 0) {
                    p.setFotoCompleta(cursor.getInt(6));
                } else { //Si recibo un Uri porque es de la galería, le asigno el Uri
                    p.setFotoCompleta(Uri.parse(cursor.getString(6)));
                }

                //cargamos el array principal con el resultado acual
                resultadoConsulta.add(p);
            }

        // Cerramos la conexión
        db.close();

        // Se devuleven los resultados
        return resultadoConsulta;
    }

    /**
     * Método para editar un personaje en la base de datos
     */
    public void editarPersonaje(Personaje p) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            String sentenciaEditar = "UPDATE personajes SET nombre = '" + p.getNombre() + "', descripcion = " +
                    "'" + p.getDescripcion() + "', raza = '" + p.getRaza() + "" + "', ataqueEspecial = '" +
                    "" + p.getAtaqueEspecial() + "', foto = '" +
                    "" + p.getFoto() + "', fotoCompleta = '" + p.getFotoCompleta() + "' WHERE id = " + p.getId() + ";";

            //Ejecutamos la consulta
            db.execSQL(sentenciaEditar);

            //Cerramos la conexión
            db.close();
        }
    }

    /**
     * Método para obtener el último índice de una tabla para luego asignarlo en una creación nueva
     */
    public int obtenerUltimoIndice(String nombreTabla) {
        db = this.getWritableDatabase();
        int indiceDevuelto = -1;
        @SuppressLint("Recycle") Cursor cursor = db.query(nombreTabla, null, "id=(SELECT id FROM " + nombreTabla +
                " ORDER BY id DESC LIMIT 1)", null, null, null, null);
        if (cursor.getCount() > 0)
            while (cursor.moveToNext()) {
                indiceDevuelto = cursor.getInt(0);
            }
        db.close();


        // Devolvemos los resultados
        return indiceDevuelto + 1;
    }

    /**
     * Método para insertar transformaciones a un personaje
     */
    public void insertarTransformacion(Personaje personaje, Transformacion transformacion) {
        //Obtenemos el ultimo indice para insertar el siguiente en la base de datos
        indiceTransformaciones = obtenerUltimoIndice("transformaciones");

        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            String sentenciaInsertar = "INSERT INTO transformaciones VALUES (" + indiceTransformaciones + "," +
                    " '" + transformacion.getNombre() + "', '" + transformacion.getFoto() + "', " + personaje.getId() + ")";

            //Ejecutamos la consulta
            db.execSQL(sentenciaInsertar);

            //Cerramos la conexión
            db.close();
        }
    }

    /**
     * Método para obtener el listado de transformaciones de un personaje
     */
    public List<Transformacion> getListadoTransformaciones(Personaje personaje) {
        db = getWritableDatabase();

        //Definimos un array para almacenar los resultados
        List<Transformacion> resultadoConsulta = new ArrayList<>();

        //Campos para buscar en la tabla
        String[] CAMPOS = {"*"};

        //Cláusula WHERE para buscar el personaje del que queremos sacar las transformaciones
        String WHERE = "id_personaje = " + personaje.getId();

        //Cursor
        @SuppressLint("Recycle") Cursor cursor = db.query("transformaciones", CAMPOS, WHERE, null, null, null, null, null);

        //Mientras existan resultados seguiremos recorriendo los resultados de la consulta
        if (cursor.getCount() > 0)
            while (cursor.moveToNext()) {
                //Se inicia el array temporal
                Transformacion t = new Transformacion();

                //Añadimos los resultados al personaje
                t.setId(cursor.getInt(0));
                t.setNombre(cursor.getString(1));
                //Si recibo un entero en el objeto Object, le asigno el Integer
                if (cursor.getInt(2) != 0) {
                    t.setFoto(cursor.getInt(2));
                } else { //Si no, es porque recibo un Uri de la galeria
                    t.setFoto(Uri.parse(cursor.getString(2)));
                }
                t.setId_usuario(cursor.getInt(3));

                //cargamos el array principal con el resultado acual
                resultadoConsulta.add(t);
            }

        // Cerramos la conexión
        db.close();

        // Se devuleven los resultados
        return resultadoConsulta;
    }

    /**
     * Método para borrar una transformacion de la base de datos
     */
    public void borrarTransformacion(Transformacion transformacion) {
        db = getWritableDatabase();

        //Sentencia de borrado
        String sentenciaBorrado = "DELETE FROM transformaciones WHERE (id='" + transformacion.getId() + "');";

        //Ejecutamos la consulta
        db.execSQL(sentenciaBorrado);

        // Cerramos la conexión
        db.close();
    }

    /**
     * Método para editar una transformacion en la base de datos
     */
    public void editarTransformacion(Transformacion transformacion) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            String sentenciaEditar = "UPDATE transformaciones SET nombre = '" + transformacion.getNombre() +
                    "', foto = '" + transformacion.getFoto() + "' WHERE id = " + transformacion.getId() + ";";

            //Ejecutamos la consulta
            db.execSQL(sentenciaEditar);

            //Cerramos la conexión
            db.close();
        }
    }

    /**
     * Método para editar la foto completa
     */
    public void editarFotoCompleta(Uri path, Personaje personaje) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            String sentenciaEditar = "UPDATE personajes SET fotoCompleta = '" + path + "' WHERE " +
                    "id = " +  personaje.getId() + ";";

            //Ejecutamos la consulta
            db.execSQL(sentenciaEditar);

            //Cerramos la conexión
            db.close();
        }
    }
}
