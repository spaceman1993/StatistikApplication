package widgets.table;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hilfklassen.Kategorie;
import hilfklassen.Spieler;
import hilfklassen.Statistikwerte;
import szut.de.statistikapplication.R;

/**
 * Created by roese on 26.03.2015.
 */
public class HeaderObjects {

    private Activity activity;

    private ArrayList<Kategorie> kategorien;

    private ArrayList<View> headerViews = new ArrayList<>();



    public HeaderObjects(Activity activity, ArrayList<Kategorie> kategorien){

        this.activity = activity;
        this.kategorien = kategorien;

        initHeaderViews();
    }



    public void initHeaderViews(){

        TextView firstHeader = new TextView(activity.getApplicationContext());
        firstHeader.setText("SPIELER");

        headerViews.add(firstHeader);

        for(int i=0; i<kategorien.size(); i++){
            headerViews.add(getKategorienView(kategorien.get(i)));
        }
    }

    public View getKategorienView(Kategorie kategorie){
        View v = activity.findViewById(R.id.table_kategorien);

        ImageView icon = (ImageView) v.findViewById(R.id.image_view);
        TextView kategoriename = (TextView) v.findViewById(R.id.kategorie);

        icon.setImageBitmap(kategorie.getFoto());
        kategoriename.setText(kategorie.getName());

        return v;
    }

    public ArrayList<View> getHeaderViews() {
        return headerViews;
    }
}
