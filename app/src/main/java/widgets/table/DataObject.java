package widgets.table;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Spieler;
import database.data.Statistikwerte;
import szut.de.statistikapplication.R;
import szut.de.statistikapplication.createMannschaftActivities.NewSpielerActivity;
import szut.de.statistikapplication.showStatiActivities.GesamtStatiActivity;

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
                Intent intent = new Intent(activity, GesamtStatiActivity.class);

                Bundle bundle = new Bundle();
                bundle.putBoolean("Show", true);
                bundle.putParcelable("Spieler", spieler);

                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {

                 Dialog d = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK)
                         .setTitle("Hinweis")
                         .setMessage("Wollen Sie den Spieler " + spieler.getVorname() + " " + spieler.getNachname() + " bearbeiten?")
                         .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int id) {
                                 Intent intent = new Intent(activity, NewSpielerActivity.class);

                                 Bundle bundle = new Bundle();
                                 bundle.putBoolean("Update", true);
                                 bundle.putParcelable("Spieler", spieler);

                                 intent.putExtras(bundle);
                                 activity.startActivity(intent);
                             }
                         })
                         .setNegativeButton("NEIN", null)
                         .create();
                 d.show();

                 return false;
             }
        });

        return v;
    }

    public View getDataView(final Statistikwerte statistikwert){


        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.table_inhalt, null);

        final TextView wert = (TextView) v.findViewById(R.id.wert);
        wert.setText(statistikwert.getWert());


        v.setBackgroundResource(R.drawable.tabledata);

        if(activity.getIntent().getExtras().getBoolean("Bearbeiten")) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateWert(wert, statistikwert);
                }
            });
        }
        else{
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    updateWert(wert, statistikwert);
                    return true;
                }
            });
        }

        return v;
    }

    public void updateWert(final TextView wert, final Statistikwerte statistikwert){

        final DBHandler dbHandler = new DBHandler(activity, null, null, 1);

        final View dialog = activity.getLayoutInflater().inflate(R.layout.dialog_werteaenderung, null);

        ImageView iconIV = (ImageView) dialog.findViewById(R.id.icon);
        TextView spielerTV = (TextView) dialog.findViewById(R.id.spieler);
        TextView kategorieTV = (TextView) dialog.findViewById(R.id.kategorie);
        final EditText wertET = (EditText) dialog.findViewById(R.id.wert);
        wertET.setText(statistikwert.getWert());
        Kategorie kategorie = dbHandler.findKategorie(statistikwert.getKategorieId());

        iconIV.setImageBitmap(kategorie.getFoto());
        spielerTV.setText(spieler.getVorname() + " " + spieler.getNachname());
        kategorieTV.setText(kategorie.getName());

        AlertDialog.Builder alert = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK);
        alert.setView(dialog);
        alert.setTitle("Wertänderung");
        alert.setMessage("Bitte nehmen Sie Ihre Änderung vor.");
        alert.setPositiveButton("Übernehmen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                statistikwert.setWert(wertET.getText().toString());
                wert.setText(wertET.getText().toString());
                dbHandler.update(statistikwert);
                dbHandler.close();
            }
        });
        alert.setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHandler.close();
            }
        });

        alert.show();

    }

    public ArrayList<View> getDataviews() {
        return dataviews;
    }


}