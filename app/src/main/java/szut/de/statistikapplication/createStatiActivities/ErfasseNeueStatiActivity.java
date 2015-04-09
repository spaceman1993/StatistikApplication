package szut.de.statistikapplication.createStatiActivities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Mannschaft;
import hilfklassen.OnTouchCloseKeyboardActivity;
import database.data.Spieler;
import database.data.Statistik;
import database.data.Statistikwerte;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.R;
import widgets.swipemenu.SwipeMenuEditDelete;
import widgets.swipemenulistview.SwipeMenuListView;

public class ErfasseNeueStatiActivity extends OnTouchCloseKeyboardActivity {

    //Global-Varaiblen
    Globals g;

    //Activity-Variablen
    Resources res;
    Context context;

    //Input-Widgets
    RadioGroup heim;
    EditText gegner;
    SwipeMenuEditDelete spielerListView;
    Button anlegen;

    //Objekt-Variablen
    Mannschaft mannschaft;
    Statistik statistik;
    Statistikwerte statistikwerte;

    //Variablen
    ArrayList<Spieler> spieler;
    ArrayList<Kategorie> kategorien;
    Date datum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erfasse_neue_stati);
        initCloseEvent();

        initWidgets();
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
        statistik = new Statistik();
        statistikwerte =  new Statistikwerte();

        //Input-Widgets
        heim = (RadioGroup) findViewById(R.id.radioHeimAuswärts);
        gegner = (EditText) findViewById(R.id.gegnerbezeichnung);
        spielerListView = new SwipeMenuEditDelete(this, mannschaft, (SwipeMenuListView)findViewById(R.id.spielerList), new Spieler(), R.layout.swipemenu_item, false, false, false, true);
        anlegen = (Button) findViewById(R.id.stati_Anlegen);

        //Variablen
        spieler = new ArrayList<Spieler>();
        kategorien =  new ArrayList<Kategorie>();
        datum = new Date();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_erfasse_neue_stati, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void stati_Anlegen(View view){

        DBHandler dbHandler =  new DBHandler(context, null, null, 1);

        statistik.setMannschaftsid(g.getMannschaft().getId());

        String fDate = new SimpleDateFormat("dd.MM.yy").format(datum);
        statistik.setDatum(fDate);

        if(heim.getCheckedRadioButtonId() == R.id.radioHeim){
            statistik.setHeim(1);
        }
        else{
            statistik.setHeim(0);
        }
        statistik.setGegner(gegner.getText().toString());
        statistik.setEigeneTore(0);
        statistik.setGegnerTore(0);

        dbHandler.add(statistik);

        statistik = dbHandler.getLastStatistik();
        spieler = spielerListView.getAllAktivItems();
        kategorien = dbHandler.getAktiveKategorienDerMannschaft(mannschaft);

        for(int i=0; i<spieler.size(); i++){
            for(int j=0; j<kategorien.size(); j++){
                String wert = "";

                if(kategorien.get(i).getArt().equals("Zähler") || kategorien.get(i).getArt().equals("Auto-Zähler") || kategorien.get(i).getArt().equals("Checkbox")){
                    wert = "0";
                }
                else if (kategorien.get(i).getArt().equals("Fließzahleingabe")){
                    wert = "0.00";
                }
                else if (kategorien.get(i).getArt().equals("Timer")){
                    wert = "00:00:00";
                }

                statistikwerte = new Statistikwerte(statistik.getId(), spieler.get(i).getId(), kategorien.get(j).getId(), wert);
                dbHandler.add(statistikwerte);
            }
        }

        dbHandler.close();

        Intent intent = new Intent(this, ErfassungsActivity.class);
        startActivity(intent);
    }
}
