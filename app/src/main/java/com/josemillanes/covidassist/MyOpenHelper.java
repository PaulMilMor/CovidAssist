package com.josemillanes.covidassist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class MyOpenHelper  extends SQLiteOpenHelper {

    private static final String USERS_TABLE_CREATE = "CREATE TABLE usuarios (usuario_id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_nombre TEXT, usuario_correo TEXT, usuario_contraseña TEXT, usuario_img BLOB )";
    private static final String EVENTS_TABLE_CREATE = "CREATE TABLE eventos (evento_id INTEGER PRIMARY KEY AUTOINCREMENT, evento_titulo TEXT, evento_descripcion TEXT, evento_lugar TEXT, evento_fecha INTEGER, evento_status STRING, evento_maxcap INTEGER, evento_creador INTEGER, evento_contagio INTEGER)";
    private static final String ASSISTANCE_TABLE_CREATE = "CREATE TABLE asistencia (usuario_id INTEGER, evento_id INTEGER, FOREIGN KEY(usuario_id) REFERENCES usuarios(usuario_id), FOREIGN KEY(evento_id) REFERENCES eventos(evento_id))";

    private static final String EVENTS_TABLE_INSERT = "INSERT INTO eventos(evento_titulo, evento_descripcion, evento_lugar, evento_fecha, evento_status, evento_maxcap, evento_creador, evento_contagio) VALUES('Posada','Oficina',1637481546004, 'Planeado',30,1,0)";

    private static final String DB_NAME = "eventos.sqlite";
    private static final int DB_VERSION = 1;
    private static SQLiteDatabase db;

   // private SQLiteDatabase db;
    private Context myContext;
    public static  final String IMAGE = "usuario_img";
    private DatabaseHelper DbHelper;

    static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DB_NAME,null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(USERS_TABLE_CREATE);
            db.execSQL(EVENTS_TABLE_CREATE);
            db.execSQL(ASSISTANCE_TABLE_CREATE);
            // db.execSQL(EVENTS_TABLE_INSERT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        public int[] login(String u, String p){
            int a=0;
            int id = 0;
            int[] results = new int[2];
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            Cursor cr = db.rawQuery("select usuario_id, usuario_correo, usuario_contraseña from usuarios", null);
            if (cr!=null && cr.moveToFirst()){
                do{
                    if (cr.getString(1).equals(u) && cr.getString(2).equals(p)){
                        id = cr.getInt(0);
                        a++;
                    }
                } while (cr.moveToNext());
            }
            results[0] = a;
            results[1] = id;
            return results;
        }
    }

    public  MyOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
        myContext = context;
        DbHelper = new DatabaseHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERS_TABLE_CREATE);
        db.execSQL(EVENTS_TABLE_CREATE);
        db.execSQL(ASSISTANCE_TABLE_CREATE);
        // db.execSQL(EVENTS_TABLE_INSERT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    //Métodos para insertar, actualizar y eliminar usuarios y eventos
    //Para leer las tablas hay distintos casos así que se crearán los métodos sobre la marcha

    public void updateUsuario(int id, String nombre, String correo, String contra) {
        ContentValues cv = new ContentValues();

        cv.put("usuario_id", id);
        cv.put("usuario_nombre",nombre);
        cv.put("usuario_correo",correo);
        cv.put("usuario_contraseña",contra);

        db.update("usuarios", cv, "usuario_id= "+id, null);
    }

    public void deleteUsuario(int id) {
        String[] args = new String[]{String.valueOf(id)};
        db.delete("usuarios","usuario_id=?",args);
    }



    public void insertEvento(Evento evento) {
        ContentValues cv = new ContentValues();
        cv.put("evento_titulo",evento.getEventTitle());
        cv.put("evento_descripcion", evento.getEventDescription());
        cv.put("evento_lugar",evento.getEventPlace());
        cv.put("evento_fecha",evento.getEventDate().getTime());
        cv.put("evento_status",evento.getEventStatus());
        cv.put("evento_maxcap",evento.getEventCapacity());
        cv.put("evento_creador",evento.getEventCreator());
        cv.put("evento_contagio",0);
        db.insert("eventos",null,cv);
    }

    public void updateEvento(Evento evento) {
        ContentValues cv = new ContentValues();
        cv.put("evento_id",evento.getEventId());
        cv.put("evento_titulo",evento.getEventTitle());
        cv.put("evento_descripcion", evento.getEventDescription());
        cv.put("evento_lugar",evento.getEventPlace());
        cv.put("evento_fecha",evento.getEventDate().getTime());
        cv.put("evento_status",evento.getEventStatus());
        cv.put("evento_maxcap",evento.getEventCapacity());
        cv.put("evento_creador",evento.getEventCreator());
        cv.put("evento_contagio",evento.isEventContagio()?1:0);
        String whereClause = "evento_id=?";
        String whereArgs[] = {""+evento.getEventId()};
        db.update("eventos", cv, whereClause, whereArgs);
    }

    public void deleteEvento(int id) {
        String[] args = new String[]{String.valueOf(id)};
        db.delete("eventos","evento_id=?",args);
    }


    public ArrayList<Evento> getEventos() {
        ArrayList<Evento> eventos = new ArrayList<>();
        Cursor c = db.rawQuery("select evento_id, evento_titulo, evento_descripcion, evento_lugar, evento_fecha, evento_status, evento_maxcap, evento_creador, evento_contagio from eventos order by evento_fecha ASC",null);
        if(c != null && c.getCount() > 0) {
            c.moveToFirst();
            Log.d("DEBUGGEATE",c.toString());
            do {
              /*  Log.d("DEBUGGEATE",c.toString());
                int id = c.getInt(0);
                Log.d("DASDAS", String.valueOf(id));*/
                Evento evento = new Evento(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        new Date(c.getLong(4)),
                        c.getString(5),
                        c.getInt(6),
                        c.getInt(7),

                        c.getInt(8) == 1
                );
                evento.setEventAttendance(getAsistencia(evento.getEventId()));
                eventos.add(evento);
            } while(c.moveToNext());
        }
        c.close();
        return eventos;
    }

    public int getAsistencia(int evento_id) {
        int asistencia = 0;
        Cursor c = db.rawQuery("select count(usuario_id) from asistencia where evento_id = "+evento_id, null);
        if(c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                asistencia += c.getInt(0);
            } while(c.moveToNext());
        }
        return asistencia;
    }

    public boolean hasAsistencia(int evento_id, int usuario_id) {
        int count = 0;
        Cursor c = db.rawQuery("select exists(select usuario_id from asistencia where usuario_id ="+usuario_id+" and evento_id = "+evento_id+")",null);
        if(c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                count += c.getInt(0);
            } while(c.moveToNext());
        }
        return count==1;
    }

    public ArrayList<Evento> getMyEventos(int id) {
        ArrayList<Evento> eventos = new ArrayList<>();
        Cursor c = db.rawQuery("select evento_id, evento_titulo, evento_descripcion, evento_lugar, evento_fecha, evento_status, evento_maxcap, evento_creador, evento_contagio from eventos  where evento_creador="+id+" order by evento_fecha ASC",null);
        if(c != null && c.getCount() > 0) {
            c.moveToFirst();
            Log.d("DEBUGGEATE",c.toString());
            do {
              /*  Log.d("DEBUGGEATE",c.toString());
                int id = c.getInt(0);
                Log.d("DASDAS", String.valueOf(id));*/
                Evento evento = new Evento(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        new Date(c.getLong(4)),
                        c.getString(5),
                        c.getInt(6),
                        c.getInt(7),

                        c.getInt(8) == 1
                );
                evento.setEventAttendance(getAsistencia(evento.getEventId()));
                eventos.add(evento);
            } while(c.moveToNext());
        }
        c.close();
        return eventos;
    }

    public ArrayList<Evento> getHistoryEventos(int usuarioId) {
        ArrayList<Evento> eventos = new ArrayList<>();
        Cursor c = db.rawQuery("select * from eventos as e inner join asistencia as a where e.evento_id = a.evento_id and a.usuario_id="+usuarioId+" order by evento_fecha ASC",null);
        if(c != null && c.getCount() > 0) {
            c.moveToFirst();
            Log.d("DEBUGGEATE",c.toString());
            do {
              /*  Log.d("DEBUGGEATE",c.toString());
                int id = c.getInt(0);
                Log.d("DASDAS", String.valueOf(id));*/
                Evento evento = new Evento(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        new Date(c.getLong(4)),
                        c.getString(5),
                        c.getInt(6),
                        c.getInt(7),

                        c.getInt(8) == 1
                );
                evento.setEventAttendance(getAsistencia(evento.getEventId()));
                eventos.add(evento);
            } while(c.moveToNext());
        }
        c.close();
        return eventos;
    }

    public void marcarAsistencia(int usuario_id, int evento_id) {
        ContentValues cv = new ContentValues();
        cv.put("usuario_id",usuario_id);
        cv.put("evento_id",evento_id);

        db.insert("asistencia",null,cv);
    }
    public void removerAsistencia(int usuario_id, int evento_id) {
        String[] args = new String[]{String.valueOf(usuario_id),String.valueOf(evento_id)};
        db.delete("asistencia","usuario_id=? and evento_id=?",args);
    }


    public MyOpenHelper open() throws SQLException {
        db = DbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        DbHelper.close();
    }



    public void insertUsuario(String nombre, String correo, String contra){
        ContentValues cv = new ContentValues();
        cv.put("usuario_nombre",nombre);
        cv.put("usuario_correo",correo);
        cv.put("usuario_contraseña",contra);
        cv.put("usuario_img", "a");
        db.insert("usuarios",null,cv);
    }

    public byte[] recuperarImagen(){
        Cursor cur = db.query(true,"usuarios", new String[]{IMAGE,},
                null,null,null,null,"id"+" DESC","1");

        if (cur.moveToFirst()){
            byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }

    public static Usuario verUsuario(int id){

        Usuario usuario = null;
        Cursor cursor;

        cursor = db.rawQuery("SELECT usuario_nombre, usuario_correo, usuario_contraseña FROM "+ "usuarios" + " WHERE usuario_id = "+ id , null);
        if (cursor.moveToFirst()){
            usuario = new Usuario();
            usuario.setUserName(cursor.getString(0));
            usuario.setUserEmail(cursor.getString(1));
            usuario.setUserPassword(cursor.getString(2));
        }

        cursor.close();
        return usuario;
    }

}
