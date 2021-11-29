package com.josemillanes.covidassist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class MyOpenHelper extends SQLiteOpenHelper {

    private static final String USERS_TABLE_CREATE = "CREATE TABLE usuarios (usuario_id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_nombre TEXT, usuario_correo TEXT, usuario_contraseña TEXT, usuario_img BLOB )";
    private static final String EVENTS_TABLE_CREATE = "CREATE TABLE eventos (evento_id INTEGER PRIMARY KEY AUTOINCREMENT, evento_titulo TEXT, evento_lugar TEXT, evento_fecha INTEGER, evento_status STRING, evento_maxcap INTEGER, evento_creador INTEGER, evento_contagio INTEGER)";
    private static final String ASSISTANCE_TABLE_CREATE = "CREATE TABLE asistencia (usuario_id INTEGER, evento_id INTEGER, FOREIGN KEY(usuario_id) REFERENCES usuarios(usuario_id), FOREIGN KEY(evento_id) REFERENCES eventos(evento_id))";

    private static final String EVENTS_TABLE_INSERT = "INSERT INTO eventos(evento_titulo, evento_lugar, evento_fecha, evento_status, evento_maxcap, evento_creador, evento_contagio) VALUES('Posada','Oficina',1637481546004, 'Planeado',30,1,0)";

    private static final String DB_NAME = "eventos.sqlite";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase db;
    private Context myContext;

    public MyOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
       // db.execSQL(EVENTS_TABLE_INSERT);
        myContext = context;
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

    public void insertUsuario(Usuario usuario) {
        ContentValues cv = new ContentValues();
        cv.put("usuario_nombre",usuario.getUserName());
        cv.put("usuario_correo",usuario.getUserEmail());
        //Nota: tal vez sea necesario cambiar el tipo de useriImg
        cv.put("usuario_img", usuario.getUserImg());
        db.insert("usuarios",null,cv);
    }

    public void updateUsuario(Usuario usuario) {
        ContentValues cv = new ContentValues();
        cv.put("usuario_id", usuario.getUserId());
        cv.put("usuario_nombre",usuario.getUserName());
        cv.put("usuario_correo",usuario.getUserEmail());
        //Nota: tal vez sea necesario cambiar el tipo de useriImg
        cv.put("usuario_img", usuario.getUserImg());
        String whereClause = "usuario_id=?";
        String whereArgs[] = {""+usuario.getUserId()};
        db.update("usuarios", cv, whereClause, whereArgs);
    }

    public void deleteUsuario(int id) {
        String[] args = new String[]{String.valueOf(id)};
        db.delete("usuarios","usuario_id=?",args);
    }



    public void insertEvento(Evento evento) {
        ContentValues cv = new ContentValues();
        cv.put("evento_titulo",evento.getEventTitle());
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
        Cursor c = db.rawQuery("select evento_id, evento_titulo, evento_lugar, evento_fecha, evento_status, evento_maxcap, evento_creador, evento_contagio from eventos",null);
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
                        c.getString(1),
                        c.getString(2),
                        new Date(c.getLong(3)),
                        c.getString(4),
                        c.getInt(5),
                        c.getInt(6),
                        c.getInt(7) == 1
                );
                eventos.add(evento);
            } while(c.moveToNext());
        }
        c.close();
        return eventos;
    }



}
