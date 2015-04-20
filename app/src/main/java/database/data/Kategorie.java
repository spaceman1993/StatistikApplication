package database.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Kategorie extends SelectableItem implements Parcelable {

    private int mannschaftsID;
	private String name;
    private String kategorienart;
    private String art;
	private int eigene;
    private String sportart;

    public Kategorie(){

    }

    public Kategorie(int mannschaftsID, String name, String kategorienart, String art, int selected, int eigene, Bitmap foto, String sportart) {
        this.mannschaftsID = mannschaftsID;
        this.name = name;
        this.kategorienart = kategorienart;
        this.art = art;
        this.selected = selected;
        this.eigene = eigene;
        this.foto = foto;
        this.sportart = sportart;

    }

	public Kategorie(int id, int mannschaftsID, String name, String kategorienart, String art, int selected, int eigene, Bitmap foto, String sportart) {
        this.id = id;
        this.mannschaftsID = mannschaftsID;
		this.name = name;
        this.kategorienart = kategorienart;
        this.art = art;
        this.selected = selected;
		this.eigene = eigene;
        this.foto = foto;
        this.sportart = sportart;

	}

    public int getMannschaftsID() {
        return mannschaftsID;
    }

    public void setMannschaftsID(int mannschaftsID) {
        this.mannschaftsID = mannschaftsID;
    }

    public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

    public String getKategorienart() {
        return kategorienart;
    }

    public void setKategorienart(String kategorienart) {
        this.kategorienart = kategorienart;
    }

    public String getArt() { return art; }

    public void setArt(String art) { this.art = art; }

    public int getEigene() { return eigene; }

    public void setEigene(int eigene) { this.eigene = eigene; }

    public Bitmap getFoto() { return foto; }

    public void setFoto(Bitmap foto) { this.foto = foto; }

    public String getSportart() { return sportart; }

    public void setSportart(String sportart) { this.sportart = sportart; }

    @Override
    public String getText1(){ return getName(); }

    @Override
    public String getText2(){ return ""; }



    protected Kategorie(Parcel in) {
        mannschaftsID = in.readInt();
        name = in.readString();
        art = in.readString();
        kategorienart = in.readString();
        eigene = in.readInt();
        sportart = in.readString();
        id = in.readInt();
        text1 = in.readString();
        text2 = in.readString();
        foto = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        selected = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mannschaftsID);
        dest.writeString(name);
        dest.writeString(kategorienart);
        dest.writeString(art);
        dest.writeInt(eigene);
        dest.writeString(sportart);
        dest.writeInt(id);
        dest.writeString(text1);
        dest.writeString(text2);
        dest.writeValue(foto);
        dest.writeInt(selected);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Kategorie> CREATOR = new Parcelable.Creator<Kategorie>() {
        @Override
        public Kategorie createFromParcel(Parcel in) {
            return new Kategorie(in);
        }

        @Override
        public Kategorie[] newArray(int size) {
            return new Kategorie[size];
        }
    };
}
