package database.sql_statements;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import database.data.Kategorie;
import database.data.Mannschaft;
import database.data.Statistik;

/**
 * Hier sind alle notwendigen SQL-Statements gesammelt, die für die Kategorien notwenidig sind
 */
public class SQL_Kategorien {

    private final String TABLE_KATEGORIEN = "kategorien";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_MANNSCHAFTSID = "_mannschaftsid";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_KATEGORISIERUNG = "kategorisierung";
    private final String COLUMN_ART = "art";
    private final String COLUMN_EIGENE = "eigene";
    private final String COLUMN_SPORTART = "sportart";
    private final String COLUMN_FOTO = "foto";
    private final String COLUMN_SELECTED = "selected";

    //Datenbankzugriff mit Schreibrechten
    private SQLiteDatabase dbW;
    //Datenbankzugriff mit Leserechten
    private SQLiteDatabase dbR;

    public SQL_Kategorien(SQLiteOpenHelper db){
        this.dbW = db.getWritableDatabase();
        this.dbR = db.getReadableDatabase();
    }

    public SQL_Kategorien(SQLiteDatabase dbW){
        this.dbW = dbW;
    }

    /**
     * Erzeugt die Kategorien-Tabelle in der Datenbank
     */
    public void createTable(){
        String CREATE_KATEGORIEN_TABLE = "CREATE TABLE " +
                TABLE_KATEGORIEN + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_MANNSCHAFTSID + " INTEGER,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_KATEGORISIERUNG+ " TEXT,"
                + COLUMN_ART + " TEXT,"
                + COLUMN_FOTO + " BLOB,"
                + COLUMN_SELECTED + " INTEGER,"
                + COLUMN_EIGENE + " INTEGER,"
                + COLUMN_SPORTART + " TEXT"
                + ")";

        dbW.execSQL(CREATE_KATEGORIEN_TABLE);
    }

