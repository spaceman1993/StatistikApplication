package database.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roese on 24.03.2015.
 */
public class Statistik extends SelectableItem implements Parcelable {

    private int mannschaftsid;
    private String datum;
    private int heim;
    private String gegner;
    private int eigeneTore;
    private int gegnerTore;
    protected int id;
    protected String text1;
    protected String text2;
    protected Bitmap foto;
    protected int selected;

    public Statistik(){

    }

    public Statistik(int mannschaftsid, String datum, int heim, String gegner, int eigeneTore, int gegnerTore){
        this.mannschaftsid = mannschaftsid;
        this.datum = datum;
        this.heim = heim;
        this.gegner = gegner;
        this.eigeneTore = eigeneTore;
        this.gegnerTore = gegnerTore;
    }

    public Statistik(int id, int mannschaftsid, String datum, int heim, String gegner, int eigeneTore, int gegnerTore){
        this.id = id;
        this.mannschaftsid = mannschaftsid;
        this.datum = datum;
        this.heim = heim;
        this.gegner = gegner;
        this.eigeneTore = eigeneTore;
        this.gegnerTore = gegnerTore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMannschaftsid() {
        return mannschaftsid;
    }

    public void setMannschaftsid(int mannschaftsid) {
        this.mannschaftsid = mannschaftsid;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getHeim() {
        return heim;
    }

    public void setHeim(int heim) {
        this.heim = heim;
    }

    public String getGegner() {
        return gegner;
    }

    public void setGegner(String gegner) {
        this.gegner = gegner;
    }

    public int getEigeneTore() {
        return eigeneTore;
    }

    public void setEigeneTore(int eigeneTore) {
        this.eigeneTore = eigeneTore;
    }

    public int getGegnerTore() {
        return gegnerTore;
    }

    public void setGegnerTore(int gegnerTore) {
        this.gegnerTore = gegnerTore;
    }

    protected Statistik(Parcel in) {
        mannschaftsid = in.readInt();
        datum = in.readString();
        heim = in.readInt();
        gegner = in.readString();
        eigeneTore = in.readInt();
        gegnerTore = in.readInt();
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
        dest.writeInt(mannschaftsid);
        dest.writeString(datum);
        dest.writeInt(heim);
        dest.writeString(gegner);
        dest.writeInt(eigeneTore);
        dest.writeInt(gegnerTore);
        dest.writeInt(id);
        dest.writeString(text1);
        dest.writeString(text2);
        dest.writeValue(foto);
        dest.writeInt(selected);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Statistik> CREATOR = new Parcelable.Creator<Statistik>() {
        @Override
        public Statistik createFromParcel(Parcel in) {
            return new Statistik(in);
        }

        @Override
        public Statistik[] newArray(int size) {
            return new Statistik[size];
        }
    };
}