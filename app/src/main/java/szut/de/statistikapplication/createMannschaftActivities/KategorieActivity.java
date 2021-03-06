package szut.de.statistikapplication.createMannschaftActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Mannschaft;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.HauptmenuActivity;
import szut.de.statistikapplication.R;
import widgets.swipemenu.SwipeMenuEditDelete;
import widgets.swipemenulistview.SwipeMenuListView;

/**
 * Eine Activity, die die Kategorien einer Mannschaft verwaltet
 */
public class KategorieActivity extends Activity {

    //Global-Varaiblen
    Globals g;

    //Activity-Variablen
    private Resources res;
    private Context context;

    //Objekt-Variablen
    private Mannschaft mannschaft;
    private boolean isUpdate;

    //Widget-Variablen
    private SwipeMenuEditDelete kategorienListView;

    /**
     * Erzeugt eine Activity ohne Titleanzeige
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorie);

        initWidgets();
        checkKategorieUpdating();
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
        mannschaft = g.getMannschaft();
        isUpdate = getIntent().getExtras().getBoolean("Update");

        if(!isUpdate) {
            DBHandler dbHandler = new DBHandler(context, null, null, 1);
            ArrayList<Kategorie> kats = dbHandler.getAllDefaultKategorienDerSportart(mannschaft.getSportart());

            for (int i = 0; i < kats.size(); i++) {
                kats.get(i).setMannschaftsID(mannschaft.getId());
                dbHandler.add(kats.get(i));
            }

            dbHandler.close();
        }


        //Ausgabefelder
        kategorienListView = new SwipeMenuEditDelete(this, mannschaft, (SwipeMenuListView)findViewById(R.id.kategorienList), new Kategorie(), R.layout.swipemenu_item, true, true, true, true);
    }

    /**
     * Überprüfung, ob Activity anders reagieren muss
     */
    public void checkKategorieUpdating(){
        if(isUpdate){
            ((Button) findViewById(R.id.fertig)).setText("Update");
        }
    }

    /**
     * Beim Fortsetzen der Activity wird aufgrund einer Änderung in der Datenbank die Listview
     * geupdatet
     */
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        kategorienListView.updateListView();
    }

    /**
     * Button-On-Click-Listener
     * Bei der Auswahl des Buttons "KATEGORIE ERZEUGEN" wird  die NewKategorie-Activity gestartet
     * @param view
     */
    public void addKategorie(View view){
        Intent intent = new Intent(this, NewKategorieActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Button-On-Click-Listener
     * Bei der Auswahl des Buttons "FERTIG" bzw. "UPDATE" wird  das Hauptmenü gestartet
     * bzw. der Datensatz der betroffenen Mannschaft geupdatet und zurück zum Config-Menü geleitet
     * @param view
     */
    public void fertig_Click(View view) {
        fortfahren();
    }

    public void fortfahren(){
        if(isUpdate){
            finish();
        }
        else {
            Intent intent = new Intent(this, HauptmenuActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}