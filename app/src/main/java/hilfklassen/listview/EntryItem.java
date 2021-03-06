package hilfklassen.listview;

import android.graphics.Bitmap;

import database.data.Mannschaft;

/**
 * Created by roese on 17.02.2015.
 */
public class EntryItem implements Item{

    public int id = 0;
    public Bitmap picture;
    public String title ="";
    public String subtitle = "";

    public EntryItem(Mannschaft mannschaft) {

        this.id = mannschaft.getId();
        this.picture = mannschaft.getVereinslogo();
        this.title = mannschaft.getVereinsname();
        this.subtitle = mannschaft.getMannschaftsname();
    }

    public EntryItem(String text){
        this.title = text;
    }

    @Override
    public boolean isSection() {
        return false;
    }

}