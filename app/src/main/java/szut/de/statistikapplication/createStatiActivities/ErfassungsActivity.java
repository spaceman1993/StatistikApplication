package szut.de.statistikapplication.createStatiActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hilfklassen.DBHandler;
import hilfklassen.Kategorie;
import hilfklassen.Mannschaft;
import hilfklassen.Spieler;
import hilfklassen.Statistik;
import hilfklassen.Statistikwerte;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.HauptmenuActivity;
import szut.de.statistikapplication.R;
import szut.de.statistikapplication.showStatiActivities.ErgebnistabelleActivity;
import widgets.horizontalListView.HorizontalListView;
import widgets.katItems.KatItemCheckbox;
import widgets.katItems.KatItemCounter;
import widgets.katItems.KatItemFliessZahl;
import widgets.katItems.KatItemNotiz;
import widgets.katItems.KatItemTimer;

public class ErfassungsActivity extends Activity {

    //Global-Varaiblen
    Globals g;

    //Activity-Variablen
    private Resources res;
    private Context context;

    //Objekt-Variablen
    private Mannschaft mannschaft;
    private Statistik statistik;
    private ArrayList<Statistikwerte> statistikwerte;

    //Widget-Variablen
    private ListView listview;

    //Variablen
    private ArrayList<Spieler> spieler;
    private ArrayList<Kategorie> kategorien;

    ArrayList<Erfassungsaufbau> erfassungsListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erfassungs);

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

        DBHandler dbHandler = new DBHandler(context, null, null, 1);

        //Objekt
        mannschaft = g.getMannschaft();
        statistik = dbHandler.getLastStatistik();
        statistikwerte = dbHandler.getStatistikwerteDerStatistik(statistik);

        spieler = dbHandler.getSpielerDerStatistik(statistik);
        kategorien = dbHandler.getAktiveKategorienDerMannschaft(mannschaft);

        //Widgets
        listview = (ListView) findViewById(R.id.erfassungsListe);

        //Default-Werte setzen
        erfassungsListe = new ArrayList<Erfassungsaufbau>();


        for(int i=0; i<spieler.size(); i++){
            Spieler einzelspieler = spieler.get(i);
            erfassungsListe.add(new Erfassungsaufbau(einzelspieler.getTrikonummer(), einzelspieler.getFoto(), new KategorienAdapter(this, R.layout.list_item_button, einzelspieler, kategorien)));
        }

        listview.setAdapter(new SpielerAdapter(this, R.layout.list_item_horizontalswipe, erfassungsListe));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_erfassungs, menu);
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

    public class SpielerAdapter extends ArrayAdapter<Erfassungsaufbau> {

        protected LayoutInflater inflater;
        protected int layout;
        protected ArrayList<Erfassungsaufbau> items;

        public SpielerAdapter(Activity activity,int resourceId,
                             ArrayList<Erfassungsaufbau> list) {
            super(activity,resourceId,list);

            layout = resourceId;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            items = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = inflater.inflate(layout, parent, false);
            TextView trikonummer = (TextView) v.findViewById(R.id.trikonummerErfassung);
            ImageView picture = (ImageView) v.findViewById(R.id.image_view);
            HorizontalListView kategorien = (HorizontalListView) v.findViewById(R.id.horizontalListView);


            trikonummer.setText(String.valueOf(getItem(position).getTrikonummer()));
            picture.setImageBitmap(getItem(position).getPicture());
            kategorien.setAdapter(getItem(position).getKategorienListe());

            //ViewGroup.LayoutParams p = (ViewGroup.LayoutParams)v.getLayoutParams();

            if(position == 0){
                //p.setMargins(p.leftMargin, p.topMargin+100, p.rightMargin, p.bottomMargin);
            }
            else if(position == items.size()-1){
                //p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin+100);
            }

            return v;
        }

    }

    public class KategorienAdapter extends ArrayAdapter<Kategorie> {

        protected LayoutInflater inflater;
        protected int layout;

        protected Spieler spieler;
        protected ArrayList<Kategorie> items;


        public KategorienAdapter(Activity activity,int resourceId, Spieler spieler, ArrayList<Kategorie> list) {
            super(activity,resourceId,list);

            layout = resourceId;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.spieler = spieler;
            items = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = null;

            if(getItem(position).getArt().equals("Zähler")){
                v = createCounterView(position);
            }
            else if(getItem(position).getArt().equals("Fließzahleingabe")){
                v = createFließzahlView(position);
            }
            else if(getItem(position).getArt().equals("Checkbox")){
                v = createCheckboxView(position);
            }
            else if(getItem(position).getArt().equals("Timer")){
                v = createTimerView(position);
            }
            else if(getItem(position).getArt().equals("Notiz")){
                v = createNotizView(position);
            }
            else {
                v = inflater.inflate(layout, parent, false);
            }
//            Button kategorienAction = (Button) v.findViewById(R.id.kategorieaction);
//
//            kategorienAction.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_fussball, 0, 0);
//
//                    kategorienAction.setText(getItem(position).getName());
//
//            if(position == items.size()-1){
//                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight()+50, v.getPaddingBottom());
//            }
//
//            if(position%3 == 0){
//                kategorienAction.setBackgroundResource(R.drawable.blue_button);
//            }
//            else if(position%3 == 1){
//                kategorienAction.setBackgroundResource(R.drawable.red_button);
//            }
//            else{
//                kategorienAction.setBackgroundResource(R.drawable.green_button);
//            }

            return v;

        }


        public View createCounterView(int position){

            View v = new KatItemCounter(context, statistik, spieler, getItem(position));
            setImageAndText(v, position);

            return v;
        }

        public View createFließzahlView(int position) {

            View v = new KatItemFliessZahl(context, statistik, spieler, getItem(position));
            setImageAndText(v, position);

            return v;
        }

        public View createCheckboxView(int position) {

            View v = new KatItemCheckbox(context, statistik, spieler, getItem(position));
            setImageAndText(v, position);

            return v;
        }

        public View createTimerView(int position) {

            View v = new KatItemTimer(context, statistik, spieler, getItem(position));
            setImageAndText(v, position);

            return v;
        }

        public View createNotizView(int position) {

            View v = new KatItemNotiz(context, statistik, spieler, getItem(position));
            setImageAndText(v, position);

            return v;
        }

        public void setImageAndText(View v, int position){

            Kategorie kategorie = getItem(position);

            ImageView icon = (ImageView) v.findViewById(R.id.icon);
            TextView beschreibung = (TextView) v.findViewById(R.id.beschreibung);

            icon.setImageBitmap(kategorie.getFoto());
            beschreibung.setText(kategorie.getName());

        }
    }

    public void go_Hauptmenu_Click(View view){
        Intent intent = new Intent(this, HauptmenuActivity.class);
        startActivity(intent);
    }

    public void stati_Fertig_Click(View view){
        Intent intent = new Intent(this, ErgebnistabelleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Statistik", statistik);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
