package szut.de.statistikapplication.createStatiActivities;

import android.graphics.Bitmap;
import android.widget.ListAdapter;

/**
 * Created by roese on 04.03.2015.
 */
public class Erfassungsaufbau {

    private int trikonummer;
    private Bitmap picture;
    private ListAdapter kategorienListe;

    public Erfassungsaufbau(int trikonummer, Bitmap picture, ListAdapter kategorienListe){
        this.trikonummer = trikonummer;
        this.picture = picture;
        this.kategorienListe = kategorienListe;
    }

    public int getTrikonummer() {
        return trikonummer;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public ListAdapter getKategorienListe() {
        return kategorienListe;
    }
}
