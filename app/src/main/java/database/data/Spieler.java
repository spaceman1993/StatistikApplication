package database.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by roese on 10.02.2015.
 */

public class Spieler extends SelectableItem implements Parcelable{

    private int mannschaftsID;
    private String vorname;
    private String nachname;
    private int trikonummer;
    private int groesse;
    private ArrayList<Position> position;
    private String sportart;

    public Spieler(){
    }

    public Spieler(int id, int mannschaftsID, String vorname, String nachname, int trikonummer, int groesse, ArrayList<Position> position, Bitmap foto, int selected, String sportart){

        this.id = id;
        this.mannschaftsID = mannschaftsID;
        this.vorname = vorname;
        this.nachname = nachname;
        this.trikonummer = trikonummer;
        this.groesse = groesse;
        this.position = position;
        this.foto = foto;
        this.selected = selected;
        this.sportart = sportart;

    }

    public Spieler(int mannschaftsID, String vorname, String nachname, int trikonummer, int groesse, ArrayList<Position> position, Bitmap foto, int selected, String sportart){
        this.mannschaftsID = mannschaftsID;
        this.vorname = vorname;
        this.nachname = nachname;
        this.trikonummer = trikonummer;
        this.groesse = groesse;
        this.position = position;
        this.foto = foto;
        this.selected = selected;
        this.sportart = sportart;

    }

    public int getMannschaftsID() {
        return mannschaftsID;
    }

    public void setMannschaftsID(int mannschaftsID) {
        this.mannschaftsID = mannschaftsID;
    }

    public String getVorname() { return vorname; }

    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }

    public void setNachname(String nachname) { this.nachname = nachname; }

    public int getTrikonummer() { return trikonummer; }

    public void setTrikonummer(int trikonummer) { this.trikonummer = trikonummer; }

    public int getGroesse() { return groesse; }

    public void setGroesse(int groesse) { this.groesse = groesse; }

    public ArrayList<Position> getPosition() { return position; }

    public void setPosition(ArrayList<Position> position) { this.position = position; }

    public Bitmap getFoto() { return foto; }

    public void setFoto(Bitmap foto) { this.foto = foto; }

    public String getSportart() { return sportart; }

    public void setSportart(String sportart) { this.sportart = sportart; }

    public int getSelected() { return selected; }

    public void setSelected(int selected) { this.selected = selected; }

    @Override
    public String getText1(){ return getVorname(); }

    @Override
    public String getText2(){ return getNachname(); }

    public int getId(){
        return id;
    }


    protected Spieler(Parcel in) {
        mannschaftsID = in.readInt();
        id = in.readInt();
        text1 = in.readString();
        text2 = in.readString();
        foto = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        selected = in.readInt();
        vorname = in.readString();
        nachname = in.readString();
        trikonummer = in.readInt();
        groesse = in.readInt();
        if (in.readByte() == 0x01) {
            position = new ArrayList<Position>();
            in.readList(position, Position.class.getClassLoader());
        } else {
            position = null;
        }
        sportart = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mannschaftsID);
        dest.writeInt(id);
        dest.writeString(text1);
        dest.writeString(text2);
        dest.writeValue(foto);
        dest.writeInt(selected);
        dest.writeString(vorname);
        dest.writeString(nachname);
        dest.writeInt(trikonummer);
        dest.writeInt(groesse);
        if (position == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(position);
        }
        dest.writeString(sportart);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Spieler> CREATOR = new Parcelable.Creator<Spieler>() {
        @Override
        public Spieler createFromParcel(Parcel in) {
            return new Spieler(in);
        }

        @Override
        public Spieler[] newArray(int size) {
            return new Spieler[size];
        }
    };

}