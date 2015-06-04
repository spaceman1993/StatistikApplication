package database.sql_statements;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import database.data.Position;

/**
 * Hier sind alle notwendigen SQL-Statements gesammelt, die für die Position notwenidig sind
 */
public class SQL_Position {

    private final String TABLE_POSITION = "position";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_NAMEKURZ = "vorname";
    private final String COLUMN_NAMELANG = "nachname";
    private final String COLUMN_SPORTART = "sportart";

    //Datenbankzugriff mit Schreibrechten
    private SQLiteDatabase dbW;
    //Datenbankzugriff mit Leserechten
    private SQLiteDatabase dbR;

    public SQL_Position(SQLiteOpenHelper db){
        this.dbW = db.getWritableDatabase();
        this.dbR = db.getReadableDatabase();
    }

    public SQL_Position(SQLiteDatabase dbW){
        this.dbW = dbW;
    }

    /**
     * Erzeugt die Position-Tabelle in der Datenbank
     */
    public void createTable(){

        String CREATE_POSITION_TABLE = "CREATE TABLE " +
                TABLE_POSITION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAMEKURZ + " TEXT,"
                + COLUMN_NAMELANG + " TEXT,"
                + COLUMN_SPORTART + " TEXT"
                + ")";

        dbW.execSQL(CREATE_POSITION_TABLE);
    }

    /**
     * Löscht die Position-Tabelle in der Datenbank
     */
    public void deleteTable(){
        dbW.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITION);
    }


    /**
     * Fügt einen neuen Eintrag in die Tabelle ein
     */
    public void add(Position position) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMEKURZ, position.getNameKurz());
        values.put(COLUMN_NAMELANG, position.getNameLang());
        values.put(COLUMN_SPORTART, position.getSportart());

        dbW.insert(TABLE_POSITION, null, values);
    }


    /**
     * Fügt einen neuen Eintrag in die Tabelle ein
     */
    public void add(Position position, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMEKURZ, position.getNameKurz());
        values.put(COLUMN_NAMELANG, position.getNameLang());
        values.put(COLUMN_SPORTART, position.getSportart());

        db.insert(TABLE_POSITION, null, values);
    }


    /**
     * Liefert den Datensatz mit einer bestimmten ID zurück
     */
    public Position findById(int id) {
        String query = "Select * FROM " + TABLE_POSITION + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

        Cursor cursor = dbR.rawQuery(query, null);

        Position position = new Position();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            position.setId(Integer.parseInt(cursor.getString(0)));
            position.setNameKurz(cursor.getString(1));
            position.setNameLang(cursor.getString(2));
            position.setSportart(cursor.getString(3));

        }

        cursor.close();

        return position;
    }


    /**
     * Liefert sämtliche Datensätze zurück die die Sportart aufweisen
     */
    public ArrayList<Position> findBySportart(String sportart){

        String query = "Select * FROM " + TABLE_POSITION + " WHERE " + COLUMN_SPORTART + " =  \"" + sportart + "\"";

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Position> positionsListe = new ArrayList<Position>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Position position = new Position();
                position.setId(Integer.parseInt(cursor.getString(0)));
                position.setNameKurz(cursor.getString(1));
                position.setNameLang(cursor.getString(2));
                position.setSportart(cursor.getString(3));

                positionsListe.add(position);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return positionsListe;
    }


    /**
     * Aktualisiert einen Datensatz
     */
    public void update(Position position) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMEKURZ, position.getNameKurz());
        values.put(COLUMN_NAMELANG, position.getNameLang());
        values.put(COLUMN_SPORTART, position.getSportart());

        dbW.update(TABLE_POSITION, values, COLUMN_ID + " = " + position.getId(), null);
    }


    /**
     * Löscht einen Datensatz
     */
    public void deletePosition(Position position) {

        dbW.delete(TABLE_POSITION, COLUMN_ID + " = " + position.getId(), null);
    }

    public void setDB(SQLiteOpenHelper db){
        this.dbR = db.getReadableDatabase();
        this.dbW = db.getWritableDatabase();
    }
}
