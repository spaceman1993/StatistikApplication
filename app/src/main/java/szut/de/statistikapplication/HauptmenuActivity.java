package szut.de.statistikapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import database.data.Mannschaft;
import szut.de.statistikapplication.createStatiActivities.ErfassungsartActivity;
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
    TextView überschrift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Wähle", "Test10");
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
        überschrift = ((TextView)findViewById(R.id.hauptmenuÜberschrift));

        //Default-Werte setzen
        überschrift.setText(mannschaft.getMannschaftsname());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hauptmenu, menu);
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

    public void neuestatistik_click(View view){
        Intent intent = new Intent(this, ErfassungsartActivity.class);
        startActivity(intent);
    }

    public void einzelstatistik_click(View view){
        Intent intent = new Intent(this, EinzelStatiActivity.class);
        startActivity(intent);
    }

    public void gesamtstatistik_click(View view){
        Intent intent = new Intent(this, GesamtStatiActivity.class);
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
