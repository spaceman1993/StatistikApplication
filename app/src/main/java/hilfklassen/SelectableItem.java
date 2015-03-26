package hilfklassen;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roese on 20.02.2015.
 */
public abstract class SelectableItem {

    protected int id;
    protected String text1;
    protected String text2;
    protected Bitmap foto;
    protected int selected;

    public SelectableItem(){

    }

    public SelectableItem(int selected, String text1, String text2, Bitmap icon){

        this.selected = selected;
        this.text1 = text1;
        this.text2 = text2;
        this.foto = icon;
    }



    public SelectableItem(int id, int selected, String text1, String text2, Bitmap icon){
        this.id = id;
        this.selected = selected;
        this.text1 = text1;
        this.text2 = text2;
        this.foto = icon;
    }

    public int getId(){
        return id;
    }

    public String getText1(){
        return text1;
    }

    public String getText2(){
        return text2;
    }

    public int getSelected(){
        return selected;
    }

    public Bitmap getFoto() { return foto; }

    public void setId(int id) {
        this.id = id;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setFoto(Bitmap icon) { this.foto = icon; }

}