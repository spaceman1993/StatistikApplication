package szut.de.statistikapplication.createMannschaftActivities;

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

import database.DBHandler;
import database.data.Mannschaft;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.R;


public class SportauswahlActivity extends Activity {

    //Global-Varaiblen
    Globals g;

    //Activity-Variablen
    Resources res;
    Context context;

    //Objekt-Variablen
    Mannschaft mannschaft;

    //Hilfsvariablen
    String sportart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);

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
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        mannschaft = dbHandler.createAndGetNewMannschaft(res);
        dbHandler.close();

        g.setMannschaft(mannschaft);
        mannschaft = g.getMannschaft();

    }

    public void sport_click(View view) {

        switch (view.getId()) {
            case R.id.handball:
                sportart = "handball";
                break;
            case R.id.fussball:
                sportart = "fussball";
                break;
            case R.id.basketball:
                sportart = "basketball";
                break;
            case R.id.eigene:
                sportart = "eigene";
                break;
        }

        mannschaft.setSportart(sportart);

        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        dbHandler.update(mannschaft);
        dbHandler.close();

        //Aufruf der Mannschaftseinstellungen mit Übergabe der erzeugten Mannschaft
        Intent intent = new Intent(context, MannschaftActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("Update", false);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
