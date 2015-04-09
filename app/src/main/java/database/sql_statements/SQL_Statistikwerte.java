package database.sql_statements;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Mannschaft;
import database.data.Position;
import database.data.Spieler;
import database.data.Statistik;
import database.data.Statistikwerte;
import szut.de.statistikapplication.Globals;

/**
 * Created by Spaceman on 01.04.2015.
 */
public class SQL_Statistikwerte {

    private final String TABLE_STATISTIKWERTE = "statistikwerte";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_STATISTIKID = "_statistikid";
    private final String COLUMN_SPIELERID = "_spielerid";
    private final String COLUMN_KATEGORIEID = "_kategorieid";
    private final String COLUMN_WERT = "wert";

    private SQLiteDatabase dbW;
    private SQLiteDatabase dbR;

    public SQL_Statistikwerte(SQLiteOpenHelper db){
        this.dbW = db.getWritableDatabase();
        this.dbR = db.getReadableDatabase();
    }

    public SQL_Statistikwerte(SQLiteDatabase dbW){
        this.dbW = dbW;
    }

    public void createTable(){

        String CREATE_STATISTIKWERTE_TABLE = "CREATE TABLE " +
                TABLE_STATISTIKWERTE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_STATISTIKID + " INTEGER,"
                + COLUMN_SPIELERID + " INTEGER,"
                + COLUMN_KATEGORIEID + " INTEGER,"
                + COLUMN_WERT + " TEXT"
                + ")";

        dbW.execSQL(CREATE_STATISTIKWERTE_TABLE);
    }

