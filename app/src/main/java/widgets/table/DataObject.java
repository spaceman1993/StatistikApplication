package widgets.table;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import database.data.Spieler;
import database.data.Statistikwerte;
import szut.de.statistikapplication.R;
import szut.de.statistikapplication.createMannschaftActivities.NewSpielerActivity;

public class DataObject {

    private Activity activity;

    private Spieler spieler;
    private ArrayList<Statistikwerte> statistikwerte;

    private ArrayList<View> dataviews = new ArrayList<>();


    public DataObject(Activity activity, Spieler spieler, ArrayList<Statistikwerte> statistikwerte){

        this.activity = activity;

        this.spieler = spieler;
        this.statistikwerte = statistikwerte;

        Log.d("NEU", "NEUER LAUF");
        for(int i=0; i<statistikwerte.size(); i++){
            Log.d("KAT", String.valueOf(statistikwerte.get(i).getKategorieId()));
            if(statistikwerte.get(i).getKategorieId() != 0)
                Log.d("WERT", statistikwerte.get(i).getWert());
            else
                Log.d("WERT", "NULL");
        }

        initDataViews();
    }

    public void initDataViews(){
        dataviews.add(getSpielerView(spieler));

        for(int i=0; i<statistikwerte.size(); i++){
            dataviews.add(getDataView(statistikwerte.get(i)));
        }
    }

    public View getSpielerView(final Spieler spieler){
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

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, NewSpielerActivity.class);

                Bundle bundle = new Bundle();
                bundle.putBoolean("Update", true);
                bundle.putParcelable("Spieler", spieler);

                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        return v;
    }

    public View getDataView(Statistikwerte statistikwert){

        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.table_inhalt, null);

        TextView wert = (TextView) v.findViewById(R.id.wert);
        wert.setText(statistikwert.getWert());


        v.setBackgroundResource(R.drawable.tabledata);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    public ArrayList<View> getDataviews() {
        return dataviews;
    }
}