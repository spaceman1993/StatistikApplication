package database.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roese on 06.03.2015.
 */
public class Position implements Parcelable{

    private int id;
    private String nameKurz;
    private String nameLang;
    private String sportart;


    public Position(){

    }

    public Position(String nameKurz, String nameLang, String sportart){
        this.nameKurz = nameKurz;
        this.nameLang = nameLang;
        this.sportart = sportart;
    }

    public Position(int id, String nameKurz, String nameLang, String sportart){
        this.id = id;
        this.nameKurz = nameKurz;
        this.nameLang = nameLang;
        this.sportart = sportart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameKurz() {
        return nameKurz;
    }

    public void setNameKurz(String nameKurz) {
        this.nameKurz = nameKurz;
    }

    public String getNameLang() {
        return nameLang;
    }

    public void setNameLang(String nameLang) {
        this.nameLang = nameLang;
    }

    public String getSportart() {
        return sportart;
    }

    public void setSportart(String sportart) {
        this.sportart = sportart;
    }

    protected Position(Parcel in) {
        id = in.readInt();
        nameKurz = in.readString();
        nameLang = in.readString();
        sportart = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nameKurz);
        dest.writeString(nameLang);
        dest.writeString(sportart);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
        @Override
        public Position createFromParcel(Parcel in) {
            return new Position(in);
        }

        @Override
        public Position[] newArray(int size) {
            return new Position[size];
        }
    };
}