    public void deleteTable(){
        dbW.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTIKWERTE);
    }


    public void add(Statistikwerte statistikwerte) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_STATISTIKID, statistikwerte.getStatistikId());
        values.put(COLUMN_SPIELERID, statistikwerte.getSpielerId());
        values.put(COLUMN_KATEGORIEID, statistikwerte.getKategorieId());
        values.put(COLUMN_WERT, statistikwerte.getWert());

        dbW.insert(TABLE_STATISTIKWERTE, null, values);
    }

    public void update(Statistikwerte statistikwerte) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_STATISTIKID, statistikwerte.getStatistikId());
        values.put(COLUMN_SPIELERID, statistikwerte.getSpielerId());
        values.put(COLUMN_KATEGORIEID, statistikwerte.getKategorieId());
        values.put(COLUMN_WERT, statistikwerte.getWert());

        dbW.update(TABLE_STATISTIKWERTE, values, COLUMN_ID + " = " + statistikwerte.getId(), null);
    }

    public void delete(Statistikwerte statistikwerte) {
        dbW.delete(TABLE_STATISTIKWERTE, COLUMN_ID + " = " + statistikwerte.getId(), null);
    }

    public ArrayList<Statistikwerte> findByStatistik(Statistik statistik){

        String query = "Select * FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistik.getId();

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Statistikwerte> statistikwerteListe = new ArrayList<Statistikwerte>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Statistikwerte statistikwerte = new Statistikwerte();
                statistikwerte.setId(Integer.parseInt(cursor.getString(0)));
                statistikwerte.setStatistikId(Integer.parseInt(cursor.getString(1)));
                statistikwerte.setSpielerId(Integer.parseInt(cursor.getString(2)));
                statistikwerte.setKategorieId(Integer.parseInt(cursor.getString(3)));
                statistikwerte.setWert(cursor.getString(4));

                statistikwerteListe.add(statistikwerte);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return statistikwerteListe;
    }


    public ArrayList<Statistikwerte> findByStatistikOfSpieler(Statistik statistik, Spieler spieler){

        String query = "Select * FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistik.getId() + " AND " + COLUMN_SPIELERID + " = " + spieler.getId();

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Statistikwerte> statistikwerteListe = new ArrayList<Statistikwerte>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Statistikwerte statistikwerte = new Statistikwerte();
                statistikwerte.setId(Integer.parseInt(cursor.getString(0)));
                statistikwerte.setStatistikId(Integer.parseInt(cursor.getString(1)));
                statistikwerte.setSpielerId(Integer.parseInt(cursor.getString(2)));
                statistikwerte.setKategorieId(Integer.parseInt(cursor.getString(3)));
                statistikwerte.setWert(cursor.getString(4));

                statistikwerteListe.add(statistikwerte);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return statistikwerteListe;
    }

    public ArrayList<Statistikwerte> findBySpielerAndKategorie(Spieler spieler, Kategorie kategorie){

        String query = "Select * FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_SPIELERID + " = " + spieler.getId() + " AND " + COLUMN_KATEGORIEID + " = " + kategorie.getId();

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Statistikwerte> statistikwerteListe = new ArrayList<Statistikwerte>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Statistikwerte statistikwerte = new Statistikwerte();
                statistikwerte.setId(Integer.parseInt(cursor.getString(0)));
                statistikwerte.setStatistikId(Integer.parseInt(cursor.getString(1)));
                statistikwerte.setSpielerId(Integer.parseInt(cursor.getString(2)));
                statistikwerte.setKategorieId(Integer.parseInt(cursor.getString(3)));
                statistikwerte.setWert(cursor.getString(4));

                statistikwerteListe.add(statistikwerte);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return statistikwerteListe;
    }

    public ArrayList<Statistikwerte> findByStatistikOfKategorie(Statistik statistik, int kategorieId){

        String query = "Select * FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistik.getId() + " AND " + COLUMN_KATEGORIEID + " = " + kategorieId;

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Statistikwerte> statistikwerteListe = new ArrayList<Statistikwerte>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Statistikwerte statistikwerte = new Statistikwerte();
                statistikwerte.setId(Integer.parseInt(cursor.getString(0)));
                statistikwerte.setStatistikId(Integer.parseInt(cursor.getString(1)));
                statistikwerte.setSpielerId(Integer.parseInt(cursor.getString(2)));
                statistikwerte.setKategorieId(Integer.parseInt(cursor.getString(3)));
                statistikwerte.setWert(cursor.getString(4));

                statistikwerteListe.add(statistikwerte);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return statistikwerteListe;
    }

    public Statistikwerte findById(int statistikId, int spielerId, int kategorieId) {

        String query = "Select * FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistikId + " AND " + COLUMN_SPIELERID + " = " + spielerId + " AND " + COLUMN_KATEGORIEID + " = " + kategorieId;

        Cursor cursor = dbR.rawQuery(query, null);

        Statistikwerte statistikwert = new Statistikwerte();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            statistikwert.setId(Integer.parseInt(cursor.getString(0)));
            statistikwert.setStatistikId(Integer.parseInt(cursor.getString(1)));
            statistikwert.setSpielerId(Integer.parseInt(cursor.getString(2)));
            statistikwert.setKategorieId(Integer.parseInt(cursor.getString(3)));
            statistikwert.setWert(cursor.getString(4));
            cursor.close();
        } else {
            statistikwert = null;
        }
        cursor.close();

        return statistikwert;
    }

    public int getToreByStatistik(Statistik statistik){

        int tore = 0;

        ArrayList<Statistikwerte> statistikwerte = findByStatistikOfKategorie(statistik, 0);

        for(int i=0; i<statistikwerte.size(); i++){
            tore += Integer.parseInt(statistikwerte.get(i).getWert());
        }

        return tore;
    }

    public String getGesamtwertOfKategorieOfSpieler(Spieler spieler, Kategorie kategorie){
        String wert = "";

        ArrayList<Statistikwerte> statistikwerte = findBySpielerAndKategorie(spieler, kategorie);

        if(kategorie.getArt().equals("Zähler") || kategorie.getArt().equals("Auto-Zähler")){
            int wertInt = 0;

            for (int i=0; i<statistikwerte.size(); i++){
                wertInt += Integer.parseInt(statistikwerte.get(i).getWert());
            }

            wert = String.valueOf(wertInt);
        }
        else if(kategorie.getArt().equals("Fließzahleingabe")){
            double wertDouble = 0;

            for (int i=0; i<statistikwerte.size(); i++){
                wertDouble += Double.parseDouble(statistikwerte.get(i).getWert());
            }

            wert = String.valueOf(wertDouble);
        }
        else if(kategorie.getArt().equals("Checkbox")){
            int wertInt = 0;

            for (int i=0; i<statistikwerte.size(); i++){
                if(statistikwerte.get(i).getWert() == "1") {
                    wertInt++;
                }
            }

            wert = String.valueOf(wertInt);
        }
        else if(kategorie.getArt().equals("Timer")) {

            for (int i = 0; i < statistikwerte.size(); i++) {

            }
        }

        return wert;
    }

    public void setDB(SQLiteOpenHelper db){
        this.dbR = db.getReadableDatabase();
        this.dbW = db.getWritableDatabase();
    }
}