    /**
     * Löscht die Kategorien-Tabelle in der Datenbank
     */
    public void deleteTable(){
        dbW.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORIEN);
    }

    /**
     * Fügt einen neuen Eintrag in die Kategorien-Tabelle ein
     */
    public void add(Kategorie kategorie) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, kategorie.getMannschaftsID());
        values.put(COLUMN_NAME, kategorie.getName());
        values.put(COLUMN_KATEGORISIERUNG, kategorie.getKategorisierung());
        values.put(COLUMN_ART, kategorie.getArt());

        Bitmap bitmap = kategorie.getFoto();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_FOTO, stream.toByteArray());

        values.put(COLUMN_SELECTED, kategorie.getSelected());
        values.put(COLUMN_EIGENE, kategorie.getEigene());
        values.put(COLUMN_SPORTART, kategorie.getSportart());

        dbW.insert(TABLE_KATEGORIEN, null, values);
    }

    /**
     * Fügt einen neuen Eintrag in die Kategorien-Tabelle ein
     */
    public void add(Kategorie kategorie, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, kategorie.getMannschaftsID());
        values.put(COLUMN_NAME, kategorie.getName());
        values.put(COLUMN_KATEGORISIERUNG, kategorie.getKategorisierung());
        values.put(COLUMN_ART, kategorie.getArt());

        Bitmap bitmap = kategorie.getFoto();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_FOTO, stream.toByteArray());

        values.put(COLUMN_SELECTED, kategorie.getSelected());
        values.put(COLUMN_EIGENE, kategorie.getEigene());
        values.put(COLUMN_SPORTART, kategorie.getSportart());

        db.insert(TABLE_KATEGORIEN, null, values);

    }

    /**
     * Liefert den Datensatz mit einer bestimmten ID zurück
     */
    public Kategorie findById(int id) {
        String query = "Select * FROM " + TABLE_KATEGORIEN + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

        Cursor cursor = dbR.rawQuery(query, null);

        Kategorie kategorie = new Kategorie();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            kategorie.setId(Integer.parseInt(cursor.getString(0)));
            kategorie.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
            kategorie.setName(cursor.getString(2));
            kategorie.setKategorisierung(cursor.getString(3));
            kategorie.setArt(cursor.getString(4));

            byte[] blob = cursor.getBlob(5);
            Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            kategorie.setFoto(foto);

            kategorie.setSelected(Integer.parseInt(cursor.getString(6)));
            kategorie.setEigene(Integer.parseInt(cursor.getString(7)));
            kategorie.setSportart(cursor.getString(8));
        }

        cursor.close();

        return kategorie;
    }

    /**
     * Liefert sämtliche Datensätze zurück die die Sportart aufweisen
     */
    public ArrayList<Kategorie> findBySportart(String sportart) {

        String query = "Select * FROM " + TABLE_KATEGORIEN + " WHERE " + COLUMN_SPORTART + " =  \"" + sportart + "\"";

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Kategorie> kategorienListe = new ArrayList<Kategorie>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Kategorie kategorie = new Kategorie();

                kategorie.setId(Integer.parseInt(cursor.getString(0)));
                kategorie.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                kategorie.setName(cursor.getString(2));
                kategorie.setKategorisierung(cursor.getString(3));
                kategorie.setArt(cursor.getString(4));

                byte[] blob = cursor.getBlob(5);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                kategorie.setFoto(foto);

                kategorie.setSelected(Integer.parseInt(cursor.getString(6)));
                kategorie.setEigene(Integer.parseInt(cursor.getString(7)));
                kategorie.setSportart(cursor.getString(8));

                kategorienListe.add(kategorie);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return kategorienListe;
    }

    /**
     * Liefert sämtliche Datensätze zurück die die Mannschaft aufweisen
     */
    public ArrayList<Kategorie> findByMannschaft(Mannschaft mannschaft) {

        String query = "Select * FROM " + TABLE_KATEGORIEN + " WHERE " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId();

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Kategorie> kategorienListe = new ArrayList<Kategorie>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Kategorie kategorie = new Kategorie();

                kategorie.setId(Integer.parseInt(cursor.getString(0)));
                kategorie.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                kategorie.setName(cursor.getString(2));
                kategorie.setKategorisierung(cursor.getString(3));
                kategorie.setArt(cursor.getString(4));

                byte[] blob = cursor.getBlob(5);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                kategorie.setFoto(foto);

                kategorie.setSelected(Integer.parseInt(cursor.getString(6)));
                kategorie.setEigene(Integer.parseInt(cursor.getString(7)));
                kategorie.setSportart(cursor.getString(8));

                kategorienListe.add(kategorie);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return kategorienListe;
    }

    /**
     * Liefert sämtliche Datensätze zurück die dem Defaultwert einer Sportart aufweisen
     */
    public ArrayList<Kategorie> findAllDefaultKategorienBySportart(String sportart){

        String query = "Select * FROM " + TABLE_KATEGORIEN + " WHERE " + COLUMN_MANNSCHAFTSID + " = 0 AND " + COLUMN_SPORTART + " = '" + sportart + "'";

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Kategorie> kategorienListe = new ArrayList<Kategorie>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Kategorie kategorie = new Kategorie();

                kategorie.setId(Integer.parseInt(cursor.getString(0)));
                kategorie.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                kategorie.setName(cursor.getString(2));
                kategorie.setKategorisierung(cursor.getString(3));
                kategorie.setArt(cursor.getString(4));

                byte[] blob = cursor.getBlob(5);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                kategorie.setFoto(foto);

                kategorie.setSelected(Integer.parseInt(cursor.getString(6)));
                kategorie.setEigene(Integer.parseInt(cursor.getString(7)));
                kategorie.setSportart(cursor.getString(8));

                kategorienListe.add(kategorie);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return kategorienListe;

    };

    /**
     * Liefert sämtliche Datensätze zurück die die Mannschaft aufweisen und aktiv sind
     */
    public ArrayList<Kategorie> findByMannschaftAndAktiv(Mannschaft mannschaft) {

        String query = "Select * FROM " + TABLE_KATEGORIEN + " WHERE " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId() + " AND " + COLUMN_SELECTED + " = 1";

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Kategorie> kategorienListe = new ArrayList<Kategorie>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Kategorie kategorie = new Kategorie();

                kategorie.setId(Integer.parseInt(cursor.getString(0)));
                kategorie.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                kategorie.setName(cursor.getString(2));
                kategorie.setKategorisierung(cursor.getString(3));
                kategorie.setArt(cursor.getString(4));

                byte[] blob = cursor.getBlob(5);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                kategorie.setFoto(foto);

                kategorie.setSelected(Integer.parseInt(cursor.getString(6)));
                kategorie.setEigene(Integer.parseInt(cursor.getString(7)));
                kategorie.setSportart(cursor.getString(8));

                kategorienListe.add(kategorie);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return kategorienListe;
    }

    /**
     * Aktualisiert einen Datensatz
     */
    public void update(Kategorie kategorie) {

        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, kategorie.getMannschaftsID());
        values.put(COLUMN_NAME, kategorie.getName());
        values.put(COLUMN_KATEGORISIERUNG, kategorie.getKategorisierung());
        values.put(COLUMN_ART, kategorie.getArt());

        Bitmap bitmap = kategorie.getFoto();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_FOTO, stream.toByteArray());

        values.put(COLUMN_SELECTED, kategorie.getSelected());
        values.put(COLUMN_EIGENE, kategorie.getEigene());
        values.put(COLUMN_SPORTART, kategorie.getSportart());

        dbW.update(TABLE_KATEGORIEN, values, COLUMN_ID + " = " + kategorie.getId(), null);
        dbW.close();
    }

    /**
     * Löscht einen Datensatz
     */
    public void delete(Kategorie kategorie) {

        dbW.delete(TABLE_KATEGORIEN, COLUMN_ID + " = " + kategorie.getId(), null);
        dbW.close();
    }

    /**
     * Liefert sämtliche Kategorien einer Statistik zurück
     */
    public ArrayList<Kategorie> findByStatistik(Statistik statistik){

        String TABLE_STATISTIKWERTE = "statistikwerte";
        String COLUMN_STATISTIKID = "_statistikid";
        String COLUMN_KATEGORIEID = "_kategorieid";

        String query = "Select DISTINCT " + COLUMN_KATEGORIEID + " FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistik.getId();

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Kategorie> kategorienListe = new ArrayList<Kategorie>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int kategorieId = Integer.parseInt(cursor.getString(0));
                kategorienListe.add(findById(kategorieId));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return kategorienListe;
    }

    /**
     * Liefert sämtliche Datensätze zurück die die Kategorisierung aufweisen
     */
    public ArrayList<Kategorie> findByKategorisierung(String kategorieart, Mannschaft mannschaft){
        String query = "Select * FROM " + TABLE_KATEGORIEN + " WHERE " + COLUMN_SELECTED + " = 1 AND " + COLUMN_KATEGORISIERUNG + " =  \"" + kategorieart + "\" AND " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId();

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Kategorie> kategorienListe = new ArrayList<Kategorie>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Kategorie kategorie = new Kategorie();

                kategorie.setId(Integer.parseInt(cursor.getString(0)));
                kategorie.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                kategorie.setName(cursor.getString(2));
                kategorie.setKategorisierung(cursor.getString(3));
                kategorie.setArt(cursor.getString(4));

                byte[] blob = cursor.getBlob(5);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                kategorie.setFoto(foto);

                kategorie.setSelected(Integer.parseInt(cursor.getString(6)));
                kategorie.setEigene(Integer.parseInt(cursor.getString(7)));
                kategorie.setSportart(cursor.getString(8));

                kategorienListe.add(kategorie);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return kategorienListe;
    }

    /**
     * Liefert sämtliche Datensätze zurück die die Kategorienartbezeichnung einer Mannschaft aufweisen
     */
    public ArrayList<String> findAllKategorieartbezeichnungenByMannschaft(Mannschaft mannschaft){
        String query = "Select DISTINCT " + COLUMN_KATEGORISIERUNG + " FROM " + TABLE_KATEGORIEN + " WHERE " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId() + " AND " + COLUMN_SELECTED + " = 1";

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<String> kategorienartenbezeichnungen = new ArrayList<>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                kategorienartenbezeichnungen.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }

        cursor.close();

        return kategorienartenbezeichnungen;
    }

    /**
     * Liefert sämtliche Datensätze zurück die die Kategorieart einer Mannschaft aufweisen
     */
    public ArrayList<ArrayList<Kategorie>> findByKategorieartAll(Mannschaft mannschaft){

        ArrayList<ArrayList<Kategorie>> kategorienListe = new ArrayList<>();

        ArrayList<String> kategorieartenbezeichnungen = findAllKategorieartbezeichnungenByMannschaft(mannschaft);

        for(int i=0; i<kategorieartenbezeichnungen.size(); i++){
            kategorienListe.add(findByKategorisierung(kategorieartenbezeichnungen.get(i), mannschaft));
        }

        return kategorienListe;
    }

    public void setDB(SQLiteOpenHelper db){
        this.dbR = db.getReadableDatabase();
        this.dbW = db.getWritableDatabase();
    }
}
