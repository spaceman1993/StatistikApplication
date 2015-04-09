package database.sql_statements;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import database.data.Mannschaft;
import database.data.Position;
import szut.de.statistikapplication.R;

/**
 * Created by Spaceman on 01.04.2015.
 */
public class SQL_Mannschaft {

    private final String TABLE_MANNSCHAFT = "mannschaften";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_VEREINSNAME = "vereinsname";
    private final String COLUMN_MANNSCHAFTSNAME = "mannschaftsname";
    private final String COLUMN_VEREINLOGO = "vereinslogo";
    private final String COLUMN_SPORTART = "sportart";

    private SQLiteDatabase dbW;
    private SQLiteDatabase dbR;

    public SQL_Mannschaft(SQLiteOpenHelper db){
        this.dbW = db.getWritableDatabase();
        this.dbR = db.getReadableDatabase();
    }

    public SQL_Mannschaft(SQLiteDatabase dbW){
        this.dbW = dbW;
    }

    public void createTable(){


        String CREATE_MANNSCHAFT_TABLE = "CREATE TABLE " +
                TABLE_MANNSCHAFT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_VEREINSNAME + " TEXT,"
                + COLUMN_MANNSCHAFTSNAME + " TEXT,"
                + COLUMN_VEREINLOGO + " BLOB,"
                + COLUMN_SPORTART + " TEXT"
                + ")";

        dbW.execSQL(CREATE_MANNSCHAFT_TABLE);
    }

    public void deleteTable(){
        dbW.execSQL("DROP TABLE IF EXISTS " + TABLE_MANNSCHAFT);
    }

    public Mannschaft createAndGetNewMannschaft(Resources res){
        ContentValues values = new ContentValues();

        values.put(COLUMN_VEREINSNAME, "");
        values.put(COLUMN_MANNSCHAFTSNAME, "");

        Bitmap bitmap = ((BitmapDrawable)res.getDrawable(R.drawable.defaultvereinslogo)).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_VEREINLOGO, stream.toByteArray());

        values.put(COLUMN_SPORTART, "");

        dbW.insert(TABLE_MANNSCHAFT, null, values);

        return getLastId();
    }

    public void add(Mannschaft mannschaft) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_VEREINSNAME, mannschaft.getVereinsname());
        values.put(COLUMN_MANNSCHAFTSNAME, mannschaft.getMannschaftsname());

        Bitmap bitmap = mannschaft.getVereinslogo();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(COLUMN_VEREINLOGO, stream.toByteArray());

        values.put(COLUMN_SPORTART, mannschaft.getSportart());

        dbW.insert(TABLE_MANNSCHAFT, null, values);
    }

    public Mannschaft findById(int id){

        String query = "Select * FROM " + TABLE_MANNSCHAFT + " WHERE " + COLUMN_ID + " = " + id;

        Cursor cursor = dbR.rawQuery(query, null);

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

        return mannschaft;
    }

    public ArrayList<Mannschaft> findBySportart(String sportart){

        String query = "Select * FROM " + TABLE_MANNSCHAFT + " WHERE " + COLUMN_SPORTART + " =  \"" + sportart + "\"";

        Cursor cursor = dbR.rawQuery(query, null);

        Log.d("ANZAHL", String.valueOf(cursor.getCount()));

        ArrayList<Mannschaft> mannschaftsListe = new ArrayList<Mannschaft>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
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

        return mannschaftsListe;
    }

    public void update(Mannschaft mannschaft) {

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

        dbW.update(TABLE_MANNSCHAFT, values, COLUMN_ID + " = " + mannschaft.getId(), null);
    }

    public void delete(Mannschaft mannschaft) {
        dbW.delete(TABLE_MANNSCHAFT, COLUMN_ID + " = " + mannschaft.getId(), null);
    }

    public Mannschaft getLastId(){

        String query = "Select * FROM " + TABLE_MANNSCHAFT;

        Cursor cursor = dbR.rawQuery(query, null);

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

        return mannschaft;
    }

    public ArrayList<Mannschaft> getAll(){
        String query = "Select * FROM " + TABLE_MANNSCHAFT;

        Cursor cursor = dbR.rawQuery(query, null);

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

        return mannschaftsListe;
    }

    public void setDB(SQLiteOpenHelper db){
        this.dbR = db.getReadableDatabase();
        this.dbW = db.getWritableDatabase();
    }
}
