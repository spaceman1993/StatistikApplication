package hilfklassen;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.StateSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import szut.de.statistikapplication.R;

/**
 * Created by Alienware on 26.02.2015.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 110;
    private static final String DATABASE_NAME = "statistikDB.db";
    private Context context;

    private final String TABLE_KATEGORIEN = "kategorien";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_ART = "art";
    private final String COLUMN_SELECTED = "selected";
    private final String COLUMN_POSITION = "position";
    private final String COLUMN_EIGENE = "eigene";
    private final String COLUMN_SPORTART = "sportart";


    private final String TABLE_MANNSCHAFT = "mannschaften";
    //private final String COLUMN_ID = "_id";
    private final String COLUMN_VEREINSNAME = "vorname";
    private final String COLUMN_MANNSCHAFTSNAME = "nachname";
    private final String COLUMN_VEREINLOGO = "vereinslogo";
    //private final String COLUMN_SPORTART = "sportart";


    private final String TABLE_SPIELER = "spieler";
    //private final String COLUMN_ID = "_id";
    private final String COLUMN_MANNSCHAFTSID = "_mannschaftsid";
    private final String COLUMN_VORNAME = "vorname";
    private final String COLUMN_NACHNAME = "nachname";
    private final String COLUMN_TRIKONUMMER = "trikonummer";
    private final String COLUMN_GROESSE = "groesse";
    //private final String COLUMN_POSITION = "position";
    private final String COLUMN_FOTO = "foto";
    //private final String COLUMN_SELECTED = "selected";
    //private final String COLUMN_SPORTART = "sportart";


    private final String TABLE_POSITION = "position";
    //private final String COLUMN_ID = "_id";
    private final String COLUMN_NAMEKURZ = "vorname";
    private final String COLUMN_NAMELANG = "nachname";
    //private final String COLUMN_SPORTART = "sportart";


    private final String TABLE_STATISTIKWERTE = "statistikwerte";
    //private final String COLUMN_ID = "_id";
    private final String COLUMN_STATISTIKID = "_statistikid";
    private final String COLUMN_SPIELERID = "_spielerid";
    private final String COLUMN_KATEGORIEID = "_kategorieid";
    private final String COLUMN_WERT = "wert";


    private final String TABLE_STATISTIK = "statistik";
    //private final String COLUMN_ID = "_id";
    //private final String COLUMN_MANNSCHAFTSID = "_statistikid";
    private final String COLUMN_DATUM = "datum";
    private final String COLUMN_HEIM = "heim";
    private final String COLUMN_GEGNER = "gegner";
    private final String COLUMN_EIGENETORE = "eigenetore";
    private final String COLUMN_GEGNERTORE = "gegnertore";



    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        erzeugeTabellen(db);
        fülleTabellen(db);
    }

    public void erzeugeTabellen(SQLiteDatabase db){
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


        String CREATE_KATEGORIEN_TABLE = "CREATE TABLE " +
                TABLE_KATEGORIEN + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_MANNSCHAFTSID + " INTEGER,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_ART + " TEXT,"
                + COLUMN_FOTO + " BLOB,"
                + COLUMN_SELECTED + " INTEGER,"
                + COLUMN_EIGENE + " INTEGER,"
                + COLUMN_SPORTART + " TEXT"
                + ")";


        String CREATE_MANNSCHAFT_TABLE = "CREATE TABLE " +
                TABLE_MANNSCHAFT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_VEREINSNAME + " TEXT,"
                + COLUMN_MANNSCHAFTSNAME + " TEXT,"
                + COLUMN_VEREINLOGO + " BLOB,"
                + COLUMN_SPORTART + " TEXT"
                + ")";


        String CREATE_POSITION_TABLE = "CREATE TABLE " +
                TABLE_POSITION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAMEKURZ + " TEXT,"
                + COLUMN_NAMELANG + " TEXT,"
                + COLUMN_SPORTART + " TEXT"
                + ")";


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


        String CREATE_STATISTIKWERTE_TABLE = "CREATE TABLE " +
                TABLE_STATISTIKWERTE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_STATISTIKID + " INTEGER,"
                + COLUMN_SPIELERID + " INTEGER,"
                + COLUMN_KATEGORIEID + " INTEGER,"
                + COLUMN_WERT + " TEXT"
                + ")";



        db.execSQL(CREATE_SPIELER_TABLE);
        db.execSQL(CREATE_KATEGORIEN_TABLE);
        db.execSQL(CREATE_MANNSCHAFT_TABLE);
        db.execSQL(CREATE_POSITION_TABLE);
        db.execSQL(CREATE_STATISTIK_TABLE);
        db.execSQL(CREATE_STATISTIKWERTE_TABLE);
    }

    public void fülleTabellen(SQLiteDatabase db){

        //Handball-Einstellungen
        add(new Position("TW", "Torwart", "handball"), db);
        add(new Position("LA", "Linksaußen", "handball"), db);
        add(new Position("RL", "Rückraumlinks", "handball"), db);
        add(new Position("RM", "Rückraummitte", "handball"), db);
        add(new Position("RR", "Rückraumrechts", "handball"), db);
        add(new Position("RA", "Rechtsaußen", "handball"), db);
        add(new Position("KR", "Kreisläufer", "handball"), db);


        add(new Kategorie(0, "Tore", "Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.tor)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Verworfen", "Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.keintor)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Würfe", "Auto-Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.wuerfe)).getBitmap(), "handball"), db);

        add(new Kategorie(0, "7 M.-Tor", "Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.siebenmetertor)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "7 M.-Gehalten", "Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.keinsiebenmetertor)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Fouls", "Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.foul)).getBitmap(), "handball"), db);

        add(new Kategorie(0, "Paraden", "Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.gehalten)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Tempos", "Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.tempos)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Block", "Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.block)).getBitmap(), "handball"), db);

        add(new Kategorie(0, "2-Minuten", "Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.zweiminutenhand)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Gelbe Karte", "Checkbox", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.gelbekarte)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Rote Karte", "Checkbox", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.rotekarte)).getBitmap(), "handball"), db);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPIELER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANNSCHAFT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTIK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTIKWERTE);
        onCreate(db);
    }

    public void add(Object klasse){
        if(klasse instanceof Statistikwerte){
            addStatistikwerte((Statistikwerte) klasse);
        }
        else if(klasse instanceof Statistik){
            addStatistik((Statistik) klasse);
        }
        else if(klasse instanceof Spieler){
            addSpieler((Spieler) klasse);
        }
        else if (klasse instanceof Kategorie){
            addKategorie((Kategorie) klasse);
        }
        else if(klasse instanceof Mannschaft){
            addMannschaft((Mannschaft) klasse);
        }
        else if(klasse instanceof Position){
            addPosition((Position) klasse);
        }
    }

    public void add(Object klasse, SQLiteDatabase db){
        if(klasse instanceof Kategorie){
            addKategorie((Kategorie)klasse, db);
        }
        else if(klasse instanceof Position){
            addPosition((Position) klasse, db);
        }
    }

    public Object find(int id, Object klasse){

        if(klasse instanceof Spieler){
            return findSpieler(id);
        }
        else if (klasse instanceof Kategorie){
            return findKategorie(id);
        }
        else if(klasse instanceof Mannschaft){
            return findMannschaft(id);
        }
        else if(klasse instanceof Position){
            return findPosition(id);
        }

        return null;
    }

    public void update(Object klasse){
        if(klasse instanceof Statistikwerte){
            updateStatistikwerte((Statistikwerte) klasse);
        }
        else if(klasse instanceof Statistik){
            updateStatistik((Statistik) klasse);
        }
        else if(klasse instanceof Spieler){
            updateSpieler((Spieler) klasse);
        }
        else if (klasse instanceof Kategorie){
            updateKategorie((Kategorie) klasse);
        }
        else if(klasse instanceof Mannschaft){
            updateMannschaft((Mannschaft) klasse);
        }
        else if(klasse instanceof Position){
            updatePosition((Position)klasse);
        }
    }

    public ArrayList<? extends Object> findObjectsDerSportart(Object klasse, String sportart){

        if(klasse instanceof Spieler){
            return findSpielerDerSportart(sportart);
        }
        else if (klasse instanceof Kategorie){
            return findKategorienDerSportart(sportart);
        }
        else if(klasse instanceof Mannschaft){
            return findMannschaftenDerSportart(sportart);
        }
        else if(klasse instanceof Position){
            return findPositionenDerSportart(sportart);
        }
        else{
            return null;
        }
    }

    public void delete(Object klasse){
        if(klasse instanceof Statistikwerte){
            deleteStatistikwerte((Statistikwerte) klasse);
        }
        else if(klasse instanceof Statistik){
            deleteStatistik((Statistik) klasse);
        }
        else if(klasse instanceof Spieler){
            deleteSpieler((Spieler) klasse);
        }
        else if (klasse instanceof Kategorie){
            deleteKategorie((Kategorie) klasse);
        }
        else if(klasse instanceof Mannschaft){
            deleteMannschaft((Mannschaft) klasse);
        }
        else if(klasse instanceof Position){
            deletePosition((Position)klasse);
        }
    }




    private void addStatistikwerte(Statistikwerte statistikwerte) {

        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATISTIKID, statistikwerte.getStatistikId());
        values.put(COLUMN_SPIELERID, statistikwerte.getSpielerId());
        values.put(COLUMN_KATEGORIEID, statistikwerte.getKategorieId());
        values.put(COLUMN_WERT, statistikwerte.getWert());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_STATISTIKWERTE, null, values);
        db.close();
    }

    private void updateStatistikwerte(Statistikwerte statistikwerte) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_STATISTIKID, statistikwerte.getStatistikId());
        values.put(COLUMN_SPIELERID, statistikwerte.getSpielerId());
        values.put(COLUMN_KATEGORIEID, statistikwerte.getKategorieId());
        values.put(COLUMN_WERT, statistikwerte.getWert());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_STATISTIKWERTE, values, COLUMN_ID + " = " + statistikwerte.getId(), null);
        db.close();
    }

    private void deleteStatistikwerte(Statistikwerte statistikwerte) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STATISTIKWERTE, COLUMN_ID + " = " + statistikwerte.getId(), null);
        db.close();
    }

    public ArrayList<Statistikwerte> getStatistikwerteDerStatistik(Statistik statistik){

        String query = "Select * FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistik.getId();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

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
        db.close();

        return statistikwerteListe;
    }


    public ArrayList<Statistikwerte> getStatistikwerteDerStatistikDesSpielers(Statistik statistik, Spieler spieler){
        Gson gson = new Gson();
        String query = "Select * FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistik.getId() + " AND " + COLUMN_SPIELERID + " = " + spieler.getId();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

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
        db.close();

        return statistikwerteListe;
    }

    public ArrayList<Spieler> getSpielerDerStatistik(Statistik statistik){

        String query = "Select DISTINCT " + COLUMN_SPIELERID + " FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistik.getId();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Spieler> spielerListe = new ArrayList<Spieler>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int spielerId = Integer.parseInt(cursor.getString(0));
                spielerListe.add(findSpieler(spielerId));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return spielerListe;
    }

    public ArrayList<Kategorie> getKategorienDerStatistik(Statistik statistik){

        String query = "Select DISTINCT " + COLUMN_KATEGORIEID + " FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistik.getId();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Kategorie> kategorienListe = new ArrayList<Kategorie>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int kategorieId = Integer.parseInt(cursor.getString(0));
                kategorienListe.add(findKategorie(kategorieId));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return kategorienListe;
    }


    public Statistikwerte findStatistikwert(int statistikId, int spielerId, int kategorieId) {

        String query = "Select * FROM " + TABLE_STATISTIKWERTE + " WHERE " + COLUMN_STATISTIKID + " = " + statistikId + " AND " + COLUMN_SPIELERID + " = " + spielerId + " AND " + COLUMN_KATEGORIEID + " = " + kategorieId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

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
        db.close();
        return statistikwert;
    }







    private void addStatistik(Statistik statistik) {

        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, statistik.getMannschaftsid());
        values.put(COLUMN_DATUM, statistik.getDatum());
        values.put(COLUMN_HEIM, statistik.getHeim());
        values.put(COLUMN_GEGNER, statistik.getGegner());
        values.put(COLUMN_EIGENETORE, statistik.getEigeneTore());
        values.put(COLUMN_GEGNERTORE, statistik.getGegnerTore());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_STATISTIK, null, values);
        db.close();
    }

    private void updateStatistik(Statistik statistik) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, statistik.getMannschaftsid());
        values.put(COLUMN_DATUM, statistik.getDatum());
        values.put(COLUMN_HEIM, statistik.getHeim());
        values.put(COLUMN_GEGNER, statistik.getGegner());
        values.put(COLUMN_EIGENETORE, statistik.getEigeneTore());
        values.put(COLUMN_GEGNERTORE, statistik.getGegnerTore());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_STATISTIK, values, COLUMN_ID + " = " + statistik.getId(), null);
        db.close();
    }

    private void deleteStatistik(Statistik statistik) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STATISTIK, COLUMN_ID + " = " + statistik.getId(), null);
        db.close();
    }


    public Statistik getLastStatistik(){
        String query = "Select * FROM " + TABLE_STATISTIK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

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
        db.close();

        return statistik;
    }


    public ArrayList<Statistik> getStatistikenDerMannschaft(Mannschaft mannschaft){
        Gson gson = new Gson();
        String query = "Select * FROM " + TABLE_STATISTIK + " WHERE " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

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
        db.close();

        return statistikListe;
    }











    private void addSpieler(Spieler spieler) {

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

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SPIELER, null, values);
        db.close();
    }

    private ArrayList<Spieler> findSpielerDerSportart(String sportart){

        Gson gson = new Gson();
        String query = "Select * FROM " + TABLE_SPIELER + " WHERE " + COLUMN_SPORTART + " =  \"" + sportart + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

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
        db.close();

        return spielerListe;
    }

    private Spieler findSpieler(int id) {
        Gson gson = new Gson();
        String query = "Select * FROM " + TABLE_SPIELER + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

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
        db.close();
        return spieler;
    }

    private void updateSpieler(Spieler spieler) {

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

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_SPIELER, values, COLUMN_ID + " = " + spieler.getId(), null);
        db.close();
    }

    private void deleteSpieler(Spieler spieler) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SPIELER, COLUMN_ID + " = " + spieler.getId(), null);
        db.close();
    }

    public ArrayList<Spieler> getSpielerDerMannschaft(Mannschaft mannschaft){
        Gson gson = new Gson();
        String query = "Select * FROM " + TABLE_SPIELER + " WHERE " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

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
        db.close();

        return spielerListe;
    }


    public ArrayList<Spieler> getAktiveSpielerDerMannschaft(Mannschaft mannschaft){
        Gson gson = new Gson();
        String query = "Select * FROM " + TABLE_SPIELER + " WHERE " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId() + " & " + COLUMN_SELECTED + " = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

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
        db.close();

        return spielerListe;
    }


    private void addPosition(Position position) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMEKURZ, position.getNameKurz());
        values.put(COLUMN_NAMELANG, position.getNameLang());
        values.put(COLUMN_SPORTART, position.getSportart());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_POSITION, null, values);
        db.close();
    }

    private void addPosition(Position position, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMEKURZ, position.getNameKurz());
        values.put(COLUMN_NAMELANG, position.getNameLang());
        values.put(COLUMN_SPORTART, position.getSportart());

        db.insert(TABLE_POSITION, null, values);
    }

    private Position findPosition(int id) {
        String query = "Select * FROM " + TABLE_POSITION + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Position position = new Position();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            position.setId(Integer.parseInt(cursor.getString(0)));
                position.setNameKurz(cursor.getString(1));
                position.setNameLang(cursor.getString(2));
                position.setSportart(cursor.getString(3));

        }

        cursor.close();
        db.close();

        return position;
    }

    private ArrayList<Position> findPositionenDerSportart(String sportart){

        String query = "Select * FROM " + TABLE_POSITION + " WHERE " + COLUMN_SPORTART + " =  \"" + sportart + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

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
        db.close();

        return positionsListe;
    }

    private void updatePosition(Position position) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMEKURZ, position.getNameKurz());
        values.put(COLUMN_NAMELANG, position.getNameLang());
        values.put(COLUMN_SPORTART, position.getSportart());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_POSITION, values, COLUMN_ID + " = " + position.getId(), null);
        db.close();
    }

    private void deletePosition(Position position) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POSITION, COLUMN_ID + " = " + position.getId(), null);
        db.close();
    }


    private void addKategorie(Kategorie kategorie) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, kategorie.getMannschaftsID());
        values.put(COLUMN_NAME, kategorie.getName());
        values.put(COLUMN_ART, kategorie.getArt());

        Bitmap bitmap = kategorie.getFoto();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_FOTO, stream.toByteArray());

        values.put(COLUMN_SELECTED, kategorie.getSelected());
        values.put(COLUMN_EIGENE, kategorie.getEigene());
        values.put(COLUMN_SPORTART, kategorie.getSportart());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_KATEGORIEN, null, values);
        db.close();
    }

    private void addKategorie(Kategorie kategorie, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, kategorie.getMannschaftsID());
        values.put(COLUMN_NAME, kategorie.getName());
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

    private Kategorie findKategorie(int id) {
        String query = "Select * FROM " + TABLE_KATEGORIEN + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Kategorie kategorie = new Kategorie();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            kategorie.setId(Integer.parseInt(cursor.getString(0)));
            kategorie.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
            kategorie.setName(cursor.getString(2));
            kategorie.setArt(cursor.getString(3));

            byte[] blob = cursor.getBlob(4);
            Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            kategorie.setFoto(foto);

            kategorie.setSelected(Integer.parseInt(cursor.getString(5)));
            kategorie.setEigene(Integer.parseInt(cursor.getString(6)));
            kategorie.setSportart(cursor.getString(7));
        }

        cursor.close();
        db.close();

        return kategorie;
    }

    private ArrayList<Kategorie> findKategorienDerSportart(String sportart) {

        String query = "Select * FROM " + TABLE_KATEGORIEN + " WHERE " + COLUMN_SPORTART + " =  \"" + sportart + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Kategorie> kategorienListe = new ArrayList<Kategorie>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Kategorie kategorie = new Kategorie();

                kategorie.setId(Integer.parseInt(cursor.getString(0)));
                kategorie.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                kategorie.setName(cursor.getString(2));
                kategorie.setArt(cursor.getString(3));

                byte[] blob = cursor.getBlob(4);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                kategorie.setFoto(foto);

                kategorie.setSelected(Integer.parseInt(cursor.getString(5)));
                kategorie.setEigene(Integer.parseInt(cursor.getString(6)));
                kategorie.setSportart(cursor.getString(7));

                kategorienListe.add(kategorie);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        return kategorienListe;
    }

    public ArrayList<Kategorie> getKategorienDerMannschaft(Mannschaft mannschaft) {

        String query = "Select * FROM " + TABLE_KATEGORIEN + " WHERE " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId() + " OR " + COLUMN_MANNSCHAFTSID + " = 0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Kategorie> kategorienListe = new ArrayList<Kategorie>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Kategorie kategorie = new Kategorie();

                kategorie.setId(Integer.parseInt(cursor.getString(0)));
                kategorie.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                kategorie.setName(cursor.getString(2));
                kategorie.setArt(cursor.getString(3));

                byte[] blob = cursor.getBlob(4);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                kategorie.setFoto(foto);

                kategorie.setSelected(Integer.parseInt(cursor.getString(5)));
                kategorie.setEigene(Integer.parseInt(cursor.getString(6)));
                kategorie.setSportart(cursor.getString(7));

                kategorienListe.add(kategorie);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        return kategorienListe;
    }


    public ArrayList<Kategorie> getAktiveKategorienDerMannschaft(Mannschaft mannschaft) {

        String query = "Select * FROM " + TABLE_KATEGORIEN + " WHERE ( " + COLUMN_MANNSCHAFTSID + " = " + mannschaft.getId() + " OR " + COLUMN_MANNSCHAFTSID + " = 0 ) AND " + COLUMN_SELECTED + " = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Kategorie> kategorienListe = new ArrayList<Kategorie>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Kategorie kategorie = new Kategorie();

                kategorie.setId(Integer.parseInt(cursor.getString(0)));
                kategorie.setMannschaftsID(Integer.parseInt(cursor.getString(1)));
                kategorie.setName(cursor.getString(2));
                kategorie.setArt(cursor.getString(3));

                byte[] blob = cursor.getBlob(4);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                kategorie.setFoto(foto);

                kategorie.setSelected(Integer.parseInt(cursor.getString(5)));
                kategorie.setEigene(Integer.parseInt(cursor.getString(6)));
                kategorie.setSportart(cursor.getString(7));

                kategorienListe.add(kategorie);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        return kategorienListe;
    }


    private void updateKategorie(Kategorie kategorie) {

        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MANNSCHAFTSID, kategorie.getMannschaftsID());
        values.put(COLUMN_NAME, kategorie.getName());
        values.put(COLUMN_ART, kategorie.getArt());

        Bitmap bitmap = kategorie.getFoto();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_FOTO, stream.toByteArray());

        values.put(COLUMN_SELECTED, kategorie.getSelected());
        values.put(COLUMN_EIGENE, kategorie.getEigene());
        values.put(COLUMN_SPORTART, kategorie.getSportart());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_KATEGORIEN, values, COLUMN_ID + " = " + kategorie.getId(), null);
        db.close();
    }

    private void deleteKategorie(Kategorie kategorie) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KATEGORIEN, COLUMN_ID + " = " + kategorie.getId(), null);
        db.close();
    }

    public Mannschaft createAndGetNewMannschaft(Resources res){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_VEREINSNAME, "");
        values.put(COLUMN_MANNSCHAFTSNAME, "");

        Bitmap bitmap = ((BitmapDrawable)res.getDrawable(R.drawable.defaultvereinslogo)).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_VEREINLOGO, stream.toByteArray());

        values.put(COLUMN_SPORTART, "");

        db.insert(TABLE_MANNSCHAFT, null, values);
        db.close();

        return getLastMannschaft();
    }

    private void addMannschaft(Mannschaft mannschaft) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_VEREINSNAME, mannschaft.getVereinsname());
        values.put(COLUMN_MANNSCHAFTSNAME, mannschaft.getMannschaftsname());

        Bitmap bitmap = mannschaft.getVereinslogo();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_VEREINLOGO, stream.toByteArray());

        values.put(COLUMN_SPORTART, mannschaft.getSportart());


        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_MANNSCHAFT, null, values);
        db.close();
    }

    public Mannschaft findMannschaft(int id){

        String query = "Select * FROM " + TABLE_MANNSCHAFT + " WHERE " + COLUMN_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Mannschaft mannschaft = new Mannschaft();

        if (cursor.moveToFirst()) {
            Gson gson = new Gson();

            mannschaft.setId(Integer.parseInt(cursor.getString(0)));
            mannschaft.setVereinsname(cursor.getString(1));
            mannschaft.setMannschaftsname(cursor.getString(2));

            byte[] blob = cursor.getBlob(3);
            Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            mannschaft.setVereinslogo(foto);

            mannschaft.setSportart(cursor.getString(4));
        }
        else{
            mannschaft = null;
        }

        cursor.close();
        db.close();

        return mannschaft;
    }

    public ArrayList<Mannschaft> findMannschaftenDerSportart(String sportart){

        String query = "Select * FROM " + TABLE_MANNSCHAFT + " WHERE " + COLUMN_SPORTART + " =  \"" + sportart + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Log.d("ANZAHL", String.valueOf(cursor.getCount()));

        ArrayList<Mannschaft> mannschaftsListe = new ArrayList<Mannschaft>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Gson gson = new Gson();

                Mannschaft mannschaft = new Mannschaft();
                mannschaft.setId(Integer.parseInt(cursor.getString(0)));
                mannschaft.setVereinsname(cursor.getString(1));
                mannschaft.setMannschaftsname(cursor.getString(2));

                byte[] blob = cursor.getBlob(3);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                mannschaft.setVereinslogo(foto);

                mannschaft.setSportart(cursor.getString(4));

                mannschaftsListe.add(mannschaft);
                cursor.moveToNext();
            }
        }

        db.close();
        cursor.close();

        return mannschaftsListe;
    }

    private void updateMannschaft(Mannschaft mannschaft) {

        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VEREINSNAME, mannschaft.getVereinsname());
        values.put(COLUMN_MANNSCHAFTSNAME, mannschaft.getMannschaftsname());

        Bitmap bitmap = mannschaft.getVereinslogo();
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            values.put(COLUMN_VEREINLOGO, stream.toByteArray());
        }

        values.put(COLUMN_SPORTART, mannschaft.getSportart());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_MANNSCHAFT, values, COLUMN_ID + " = " + mannschaft.getId(), null);
        db.close();
    }

    private void deleteMannschaft(Mannschaft mannschaft) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MANNSCHAFT, COLUMN_ID + " = " + mannschaft.getId(), null);
        db.close();
    }

    public Mannschaft getLastMannschaft(){

        String query = "Select * FROM " + TABLE_MANNSCHAFT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Mannschaft mannschaft = new Mannschaft();

        if(cursor!=null && cursor.getCount() > 0) {
            Gson gson = new Gson();
            cursor.moveToLast();


            mannschaft.setId(Integer.parseInt(cursor.getString(0)));
            mannschaft.setVereinsname(cursor.getString(1));
            mannschaft.setMannschaftsname(cursor.getString(2));

            byte[] blob = cursor.getBlob(3);
            Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            mannschaft.setVereinslogo(foto);

            mannschaft.setSportart(cursor.getString(4));
        }
        cursor.close();
        db.close();

        return mannschaft;
    }

    public ArrayList<Mannschaft> getAllMannschaften(){
        String query = "Select * FROM " + TABLE_MANNSCHAFT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Mannschaft> mannschaftsListe = new ArrayList<Mannschaft>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Gson gson = new Gson();

                Mannschaft mannschaft = new Mannschaft();
                mannschaft.setId(Integer.parseInt(cursor.getString(0)));
                mannschaft.setVereinsname(cursor.getString(1));
                mannschaft.setMannschaftsname(cursor.getString(2));

                byte[] blob = cursor.getBlob(3);
                Bitmap foto = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                mannschaft.setVereinslogo(foto);

                mannschaft.setSportart(cursor.getString(4));

                mannschaftsListe.add(mannschaft);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return mannschaftsListe;
    }

}
