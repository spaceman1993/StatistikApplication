package widgets.table;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hilfklassen.Kategorie;
import hilfklassen.Spieler;
import hilfklassen.Statistikwerte;
import szut.de.statistikapplication.R;

public class DataObject {

    private Activity activity;

    private Spieler spieler;
    private ArrayList<Statistikwerte> statistikwerte;

    private ArrayList<View> dataviews = new ArrayList<>();



    public DataObject(Activity activity, Spieler spieler, ArrayList<Statistikwerte> statistikwerte){

        this.activity = activity;

        this.spieler = spieler;
        this.statistikwerte = statistikwerte;

        initDataViews();
    }

    public void initDataViews(){
        dataviews.add(getSpielerView(spieler));

        for(int i=0; i<statistikwerte.size(); i++){
            dataviews.add(getDataView(statistikwerte.get(i)));
        }
    }

    public View getSpielerView(Spieler spieler){
        View v = activity.findViewById(R.id.table_spieler);

        TextView trikonummer = (TextView) v.findViewById(R.id.trikonummerErfassung);
        ImageView foto = (ImageView) v.findViewById(R.id.image_view);
        TextView nachname = (TextView) v.findViewById(R.id.nachname);
        TextView vorname = (TextView) v.findViewById(R.id.vorname);

        trikonummer.setText(spieler.getTrikonummer());
        foto.setImageBitmap(spieler.getFoto());
        nachname.setText(spieler.getNachname());
        vorname.setText(spieler.getVorname());

        return v;
    }

    public View getDataView(Statistikwerte statistikwert){
        View v = null;

        return v;
    }

    public ArrayList<View> getDataviews() {
        return dataviews;
    }
}