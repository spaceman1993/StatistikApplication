package szut.de.statistikapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import database.DBHandler;
import database.data.Mannschaft;
import hilfklassen.listview.EntryAdapter;
import hilfklassen.listview.EntryItem;
import hilfklassen.listview.Item;
import hilfklassen.listview.SectionItem;
import szut.de.statistikapplication.createMannschaftActivities.SportauswahlActivity;


public class AuswahlMannschaftActivity extends Activity {

    //Global-Varaiblen
    Globals g;

    //Activity-Variablen
    private Resources res;
    private Context context;

    //Objekt-Variablen
    private Mannschaft mannschaft;

    //Hilfvariablen
    private ArrayList<Mannschaft> handball;
    private ArrayList<Mannschaft> fussball;
    private ArrayList<Mannschaft> basketball;
    private ArrayList<Mannschaft> eigene;

    private ArrayList<Item> items;

    //Widget-Variablen
    private  ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auswahl_mannschaft);

        initWidgets();
        createListener();
        checkMannschaften();
    }

    /**
     * Initialisiert die Variablen der Activity und belegt sie mit Default-Werten
     */
    public void initWidgets(){

        g = (Globals)getApplication();

        //Application
        context = this;
        res = context.getResources();

        //Objekt
        g.setMannschaft(new Mannschaft());
        mannschaft = g.getMannschaft();

        //Ausgabefelder
        items = new ArrayList<Item>();
        listview=(ListView)findViewById(R.id.listView_main);

        //Default-Werte setzen
        initMannschaften();
        fillListView();
    }

    /**
     * Initialisiert die verschiedenen Sportarten und belegt sie mit den gefundenen Mannschaften
     * der jeweiligen Sportart
     */
    public void initMannschaften(){
        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        handball = dbHandler.findMannschaftenDerSportart("handball");
        fussball = dbHandler.findMannschaftenDerSportart("fussball");
        basketball = dbHandler.findMannschaftenDerSportart("basketball");
        eigene = dbHandler.findMannschaftenDerSportart("eigene");

        dbHandler.close();
    }

    /**
     * Erzeugt die Listeneinträge der Listview. Dargestellt werden alle erstellten Mannschaften,
     * sortiert nach der Sportart
     */
    public void fillListView(){
        //Zurücksetzung der Items
        items.clear();

        //Erzeugung der Handballsection mit vorherigen Trenner, gefolgt von den Mannschaften
        if(handball.size() > 0) {
            items.add(new SectionItem("Handball"));
            for (int i = 0; i < handball.size(); i++) {
                items.add(new EntryItem(handball.get(i)));
            }
        }

        //Erzeugung der Fußballsection mit vorherigen Trenner, gefolgt von den Mannschaften
        if(fussball.size() > 0) {
            items.add(new SectionItem("Fußball"));
            for (int i = 0; i < fussball.size(); i++) {
                items.add(new EntryItem(fussball.get(i)));
            }
        }

        //Erzeugung der Basketballsection mit vorherigen Trenner, gefolgt von den Mannschaften
        if(basketball.size() > 0) {
            items.add(new SectionItem("Basketball"));
            for (int i = 0; i < basketball.size(); i++) {
                items.add(new EntryItem(basketball.get(i)));
            }
        }

        //Erzeugung der Eigenensection mit vorherigen Trenner, gefolgt von den Mannschaften
        if(eigene.size() > 0) {
            items.add(new SectionItem("Eigene Sportarten"));
            for (int i = 0; i < eigene.size(); i++) {
                items.add(new EntryItem(eigene.get(i)));
            }
        }

        //Hinzufügen der Liste an die Listview
        EntryAdapter adapter = new EntryAdapter(context, items);
        listview.setAdapter(adapter);
    }

    /**
     * Erzeugung alle Listener, die für die Klasse von Bedeutung sind
     */
    public void createListener(){

        //On-Click-Listener für die Listview
        //Bei der Wahl einer Mannschaft wird das Hauptmenü mit der gewählten Mannschaft aufgerufen
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DBHandler dbHandler = new DBHandler(context, null, null, 1);

                //Suchen der gewählten Mannschaft
                EntryItem item = (EntryItem)items.get(position);
                mannschaft = dbHandler.findMannschaft(item.id);
                g.setMannschaft(mannschaft);
                dbHandler.close();

                //Aufruf des Hauptmenüs mit Übergabe der gewählten Mannschaft
                Intent intent = new Intent(context, HauptmenuActivity.class);
                startActivity(intent);
            }
        });

        //On-Long-Click-Listener für die Listview
        //Bei der Wahl einer Mannschaft und einem langen gedrückt halten wird ein Fenster
        //aufgerufen, das den Benutzer darauf hinweist, ob er die Mannschaft löschen möchte
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //Suchen der gewählten Mannschaft
                EntryItem item = (EntryItem)items.get(position);

                DBHandler dbHandler = new DBHandler(context, null, null, 1);
                mannschaft = dbHandler.findMannschaft(item.id);
                Log.d("LÖSCHEN", String.valueOf(mannschaft.getId()));
                dbHandler.close();

                //Funktionsbestimmung beim Klicken eines Buttons des Lösch-Anfrage-Fensters
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            //Mannschaft soll gelöscht werden
                            case DialogInterface.BUTTON_POSITIVE:
                                //Mannschaft wird gelöscht und die Listview wird aktualisiert
                                DBHandler dbHandler = new DBHandler(context, null, null, 1);
                                dbHandler.delete(mannschaft);
                                dbHandler.close();
                                initMannschaften();
                                fillListView();
                                break;

                            //Mannschaft soll nicht gelöscht werden
                            case DialogInterface.BUTTON_NEGATIVE:
                                //Nichts passiert
                                break;
                        }
                    }
                };

                //Lösch-Anfrage-Fenster erstellen
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(item.title + " (" + item.subtitle + ") wirklich löschen?")
                       .setPositiveButton("JA", dialogClickListener)
                       .setNegativeButton("NEIN", dialogClickListener)
                       .show();

                return true;
            }
        });
    }

    /**
     * Überprüfung, ob Activity anders reagieren muss
     */
    public void checkMannschaften(){
        if(handball.size()+fussball.size()+basketball.size()+eigene.size() == 0){
            Intent intent = new Intent(context, SportauswahlActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Button-On-Click-Listener
     * Bei der Auswahl des Buttons "Neue Mannschaft" wird die Sportauswahl-Activity gestartet,
     * in der der Benutzer eine neue Mannschaft erstellt
     * @param view
     */
    public void addMannschaft(View view){
        Intent intent = new Intent(context, SportauswahlActivity.class);
        startActivity(intent);
    }

}
