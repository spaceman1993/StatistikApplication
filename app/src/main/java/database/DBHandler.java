package database;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import database.data.Kategorie;
import database.data.Mannschaft;
import database.data.Position;
import database.data.Spieler;
import database.data.Statistik;
import database.data.Statistikwerte;
import database.sql_statements.SQL_Kategorien;
import database.sql_statements.SQL_Mannschaft;
import database.sql_statements.SQL_Position;
import database.sql_statements.SQL_Spieler;
import database.sql_statements.SQL_Statistiken;
import database.sql_statements.SQL_Statistikwerte;
import szut.de.statistikapplication.R;

/**
 * Created by Alienware on 26.02.2015.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 126;
    private static final String DATABASE_NAME = "statistikDB.db";
    private Context context;

    private SQL_Spieler spieler;
    private SQL_Kategorien kategorien;
    private SQL_Mannschaft mannschaft;
    private SQL_Position positionen;
    private SQL_Statistiken statistiken;
    private SQL_Statistikwerte statistikwerte;

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;

        if(spieler == null){
            spieler = new SQL_Spieler(this);
            kategorien = new SQL_Kategorien(this);
            mannschaft = new SQL_Mannschaft(this);
            positionen = new SQL_Position(this);
            statistiken = new SQL_Statistiken(this);
            statistikwerte = new SQL_Statistikwerte(this);
        }
        else{
            spieler.setDB(this);
            kategorien.setDB(this);
            mannschaft.setDB(this);
            positionen.setDB(this);
            statistiken.setDB(this);
            statistikwerte.setDB(this);
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        if(spieler == null) {
            spieler = new SQL_Spieler(db);
            kategorien = new SQL_Kategorien(db);
            mannschaft = new SQL_Mannschaft(db);
            positionen = new SQL_Position(db);
            statistiken = new SQL_Statistiken(db);
            statistikwerte = new SQL_Statistikwerte(db);
        }

        spieler.createTable();
        kategorien.createTable();
        mannschaft.createTable();
        positionen.createTable();
        statistiken.createTable();
        statistikwerte.createTable();

        fülleTabellen(db);
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


        add(new Kategorie(0, "Tore", "Angriff", "Zähler", 1, 1, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.tor)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Verworfen", "Angriff", "Zähler", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.keintor)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Würfe", "Angriff", "Auto-Zähler", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.wuerfe)).getBitmap(), "handball"), db);

        add(new Kategorie(0, "7 M.-Tor", "Fouls", "Zähler", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.siebenmetertor)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "7 M.-Gehalten", "Fouls", "Zähler", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.keinsiebenmetertor)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Fouls", "Fouls", "Zähler", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.foul)).getBitmap(), "handball"), db);

        add(new Kategorie(0, "Paraden", "Verteidigung", "Zähler", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.gehalten)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Tempos", "Weitere", "Zähler", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.tempos)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Block", "Verteidigung", "Zähler", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.block)).getBitmap(), "handball"), db);

        add(new Kategorie(0, "2-Minuten", "Fouls", "Zähler", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.zweiminutenhand)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Gelbe Karte", "Fouls", "Checkbox", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.gelbekarte)).getBitmap(), "handball"), db);
        add(new Kategorie(0, "Rote Karte", "Fouls", "Checkbox", 1, 0, ((BitmapDrawable)context.getResources().getDrawable(R.drawable.rotekarte)).getBitmap(), "handball"), db);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(spieler == null) {
            spieler = new SQL_Spieler(db);
            kategorien = new SQL_Kategorien(db);
            mannschaft = new SQL_Mannschaft(db);
            positionen = new SQL_Position(db);
            statistiken = new SQL_Statistiken(db);
            statistikwerte = new SQL_Statistikwerte(db);
        }

        spieler.deleteTable();
        kategorien.deleteTable();
        mannschaft.deleteTable();
        positionen.deleteTable();
        statistiken.deleteTable();
        statistikwerte.deleteTable();

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

        if(klasse instanceof Statistik){
            return findStatistik(id);
        }
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



    //----------------------------------------------------------------------------------------------
    //---------STATISTIKWERTE-STATEMENTS------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private void addStatistikwerte(Statistikwerte statistikwerte) {
        this.statistikwerte.add(statistikwerte);
    }

    private void updateStatistikwerte(Statistikwerte statistikwerte) {
        this.statistikwerte.update(statistikwerte);
    }

    private void deleteStatistikwerte(Statistikwerte statistikwerte) {
        this.statistikwerte.delete(statistikwerte);
    }

    public ArrayList<Statistikwerte> getStatistikwerteDerStatistik(Statistik statistik){
        return this.statistikwerte.findByStatistik(statistik);
    }

    public ArrayList<Statistikwerte> getStatistikwerteDerStatistikDesSpielers(Statistik statistik, Spieler spieler){
        return this.statistikwerte.findByStatistikOfSpieler(statistik, spieler);
    }

    public Statistikwerte findStatistikwert(int statistikId, int spielerId, int kategorieId) {
        return this.statistikwerte.findById(statistikId, spielerId, kategorieId);
    }

    public int getToreByStatistik(Statistik statistik){
        return this.statistikwerte.getToreByStatistik(statistik);
    }

    public String getGesamtwertOfKategorieFromSpieler(Spieler spieler, Kategorie kategorie){
        return this.statistikwerte.getGesamtwertOfKategorieOfSpieler(spieler, kategorie);
    }

    //----------------------------------------------------------------------------------------------
    //---------STATISTIK-STATEMENTS-----------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private void addStatistik(Statistik statistik) {
        statistiken.add(statistik);
    }

    private void updateStatistik(Statistik statistik) {
        statistiken.update(statistik);
    }

    private void deleteStatistik(Statistik statistik) {
        statistiken.delete(statistik);
    }

    private Statistik findStatistik(int id) {
        return this.statistiken.findById(id);
    }

    public Statistik getLastStatistik(){
        return statistiken.getLastId();
    }

    public ArrayList<Statistik> getStatistikenDerMannschaft(Mannschaft mannschaft){
        return statistiken.findByMannschaft(mannschaft);
    }

    public ArrayList<String> getStatistikübersichtDerMannschaft(Mannschaft mannschaft){
        return statistiken.getÜbersichtDerMannschaft(mannschaft);
    }


    //----------------------------------------------------------------------------------------------
    //---------SPIELER-STATEMENTS-------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private void addSpieler(Spieler spieler) {
        this.spieler.add(spieler);
    }

    private ArrayList<Spieler> findSpielerDerSportart(String sportart){
        return this.spieler.findBySportart(sportart);
    }

    private Spieler findSpieler(int id) {
       return this.spieler.findById(id);
    }

    private void updateSpieler(Spieler spieler) {
        this.spieler.update(spieler);
    }

    private void deleteSpieler(Spieler spieler) {
        this.spieler.delete(spieler);
    }

    public ArrayList<Spieler> getSpielerDerMannschaft(Mannschaft mannschaft){
        return this.spieler.findByMannschaft(mannschaft);
    }


    public ArrayList<Spieler> getAktiveSpielerDerMannschaft(Mannschaft mannschaft){
        return this.spieler.findByMannschaftAndAktiv(mannschaft);
    }

    public ArrayList<Spieler> getSpielerDerStatistik(Statistik statistik){
        return this.spieler.findByStatistik(statistik);
    }


    //----------------------------------------------------------------------------------------------
    //---------POSITIONS-STATEMENTS-----------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private void addPosition(Position position) {
        positionen.add(position);
    }

    private void addPosition(Position position, SQLiteDatabase db) {
        positionen.add(position, db);
    }

    private Position findPosition(int id) {
        return positionen.findById(id);
    }

    private ArrayList<Position> findPositionenDerSportart(String sportart){
        return positionen.findBySportart(sportart);
    }

    private void updatePosition(Position position) {
        positionen.update(position);
    }

    private void deletePosition(Position position) {
        positionen.deletePosition(position);
    }



    //----------------------------------------------------------------------------------------------
    //---------KATEGORIEN-STATEMENTS----------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private void addKategorie(Kategorie kategorie) {
        kategorien.add(kategorie);
    }

    private void addKategorie(Kategorie kategorie, SQLiteDatabase db) {
        kategorien.add(kategorie, db);
    }

    private Kategorie findKategorie(int id) {
       return kategorien.findById(id);
    }

    private ArrayList<Kategorie> findKategorienDerSportart(String sportart) {
        return kategorien.findBySportart(sportart);
    }

    public ArrayList<Kategorie> getKategorienDerMannschaft(Mannschaft mannschaft) {
        return kategorien.findByMannschaft(mannschaft);
    }

    public ArrayList<Kategorie> getAktiveKategorienDerMannschaft(Mannschaft mannschaft) {
        return kategorien.findByMannschaftAndAktiv(mannschaft);
    }

    private void updateKategorie(Kategorie kategorie) {
        kategorien.update(kategorie);
    }

    private void deleteKategorie(Kategorie kategorie) {
        kategorien.delete(kategorie);
    }

    public ArrayList<Kategorie> getKategorienDerStatistik(Statistik statistik){
        return kategorien.findByStatistik(statistik);
    }

    public ArrayList<ArrayList<Kategorie>> getAllKategorienarten(){
        return kategorien.findByKategorieartAll();
    }

    //----------------------------------------------------------------------------------------------
    //---------MANNSCHAFTS-STATEMENTS---------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    public Mannschaft createAndGetNewMannschaft(Resources res){
        return this.mannschaft.createAndGetNewMannschaft(res);
    }

    private void addMannschaft(Mannschaft mannschaft) {
        this.mannschaft.add(mannschaft);
    }

    public Mannschaft findMannschaft(int id){
        return this.mannschaft.findById(id);
    }

    public ArrayList<Mannschaft> findMannschaftenDerSportart(String sportart){
        return this.mannschaft.findBySportart(sportart);
    }

    private void updateMannschaft(Mannschaft mannschaft) {
        this.mannschaft.update(mannschaft);
    }

    private void deleteMannschaft(Mannschaft mannschaft) {
        this.mannschaft.delete(mannschaft);
    }

    public Mannschaft getLastMannschaft(){
        return this.mannschaft.getLastId();
    }

    public ArrayList<Mannschaft> getAllMannschaften(){
        return this.mannschaft.getAll();
    }

}
