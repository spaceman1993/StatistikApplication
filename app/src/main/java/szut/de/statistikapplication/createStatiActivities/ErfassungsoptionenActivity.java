package szut.de.statistikapplication.createStatiActivities;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import szut.de.statistikapplication.HauptmenuActivity;
import szut.de.statistikapplication.R;
import szut.de.statistikapplication.showStatiActivities.ErgebnistabelleActivity;
import widgets.swipemenu.SwipeMenuEditDelete;
import widgets.swipemenulistview.SwipeMenuListView;

public class ErfassungsoptionenActivity extends OnTouchCloseKeyboardActivity {

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
    Boolean isUpdate;

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

        checkStatistikUpdating();
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
        isUpdate = getIntent().getExtras().getBoolean("Update");

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

    public void checkStatistikUpdating() {

        if (isUpdate) {
            statistik = (Statistik) getIntent().getExtras().getParcelable("Statistik");

            ((Button) findViewById(R.id.stati_Anlegen)).setText("Statistik updaten");

            if(statistik.getHeim() == 1){
                ((RadioButton)heim.getChildAt(0)).setChecked(true);
            }
            else{
                ((RadioButton)heim.getChildAt(1)).setChecked(true);
            }

            gegner.setText(statistik.getGegner());

            statistik.setGegner(gegner.getText().toString());
            statistik.setEigeneTore(statistik.getEigeneTore());
            statistik.setGegnerTore(statistik.getGegnerTore());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AufnahmemodusActivity.class);
        startActivity(intent);
    }

    public void stati_Anlegen(View view){

        DBHandler dbHandler =  new DBHandler(context, null, null, 1);

        statistik.setMannschaftsid(g.getMannschaft().getId());

        if(heim.getCheckedRadioButtonId() == R.id.radioHeim){
            statistik.setHeim(1);
        }
        else{
            statistik.setHeim(0);
        }
        statistik.setGegner(gegner.getText().toString());

        if(isUpdate){


            final View v = getLayoutInflater().inflate(R.layout.dialog_ergebnisabfrage, null);

            TextView heimmannschaft = (TextView)v.findViewById(R.id.heimmannschaft);
            TextView gastmannschaft = (TextView)v.findViewById(R.id.gastmannschaft);

            final EditText heimtore = (EditText)v.findViewById(R.id.heimtore);
            final EditText gegnertore = (EditText)v.findViewById(R.id.gasttore);

            if(statistik.getHeim() == 1) {
                heimmannschaft.setText(mannschaft.getVereinsname());
                heimtore.setText(String.valueOf(statistik.getEigeneTore()));
                gastmannschaft.setText(statistik.getGegner());
                gegnertore.setText(String.valueOf(statistik.getGegnerTore()));
            }
            else{
                heimmannschaft.setText(statistik.getGegner());
                heimtore.setText(String.valueOf(statistik.getGegnerTore()));
                gastmannschaft.setText(mannschaft.getVereinsname());
                gegnertore.setText(String.valueOf(statistik.getEigeneTore()));
            }

            AlertDialog.Builder alert = new AlertDialog.Builder(ErfassungsoptionenActivity.this);
            alert.setView(v);
            alert.setTitle("Endergebnis");
            alert.setMessage("Tragen Sie das Endergebnis des Spiels ein:");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    DBHandler dbHandler = new DBHandler(getApplicationContext(), null, null, 1);

                    if(statistik.getHeim() == 1) {
                        statistik.setEigeneTore(Integer.parseInt(heimtore.getText().toString()));
                        statistik.setGegnerTore(Integer.parseInt(gegnertore.getText().toString()));
                    }
                    else{
                        statistik.setEigeneTore(Integer.parseInt(gegnertore.getText().toString()));
                        statistik.setGegnerTore(Integer.parseInt(heimtore.getText().toString()));
                    }
                    dbHandler.update(statistik);

                    dbHandler.close();
                    finish();
                }
            });

            alert.show();

        }
        else{
            String fDate = new SimpleDateFormat("dd.MM.yy").format(datum);
            statistik.setDatum(fDate);

            statistik.setEigeneTore(0);
            statistik.setGegnerTore(0);
            dbHandler.add(statistik);
        }

        if(!isUpdate) {
            statistik = dbHandler.getLastStatistik();
            spieler = spielerListView.getAllAktivItems();
            kategorien = dbHandler.getAktiveKategorienDerMannschaft(mannschaft);

            for (int i = 0; i < spieler.size(); i++) {
                for (int j = 0; j < kategorien.size(); j++) {
                    String wert = "";

                    if (kategorien.get(i).getArt().equals("Zähler") || kategorien.get(i).getArt().equals("Auto-Zähler") || kategorien.get(i).getArt().equals("Checkbox")) {
                        wert = "0";
                    } else if (kategorien.get(i).getArt().equals("Fließzahleingabe")) {
                        wert = "0.00";
                    } else if (kategorien.get(i).getArt().equals("Timer")) {
                        wert = "00:00:00";
                    }

                    statistikwerte = new Statistikwerte(statistik.getId(), spieler.get(i).getId(), kategorien.get(j).getId(), wert);
                    dbHandler.add(statistikwerte);
                }
            }



            if(getIntent().getExtras().getBoolean("Bearbeiten")){

                Intent intent = new Intent(getApplicationContext(), ErgebnistabelleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Statistik", statistik);
                bundle.putBoolean("Bearbeiten", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, LiveErfassungActivity.class);

                dbHandler.close();
                startActivity(intent);
            }
        }
    }
}
