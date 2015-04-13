package database.sql_statements;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;

import database.data.Mannschaft;
import database.data.Position;
import database.data.Statistik;
import database.data.Statistikwerte;

/**
 * Created by Spaceman on 01.04.2015.
 */
public class SQL_Statistiken {

    private final String TABLE_STATISTIK = "statistik";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_MANNSCHAFTSID = "_mannschaftsid";
    private final String COLUMN_DATUM = "datum";
    private final String COLUMN_HEIM = "heim";
    private final String COLUMN_GEGNER = "gegner";
    private final String COLUMN_EIGENETORE = "eigenetore";
    private final String COLUMN_GEGNERTORE = "gegnertore";

    private SQLiteDatabase dbW;
    private SQLiteDatabase dbR;

    public SQL_Statistiken(SQLiteOpenHelper db){
        this.dbW = db.getWritableDatabase();
        this.dbR = db.getReadableDatabase();
    }

    public SQL_Statistiken(SQLiteDatabase dbW){
        this.dbW = dbW;
    }

    public void createTable(){

        String CREATE_STATISTIK_TABLE = "CREATE TABLE " +
                TABLE_STATISTIK + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_MANNSCHAFTSID + " INTEGER,"
                + COLUMN_DATUM + " TEXT,"
                + COLUMN_HEIM + " INTEGER,"
                + COLUMN_GEGNER + " TEXT,"
                + COLUMN_EIGENETORE + " INTEGER,"
                + COLUMN_GEGNERTORE + " INTEGER"
                + ")";

        dbW.execSQL(CREATE_STATISTIK_TABLE);
    }

    public void deleteTable(){
        dbW.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTIK);
    }

    public void add(Statistik statistik) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, statistik.getMannschaftsid());
        values.put(COLUMN_DATUM, statistik.getDatum());
        values.put(COLUMN_HEIM, statistik.getHeim());
        values.put(COLUMN_GEGNER, statistik.getGegner());
        values.put(COLUMN_EIGENETORE, statistik.getEigeneTore());
        values.put(COLUMN_GEGNERTORE, statistik.getGegnerTore());

        dbW.insert(TABLE_STATISTIK, null, values);
    }

    public void update(Statistik statistik) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, statistik.getMannschaftsid());
        values.put(COLUMN_DATUM, statistik.getDatum());
        values.put(COLUMN_HEIM, statistik.getHeim());
        values.put(COLUMN_GEGNER, statistik.getGegner());
        values.put(COLUMN_EIGENETORE, statistik.getEigeneTore());
        values.put(COLUMN_GEGNERTORE, statistik.getGegnerTore());

        dbW.update(TABLE_STATISTIK, values, COLUMN_ID + " = " + statistik.getId(), null);
    }

    public void delete(Statistik statistik) {
        dbW.delete(TABLE_STATISTIK, COLUMN_ID + " = " + statistik.getId(), null);
    }


    public Statistik getLastId(){
        String query = "Select * FROM " + TABLE_STATISTIK;

        Cursor cursor = dbR.rawQuery(query, null);

        Statistik statistik = new Statistik();

        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();

            statistik.setId(Integer.parseInt(cursor.getString(0)));
            statistik.setMannschaftsid(Integer.parseInt(cursor.getString(1)));
            statistik.setDatum(cursor.getString(2));
            statistik.setHeim(Integer.parseInt(cursor.getString(3)));
            statistik.setGegner(cursor.getString(4));
            statistik.setEigeneTore(Integer.parseInt(cursor.getString(5)));
            statistik.setGegnerTore(Integer.parseInt(cursor.getString(6)));
        }

        cursor.close();

        return statistik;
    }

    public Statistik findById(int id){
        String query = "Select * FROM " + TABLE_STATISTIK + " WHERE " + COLUMN_ID + " = " + id;

        Cursor cursor = dbR.rawQuery(query, null);

        Statistik statistik = new Statistik();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            statistik.setId(Integer.parseInt(cursor.getString(0)));
            statistik.setMannschaftsid(Integer.parseInt(cursor.getString(1)));
            statistik.setDatum(cursor.getString(2));
            statistik.setHeim(Integer.parseInt(cursor.getString(3)));
            statistik.setGegner(cursor.getString(4));
            statistik.setEigeneTore(Integer.parseInt(cursor.getString(5)));
            statistik.setGegnerTore(Integer.parseInt(cursor.getString(6)));
        }
        else{
            statistik = null;
        }
        cursor.close();

        return statistik;
    }

    public ArrayList<Statistik> findByMannschaft(Mannschaft mannschaft){
        String query = "Select * FROM " + TABLE_STATISTIK + " WHERE " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId();

        Cursor cursor = dbR.rawQuery(query, null);

        ArrayList<Statistik> statistikListe = new ArrayList<Statistik>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Statistik statistik = new Statistik();

                statistik.setId(Integer.parseInt(cursor.getString(0)));
                statistik.setMannschaftsid(Integer.parseInt(cursor.getString(1)));
                statistik.setDatum(cursor.getString(2));
                statistik.setHeim(Integer.parseInt(cursor.getString(3)));
                statistik.setGegner(cursor.getString(4));
                statistik.setEigeneTore(Integer.parseInt(cursor.getString(5)));
                statistik.setGegnerTore(Integer.parseInt(cursor.getString(6)));

                statistikListe.add(statistik);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return statistikListe;
    }

    public ArrayList<String> getÜbersichtDerMannschaft(Mannschaft mannschaft){

        ArrayList<String> übersicht = new ArrayList<>();

        ArrayList<Statistik> statistiken = findByMannschaft(mannschaft);

        //Anzahl Spiele
        int anzahl = statistiken.size();
        übersicht.add(String.valueOf(anzahl));

        //Anzahl Gewonnen/Verloren/Unentschieden/Tore/Gegentore
        int gewonnen = 0;
        int verloren = 0;
        int unentschieden = 0;
        int tore = 0;
        int gegentore = 0;

        for(int i=0; i<statistiken.size(); i++){
            Statistik statistik = statistiken.get(i);

            if(statistik.getEigeneTore() > statistik.getGegnerTore()){
                gewonnen++;
            }
            else if(statistik.getEigeneTore() < statistik.getGegnerTore()){
                verloren++;
            }
            else {
                unentschieden++;
            }

            tore += statistik.getEigeneTore();
            gegentore += statistik.getGegnerTore();
        }

        übersicht.add(String.valueOf(gewonnen));
        übersicht.add(String.valueOf(verloren));
        übersicht.add(String.valueOf(unentschieden));
        übersicht.add(String.valueOf(tore));
        übersicht.add(String.valueOf(gegentore));



        //Durchschnitt errechnen
        double schnittTore = 0.0;
        double schnittGegentore = 0.0;
        if(anzahl != 0) {
            schnittTore = (double)tore / (double)anzahl;
            schnittGegentore = (double)gegentore / (double)anzahl;
        }

        übersicht.add(String.valueOf(String.format("%.1f", schnittTore)));
        übersicht.add(String.valueOf(String.format("%.1f", schnittGegentore)));

        return übersicht;
    }

    public void setDB(SQLiteOpenHelper db){
        this.dbR = db.getReadableDatabase();
        this.dbW = db.getWritableDatabase();
    }

}
