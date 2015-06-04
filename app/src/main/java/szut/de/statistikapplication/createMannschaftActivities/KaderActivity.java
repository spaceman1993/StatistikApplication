package szut.de.statistikapplication.createMannschaftActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;

import database.data.Mannschaft;
import database.data.Spieler;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.R;
import widgets.swipemenu.SwipeMenuEditDelete;
import widgets.swipemenulistview.SwipeMenuListView;


public class KaderActivity extends Activity {

    //Global-Varaiblen
    Globals g;

    //Activity-Variablen
    private Resources res;
    private Context context;

    //Objekt-Variablen
    private Mannschaft mannschaft;
    private boolean isUpdate;

    //Widget-Variablen
    private SwipeMenuEditDelete spielerListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kader);

        initWidgets();
        checkKaderUpdating();
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

        //Ausgabefelder
        spielerListView = new SwipeMenuEditDelete(this, mannschaft, (SwipeMenuListView)findViewById(R.id.spielerList), new Spieler(), R.layout.swipemenu_item, true, true, true, true);

    }

    /**
     * Überprüfung, ob Activity anders reagieren muss
     */
    public void checkKaderUpdating(){
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
        spielerListView.updateListView();
    }

    /**
     * Button-On-Click-Listener
     * Bei der Auswahl des Buttons "SPIELER ERZEUGEN" wird die NewSpieler-Activity gestartet
     * @param view
     */
    public void addSpieler(View view){
        Intent intent = new Intent(this, NewSpielerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("Update", false);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Button-On-Click-Listener
     * Bei der Auswahl des Buttons "FERTIG" bzw. "UPDATE" wird  die Kategorie-Activity gestartet
     * bzw. der Datensatz der betroffenen Mannschaft geupdatet und zurück zum Config-Menü geleitet
     * @param view
     */
    public void fertig_Click(View view) {

        if(spielerListView.getAllAktivItems().size() == 0){
            String fehlermeldung = "Sie haben keine Spieler gewählt! Wollen Sie trotzdem fortfahren?";

            Dialog d = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_DARK)
                    .setTitle("Hinweis")
                    .setMessage(fehlermeldung)
                    .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    fortfahren();
                                }
                        })
                    .setNegativeButton("NEIN", null)
                    .create();
            d.show();
        }
        else{
            fortfahren();
        }
    }

    public void fortfahren(){
        if(isUpdate){
            finish();
        }
        else {
            Intent intent = new Intent(this, KategorieActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("Update", false);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}