package szut.de.statistikapplication.createStatiActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Mannschaft;
import database.data.Spieler;
import database.data.Statistik;
import database.data.Statistikwerte;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.R;
import szut.de.statistikapplication.showStatiActivities.ErgebnistabelleActivity;
import widgets.katItems.KatItemCheckbox;
import widgets.katItems.KatItemCounter;
import widgets.katItems.KatItemFliessZahl;
import widgets.katItems.KatItemNotiz;
import widgets.katItems.KatItemTimer;

public class LiveErfassungActivity extends Activity {

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
    private TextView benennung;
    private GridView gridview;
    private SpielerViewAdapter spielerGridView;
    private KategorisierungsViewAdapter kategorisierungsView;
    private KategorienViewAdapter kategorienView;

    //Variablen
    private ArrayList<Spieler> spieler;
    private ArrayList<Kategorie> kategorien;
    private Spieler aktSpieler;
    private boolean isSpielerbild;
    private boolean isKategorisierungsbild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        benennung = (TextView) findViewById(R.id.benennung);
        gridview = (GridView) findViewById(R.id.gridview);

        spielerGridView = new SpielerViewAdapter(this, R.layout.table_spieler, spieler);
        kategorisierungsView = new KategorisierungsViewAdapter(this, android.R.layout.simple_list_item_2, dbHandler.getAllKategorisierungsnamen(mannschaft));

        showSpieler();

    }

    public void showSpieler(){
        isSpielerbild = true;
        isKategorisierungsbild = false;
        benennung.setText("Spieler wählen");
        gridview.setNumColumns(2);
        gridview.setAdapter(spielerGridView);
    }

    public void showKategorisierung(){
        isSpielerbild = false;
        isKategorisierungsbild = true;
        benennung.setText("Oberkategorie wählen");
        gridview.setNumColumns(1);
        gridview.setAdapter(kategorisierungsView);
    }

    public void showKategorien(ArrayList<Kategorie> kategorien){
        isSpielerbild = false;
        isKategorisierungsbild = false;
        kategorienView = new KategorienViewAdapter(this, R.layout.list_item_button, aktSpieler, kategorien);
        benennung.setText("Kategorie wählen");
        gridview.setNumColumns(1);
        gridview.setAdapter(kategorienView);
    }

    @Override
    public void onBackPressed() {
        if(isSpielerbild){
            finish();
        }
        else if(isKategorisierungsbild){
            showSpieler();
        }
        else{
            showSpieler();
        }
    }

    public class SpielerViewAdapter extends ArrayAdapter<Spieler> {
        protected LayoutInflater inflater;
        protected int layout;

        public SpielerViewAdapter(Activity activity, int resourceId, ArrayList<Spieler> objects){
            super(activity, resourceId, objects);
            layout = resourceId;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final Spieler item = getItem(position);

            if(convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
            }

            TextView trikonummer = (TextView) convertView.findViewById(R.id.trikonummerErfassung);
            ImageView foto = (ImageView) convertView.findViewById(R.id.image_view);
            TextView vorname = (TextView) convertView.findViewById(R.id.vorname);
            TextView nachname = (TextView) convertView.findViewById(R.id.nachname);

            trikonummer.setText(String.valueOf(item.getTrikonummer()));
            foto.setImageBitmap(item.getFoto());
            vorname.setText(item.getVorname());
            nachname.setText(item.getNachname());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aktSpieler = item;
                    showKategorisierung();
                }
            });

            return convertView;
        }
    }

    public class KategorisierungsViewAdapter extends ArrayAdapter<String> {
        protected LayoutInflater inflater;
        protected int layout;
        protected Activity activity;

        public KategorisierungsViewAdapter(Activity activity, int resourceId, ArrayList<String> objects){
            super(activity, resourceId, objects);
            layout = resourceId;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.activity = activity;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final String item = getItem(position);

            if(convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
            }

            TextView text = (TextView) convertView.findViewById(android.R.id.text1);
            text.setGravity(Gravity.CENTER);
            text.setText(item);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHandler dbHandler = new DBHandler(activity, null, null, 1);
                    ArrayList<Kategorie> kategorien = dbHandler.findAktivKategorienByKategorisierungAndMannschaft(item, mannschaft);
                    dbHandler.close();
                    showKategorien(kategorien);
                }
            });

            return convertView;
        }
    }


    public class KategorienViewAdapter extends ArrayAdapter<Kategorie> {

        protected LayoutInflater inflater;
        protected int layout;
        protected Spieler spieler;

        public KategorienViewAdapter(Activity activity,int resourceId, Spieler spieler, ArrayList<Kategorie> list) {
            super(activity,resourceId,list);

            layout = resourceId;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.spieler = spieler;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                if (getItem(position).getArt().equals("Zähler")) {
                    convertView = new KatItemCounter(context, statistik, spieler, getItem(position));
                } else if (getItem(position).getArt().equals("Fließzahleingabe")) {
                    convertView = new KatItemFliessZahl(context, statistik, spieler, getItem(position));
                } else if (getItem(position).getArt().equals("Checkbox")) {
                    convertView = new KatItemCheckbox(context, statistik, spieler, getItem(position));
                } else if (getItem(position).getArt().equals("Timer")) {
                    convertView = new KatItemTimer(context, statistik, spieler, getItem(position));
                } else if (getItem(position).getArt().equals("Notiz")) {
                    convertView = new KatItemNotiz(context, statistik, spieler, getItem(position));
                } else {
                    convertView = inflater.inflate(layout, parent, false);
                }
            }

            return convertView;
        }
    }

    public void finish(){

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

                        AlertDialog.Builder alert = new AlertDialog.Builder(LiveErfassungActivity.this, AlertDialog.THEME_HOLO_DARK);
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
