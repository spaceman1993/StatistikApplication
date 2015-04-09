package database.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roese on 13.02.2015.
 */
public class Mannschaft implements Parcelable {

    private int id;
    private String vereinsname;
    private String mannschaftsname;
    private Bitmap vereinslogo;
    private String sportart;

    public Mannschaft(){

    }

    public Mannschaft(String vereinsname, String mannschaftsname, Bitmap vereinslogo, String sportart){
        this.vereinsname = vereinsname;
        this.mannschaftsname = mannschaftsname;
        this.vereinslogo = vereinslogo;
        this.sportart = sportart;
    }

    public Mannschaft(int id, String vereinsname, String mannschaftsname, Bitmap vereinslogo, String sportart){
        this.id = id;
        this.vereinsname = vereinsname;
        this.mannschaftsname = mannschaftsname;
        this.vereinslogo = vereinslogo;
        this.sportart = sportart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVereinsname() {
        return vereinsname;
    }

    public void setVereinsname(String vereinsname) {
        this.vereinsname = vereinsname;
    }

    public String getMannschaftsname() {
        return mannschaftsname;
    }

    public void setMannschaftsname(String mannschaftsname) {
        this.mannschaftsname = mannschaftsname;
    }

    public Bitmap getVereinslogo() {
        return vereinslogo;
    }

    public void setVereinslogo(Bitmap vereinslogo) {
        this.vereinslogo = vereinslogo;
    }

    public String getSportart() {
        return sportart;
    }

    public void setSportart(String sportart) {
        this.sportart = sportart;
    }

    protected Mannschaft(Parcel in) {
        id = in.readInt();
        vereinsname = in.readString();
        mannschaftsname = in.readString();
        vereinslogo = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        sportart = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(vereinsname);
        dest.writeString(mannschaftsname);
        dest.writeValue(vereinslogo);
        dest.writeString(sportart);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Mannschaft> CREATOR = new Parcelable.Creator<Mannschaft>() {
        @Override
        public Mannschaft createFromParcel(Parcel in) {
            return new Mannschaft(in);
        }

        @Override
        public Mannschaft[] newArray(int size) {
            return new Mannschaft[size];
        }
    };
}