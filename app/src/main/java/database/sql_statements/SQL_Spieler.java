package database.sql_statements;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import database.data.Mannschaft;
import database.data.Position;
import database.data.Spieler;
import database.data.Statistik;

/**
 * Hier sind alle notwendigen SQL-Statements gesammelt, die für die Spieler notwenidig sind
 */
public class SQL_Spieler {

    private final String TABLE_SPIELER = "spieler";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_MANNSCHAFTSID = "_mannschaftsid";
    private final String COLUMN_VORNAME = "vorname";
    private final String COLUMN_NACHNAME = "nachname";
    private final String COLUMN_TRIKONUMMER = "trikonummer";
    private final String COLUMN_GROESSE = "groesse";
    private final String COLUMN_POSITION = "position";
    private final String COLUMN_FOTO = "foto";
    private final String COLUMN_SELECTED = "selected";
    private final String COLUMN_SPORTART = "sportart";

    //Datenbankzugriff mit Schreibrechten
    private SQLiteDatabase dbW;
    //Datenbankzugriff mit Leserechten
    private SQLiteDatabase dbR;

    public SQL_Spieler(SQLiteOpenHelper db){
        this.dbW = db.getWritableDatabase();
        this.dbR = db.getReadableDatabase();
    }

    public SQL_Spieler(SQLiteDatabase dbW){
        this.dbW = dbW;
    }

    /**
     * Erzeugt die Spieler-Tabelle in der Datenbank
     */
    public void createTable(){

        String CREATE_SPIELER_TABLE = "CREATE TABLE " +
                TABLE_SPIELER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_MANNSCHAFTSID + " INTEGER,"
                + COLUMN_VORNAME + " TEXT,"
                + COLUMN_NACHNAME + " TEXT,"
                + COLUMN_TRIKONUMMER + " INTEGER,"
                + COLUMN_GROESSE + " INTEGER,"
                + COLUMN_POSITION + " BLOB,"
                + COLUMN_FOTO + " BLOB,"
                + COLUMN_SELECTED + " INTEGER,"
                + COLUMN_SPORTART + " TEXT"
                + ")";

        dbW.execSQL(CREATE_SPIELER_TABLE);
    }

    /**
     * Löscht die Spieler-Tabelle in der Datenbank
     */
    public void deleteTable(){
        dbW.execSQL("DROP TABLE IF EXISTS " + TABLE_SPIELER);
    }


    /**
     * Fügt einen neuen Eintrag in die Tabelle ein
     */
    public void add(Spieler spieler) {

        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, spieler.getMannschaftsID());
        values.put(COLUMN_VORNAME, spieler.getVorname());
        values.put(COLUMN_NACHNAME, spieler.getNachname());
        values.put(COLUMN_TRIKONUMMER, spieler.getTrikonummer());
        values.put(COLUMN_GROESSE, spieler.getGroesse());
        values.put(COLUMN_POSITION, gson.toJson(spieler.getPosition()).getBytes());

