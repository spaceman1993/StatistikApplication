package widgets.table;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
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
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.table_spieler, null);

        TextView trikonummer = (TextView) v.findViewById(R.id.trikonummerErfassung);
        ImageView foto = (ImageView) v.findViewById(R.id.image_view);
        TextView nachname = (TextView) v.findViewById(R.id.nachname);
        TextView vorname = (TextView) v.findViewById(R.id.vorname);

        trikonummer.setText(String.valueOf(spieler.getTrikonummer()));
        foto.setImageBitmap(spieler.getFoto());
        nachname.setText(spieler.getNachname());
        vorname.setText(spieler.getVorname());

        v.setBackgroundResource(R.drawable.tableheader);
        v.setPadding(v.getPaddingLeft()+100, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());

        return v;
    }

    public View getDataView(Statistikwerte statistikwert){
        TextView t = new TextView(activity);
        t.setText(statistikwert.getWert());
        View v = t;

        v.setBackgroundResource(R.drawable.tabledata);

        return v;
    }

    public ArrayList<View> getDataviews() {
        return dataviews;
    }
}