package szut.de.statistikapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import database.data.Mannschaft;
import szut.de.statistikapplication.createStatiActivities.AufnahmemodusActivity;
import szut.de.statistikapplication.showStatiActivities.EinzelStatiActivity;
import szut.de.statistikapplication.showStatiActivities.GesamtStatiActivity;


public class HauptmenuActivity extends Activity {

    //Global-Varaiblen
    Globals g;

    //Activity-Variablen
    private Resources res;
    private Context context;

    //Objekt-Variablen
    private Mannschaft mannschaft;

    //Widget-Variablen
    TextView vereinsname;
    TextView mannschaftsname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hauptmenu);

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

        //Ausgabefelder
        vereinsname = ((TextView)findViewById(R.id.vereinsname));
        mannschaftsname = ((TextView)findViewById(R.id.mannschaftsname));

        //Default-Werte setzen
        vereinsname.setText(mannschaft.getVereinsname());
        mannschaftsname.setText(mannschaft.getMannschaftsname());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AuswahlMannschaftActivity.class);
        startActivity(intent);
    }

    public void neuestatistik_click(View view){
        Intent intent = new Intent(this, AufnahmemodusActivity.class);
        startActivity(intent);
    }

    public void einzelstatistik_click(View view){
        Intent intent = new Intent(this, EinzelStatiActivity.class);
        startActivity(intent);
    }

    public void gesamtstatistik_click(View view){
        Intent intent = new Intent(this, GesamtStatiActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("Show", false);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void config_click(View view){
        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }

    public void mannschaftsauswahl_click(View view) {
        Intent intent = new Intent(this, AuswahlMannschaftActivity.class);
        startActivity(intent);
    }


}
