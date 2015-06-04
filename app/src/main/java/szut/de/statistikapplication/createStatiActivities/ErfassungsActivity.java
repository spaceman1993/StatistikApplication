package szut.de.statistikapplication.createStatiActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Mannschaft;
import database.data.Spieler;
import database.data.Statistik;
import database.data.Statistikwerte;
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

    public void stati_Fertig_Click(View view){


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        final View v = getLayoutInflater().inflate(R.layout.dialog_ergebnisabfrage, null);

                        TextView heimmannschaft = (TextView)v.findViewById(R.id.heimmannschaft);
                        TextView gastmannschaft = (TextView)v.findViewById(R.id.gastmannschaft);

                        if(statistik.getHeim() == 1) {
                            heimmannschaft.setText(mannschaft.getVereinsname());
                            gastmannschaft.setText(statistik.getGegner());
                        }
                        else{
                            heimmannschaft.setText(statistik.getGegner());
                            gastmannschaft.setText(mannschaft.getVereinsname());
                        }

                        AlertDialog.Builder alert = new AlertDialog.Builder(ErfassungsActivity.this, AlertDialog.THEME_HOLO_DARK);
                        alert.setView(v);
                        alert.setTitle("Endergebnis");
                        alert.setMessage("Tragen Sie das Endergebnis des Spiels ein:");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                DBHandler dbHandler = new DBHandler(getApplicationContext(), null, null, 1);

                                EditText heimtore = (EditText)v.findViewById(R.id.heimtore);
                                EditText gegnertore = (EditText)v.findViewById(R.id.gasttore);

                                if(statistik.getHeim() == 1) {
                                    statistik.setEigeneTore(Integer.parseInt(heimtore.getText().toString()));
                                    statistik.setGegnerTore(Integer.parseInt(gegnertore.getText().toString()));
                                }
                                else{
                                    statistik.setEigeneTore(Integer.parseInt(gegnertore.getText().toString()));
                                    statistik.setGegnerTore(Integer.parseInt(heimtore.getText().toString()));
                                }
                                dbHandler.update(statistik);

                                Intent intent = new Intent(getApplicationContext(), ErgebnistabelleActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("Statistik", statistik);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });

                        alert.show();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        Dialog d = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_DARK)
                .setTitle("Erstellung")
                .setMessage("Wollen Sie die Erfassung beenden?")
                .setPositiveButton("JA", dialogClickListener)
                .setNegativeButton("NEIN", dialogClickListener)
                .create();
        d.show();

    }
}