        Bitmap bitmap = spieler.getFoto();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_FOTO, stream.toByteArray());

        values.put(COLUMN_SELECTED, spieler.getSelected());
        values.put(COLUMN_SPORTART, spieler.getSportart());

        dbW.insert(TABLE_SPIELER, null, values);
    }


    /**
     * Liefert sämtliche Datensätze zurück die die Sportart aufweisen
     */
    public ArrayList<Spieler> findBySportart(String sportart){

        Gson gson = new Gson();
        String query = "Select * FROM " + TABLE_SPIELER + " WHERE " + COLUMN_SPORTART + " =  \"" + sportart + "\"";

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Spieler> spielerListe = new ArrayList<Spieler>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Spieler spieler = new Spieler();
                spieler.setId(Integer.parseInt(cursor.getString(0)));
                spieler.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                spieler.setVorname(cursor.getString(2));
                spieler.setNachname(cursor.getString(3));
                spieler.setTrikonummer(Integer.parseInt(cursor.getString(4)));
                spieler.setGroesse(Integer.parseInt(cursor.getString(5)));

                byte[] blob = cursor.getBlob(6);
                String json = new String(blob);
                ArrayList<Position> positionen = gson.fromJson(json, new TypeToken<ArrayList<Position>>() {}.getType());
                spieler.setPosition(positionen);

                blob = cursor.getBlob(7);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                spieler.setFoto(foto);

                spieler.setSelected(Integer.parseInt(cursor.getString(8)));
                spieler.setSportart(cursor.getString(9));

                spielerListe.add(spieler);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return spielerListe;
    }


    /**
     * Liefert den Datensatz mit einer bestimmten ID zurück
     */
    public Spieler findById(int id) {
        Gson gson = new Gson();
        String query = "Select * FROM " + TABLE_SPIELER + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

        Cursor cursor = dbR.rawQuery(query, null);

        Spieler spieler = new Spieler();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            spieler.setId(Integer.parseInt(cursor.getString(0)));
            spieler.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
            spieler.setVorname(cursor.getString(2));
            spieler.setNachname(cursor.getString(3));
            spieler.setTrikonummer(Integer.parseInt(cursor.getString(4)));
            spieler.setGroesse(Integer.parseInt(cursor.getString(5)));

            byte[] blob = cursor.getBlob(6);
            String json = new String(blob);
            ArrayList<Position> positionen = gson.fromJson(json, new TypeToken<ArrayList<Position>>() {}.getType());
            spieler.setPosition(positionen);

            blob = cursor.getBlob(7);
            Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            spieler.setFoto(foto);

            spieler.setSelected(Integer.parseInt(cursor.getString(8)));
            spieler.setSportart(cursor.getString(9));
            cursor.close();
        } else {
            spieler = null;
        }
        cursor.close();

        return spieler;
    }


    /**
     * Aktualisiert einen Datensatz
     */
    public void update(Spieler spieler) {

        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, spieler.getMannschaftsID());
        values.put(COLUMN_VORNAME, spieler.getVorname());
        values.put(COLUMN_NACHNAME, spieler.getNachname());
        values.put(COLUMN_TRIKONUMMER, spieler.getTrikonummer());
        values.put(COLUMN_GROESSE, spieler.getGroesse());
        values.put(COLUMN_POSITION, gson.toJson(spieler.getPosition()).getBytes());

        Bitmap bitmap = spieler.getFoto();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_FOTO, stream.toByteArray());

        values.put(COLUMN_SELECTED, spieler.getSelected());
        values.put(COLUMN_SPORTART, spieler.getSportart());

        dbW.update(TABLE_SPIELER, values, COLUMN_ID + " = " + spieler.getId(), null);
        dbW.close();
    }


    /**
     * Löscht einen Datensatz
     */
    public void delete(Spieler spieler) {

        dbW.delete(TABLE_SPIELER, COLUMN_ID + " = " + spieler.getId(), null);
        dbW.close();
    }


    /**
     * Liefert sämtliche Datensätze zurück die die Mannschaft aufweisen
     */
    public ArrayList<Spieler> findByMannschaft(Mannschaft mannschaft){
        Gson gson = new Gson();
        String query = "Select * FROM " + TABLE_SPIELER + " WHERE " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId();

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Spieler> spielerListe = new ArrayList<Spieler>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Spieler spieler = new Spieler();
                spieler.setId(Integer.parseInt(cursor.getString(0)));
                spieler.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                spieler.setVorname(cursor.getString(2));
                spieler.setNachname(cursor.getString(3));
                spieler.setTrikonummer(Integer.parseInt(cursor.getString(4)));
                spieler.setGroesse(Integer.parseInt(cursor.getString(5)));

                byte[] blob = cursor.getBlob(6);
                String json = new String(blob);
                ArrayList<Position> positionen = gson.fromJson(json, new TypeToken<ArrayList<Position>>() {}.getType());
                spieler.setPosition(positionen);

                blob = cursor.getBlob(7);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                spieler.setFoto(foto);

                spieler.setSelected(Integer.parseInt(cursor.getString(8)));
                spieler.setSportart(cursor.getString(9));

                spielerListe.add(spieler);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return spielerListe;
    }

    /**
     * Liefert sämtliche Datensätze zurück die die Mannschaft aufweisen und aktiv sind
     */
    public ArrayList<Spieler> findByMannschaftAndAktiv(Mannschaft mannschaft){
        Gson gson = new Gson();
        String query = "Select * FROM " + TABLE_SPIELER + " WHERE " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId() + " AND " + COLUMN_SELECTED + " = 1";

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Spieler> spielerListe = new ArrayList<Spieler>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Spieler spieler = new Spieler();
                spieler.setId(Integer.parseInt(cursor.getString(0)));
                spieler.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                spieler.setVorname(cursor.getString(2));
                spieler.setNachname(cursor.getString(3));
                spieler.setTrikonummer(Integer.parseInt(cursor.getString(4)));
                spieler.setGroesse(Integer.parseInt(cursor.getString(5)));

                byte[] blob = cursor.getBlob(6);
                String json = new String(blob);
                ArrayList<Position> positionen = gson.fromJson(json, new TypeToken<ArrayList<Position>>() {}.getType());
                spieler.setPosition(positionen);

                blob = cursor.getBlob(7);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                spieler.setFoto(foto);

                spieler.setSelected(Integer.parseInt(cursor.getString(8)));
                spieler.setSportart(cursor.getString(9));

                spielerListe.add(spieler);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return spielerListe;
    }


    /**
     * Liefert sämtliche Datensätze einer Statistik zurück
     */
    public ArrayList<Spieler> findByStatistik(Statistik statistik){

        String TABLE_STATISTIKWERTE = "statistikwerte";
        String COLUMN_SPIELERID = "_spielerid";
        String COLUMN_STATISTIKID = "_statistikid";

        String query = "Select DISTINCT " + COLUMN_SPIELERID + " FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistik.getId();

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Spieler> spielerListe = new ArrayList<Spieler>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int spielerId = Integer.parseInt(cursor.getString(0));
                spielerListe.add(findById(spielerId));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return spielerListe;
    }


    public void setDB(SQLiteOpenHelper db){
        this.dbR = db.getReadableDatabase();
        this.dbW = db.getWritableDatabase();
    }
}
