package szut.de.statistikapplication.showStatiActivities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Mannschaft;
import database.data.Spieler;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.R;

public class GesamtStatiActivity extends Activity {

    //Global-Varaiblen
    Globals g;

    //Activity-Variablen
    private Resources res;
    private Context context;

    //Objekt-Variablen
    private View view;
    private Mannschaft mannschaft;
    private ArrayList<Spieler> spieler;
    private ArrayList<Kategorie> kategorien;

    private ViewFlipper pageFlipper;
    private AdapterViewFlipper spielerFlipper;
    private AdapterViewFlipper kategorienFlipper;

    private GestureDetector swipePage;
    private View.OnTouchListener switchPageListener;

    private GestureDetector swipeSpieler;
    View.OnTouchListener switchSpielerListener;

    private GestureDetector swipeKategorie;
    View.OnTouchListener switchKategorienListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesamt_stati);

        initWidgets();
    }


    /**
     * Initialisiert die Variablen der Activity und belegt sie mit Default-Werten
     */
    public void initWidgets(){

        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        g = (Globals)getApplication();

        //Application
        context = this;
        res = context.getResources();

        //Objekt
        view = (View)findViewById(R.id.gesamtstatistik_activity);
        mannschaft = g.getMannschaft();
        spieler = dbHandler.getSpielerDerMannschaft(mannschaft);
        kategorien = dbHandler.getKategorienDerMannschaft(mannschaft);


        //Ausgabefelder
        pageFlipper = (ViewFlipper) findViewById(R.id.pageFlippper);
        spielerFlipper = (AdapterViewFlipper) findViewById(R.id.spielerViewFlipper);

        //Variablen
        swipePage = new GestureDetector(new SwipePageListener());
        switchPageListener = new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (swipePage.onTouchEvent(event)) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        swipeSpieler = new GestureDetector(new SwipeSpielerListener());
        switchSpielerListener = new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (swipeSpieler.onTouchEvent(event)) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        initViews();

        view.setOnTouchListener(switchPageListener);
        spielerFlipper.setOnTouchListener(switchSpielerListener);

        dbHandler.close();
    }

    public void initViews(){

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View page1 = inflater.inflate(R.layout.activity_gesamt_stati_page_spiele, null);
        TextView anzahl = (TextView) page1.findViewById(R.id.anzahlspiele);
        TextView gewonnen = (TextView) page1.findViewById(R.id.gewonnen);
        TextView verloren = (TextView) page1.findViewById(R.id.verloren);
        TextView unentschieden = (TextView) page1.findViewById(R.id.unentschieden);
        TextView tore = (TextView) page1.findViewById(R.id.tore);
        TextView gegentore = (TextView) page1.findViewById(R.id.gegentore);
        TextView durchschnittTore = (TextView) page1.findViewById(R.id.schnittTore);
        TextView durchschnittGegentore = (TextView) page1.findViewById(R.id.schnittGegentore);

        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        ArrayList<String> übersicht = dbHandler.getStatistikübersichtDerMannschaft(mannschaft);
        anzahl.setText(übersicht.get(0));
        gewonnen.setText(übersicht.get(1));
        verloren.setText(übersicht.get(2));
        unentschieden.setText(übersicht.get(3));
        tore.setText(übersicht.get(4));
        gegentore.setText(übersicht.get(5));
        durchschnittTore.setText(übersicht.get(6));
        durchschnittGegentore.setText(übersicht.get(7));

        pageFlipper.addView(page1);

        View page2 = inflater.inflate(R.layout.activity_gesamt_stati_page_spieler, null);
        spielerFlipper = (AdapterViewFlipper) page2.findViewById(R.id.spielerViewFlipper);
        spielerFlipper.setAdapter(new SpielerFlipperAdapter(this, R.layout.swipeview_spieler, spieler));
        spielerFlipper.setOnTouchListener(switchPageListener);
        pageFlipper.addView(page2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gesamt_stati, menu);
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

    public class SpielerFlipperAdapter extends ArrayAdapter<Spieler> {
        protected LayoutInflater inflater;
        protected int layout;
        protected Activity activity;

        private SpielerFlipperAdapter(Activity activity, int resourceId, ArrayList<Spieler> objects){
            super(activity, resourceId, objects);
            this.activity = activity;
            layout = resourceId;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
            }

            Spieler spieler = getItem(position);

            ImageView iv = (ImageView) convertView.findViewById(R.id.image);
            TextView vorname = (TextView) convertView.findViewById(R.id.vorname);
            TextView nachname = (TextView) convertView.findViewById(R.id.nachname);
            TextView trikonummer = (TextView) convertView.findViewById(R.id.trikonummer);
            TextView groesse = (TextView) convertView.findViewById(R.id.groesse);
            TextView pos = (TextView) convertView.findViewById(R.id.position);

            iv.setImageBitmap(spieler.getFoto());
            vorname.setText(spieler.getVorname());
            nachname.setText(spieler.getNachname());
            trikonummer.setText(String.valueOf(spieler.getTrikonummer()));
            groesse.setText(String.valueOf(spieler.getGroesse()));

            String positionsText = "Position: ";
            if(spieler.getPosition().size() > 0) {
                for (int i = 0; i < spieler.getPosition().size(); i++) {
                    positionsText += spieler.getPosition().get(i).getNameKurz();
                    if (i < spieler.getPosition().size() - 1) {
                        positionsText += ", ";
                    }
                }
            }else{
                positionsText += "Keine";
            }

            pos.setText(positionsText);

            DBHandler dbHandler = new DBHandler(activity, null, null, 1);
            ArrayList<ArrayList<Kategorie>> kategorienartenliste = dbHandler.getAllKategorienarten();
            dbHandler.close();

            kategorienFlipper = (AdapterViewFlipper) convertView.findViewById(R.id.kategorienViewFlipper);

            swipeKategorie = new GestureDetector(new SwipeKategorieListener());
            switchKategorienListener = new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    if (swipeKategorie.onTouchEvent(event)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            };

            kategorienFlipper.setOnTouchListener(switchKategorienListener);

            kategorienFlipper.setAdapter(new KategorienFlipperAdapter(activity, R.layout.swipeview_spielerkategorien, spieler, kategorienartenliste));


            return convertView;
        }


    }

    public class KategorienFlipperAdapter extends ArrayAdapter<ArrayList<Kategorie>> {
        protected LayoutInflater inflater;
        protected int layout;
        protected Activity activity;
        protected Spieler spieler;

        private KategorienFlipperAdapter(Activity activity, int resourceId, Spieler spieler, ArrayList<ArrayList<Kategorie>> objects){
            super(activity, resourceId, objects);
            this.activity = activity;
            layout = resourceId;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.spieler = spieler;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
            }

            ArrayList<Kategorie> kategorien = getItem(position);

            TextView kategorienart = (TextView) convertView.findViewById(R.id.kategorienart);
            GridView kategorientabelle = (GridView) convertView.findViewById(R.id.katView);

            kategorienart.setText(kategorien.get(0).getKategorienart());
            kategorientabelle.setAdapter(new GridViewAdapter(activity, R.layout.gridview_spieler_item, spieler, kategorien));
            kategorientabelle.setOnTouchListener(switchKategorienListener);

            return convertView;
        }
    }


    public class GridViewAdapter extends ArrayAdapter<Kategorie> {
        protected LayoutInflater inflater;
        protected int layout;
        protected Spieler spieler;

        public GridViewAdapter(Activity activity, int resourceId, Spieler spieler, ArrayList<Kategorie> objects){
            super(activity, resourceId, objects);
            layout = resourceId;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.spieler = spieler;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Kategorie item = getItem(position);

            if(convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
            }

            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            TextView kategoriename = (TextView) convertView.findViewById(R.id.kategoriename);
            TextView wert = (TextView) convertView.findViewById(R.id.wert);

            icon.setImageBitmap(item.getFoto());
            kategoriename.setText(item.getName());

            DBHandler dbHandler =  new DBHandler(context, null, null, 1);
            String wertBetrag = dbHandler.getGesamtwertOfKategorieFromSpieler(spieler, item);


            wert.setText(wertBetrag);

            return convertView;
        }
    }

    private class SwipePageListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            switchPage(e1, e2, velocityX, velocityY);
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void switchPage(MotionEvent e1, MotionEvent e2, float velocityX,
                            float velocityY){
        int SWIPE_MIN_DISTANCE = 120;
        int SWIPE_THRESHOLD_VELOCITY = 200;

        if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            if(pageFlipper.getDisplayedChild() < pageFlipper.getChildCount()-1) {
//                    pageFlipper.setInAnimation(context, R.anim.bottom_in);
//                    pageFlipper.setOutAnimation(context, R.anim.top_out);
                pageFlipper.showNext();
            }

        } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            if(pageFlipper.getDisplayedChild() > 0) {
                pageFlipper.showPrevious();
//                    pageFlipper.setInAnimation(context, R.anim.bottom_out);
//                    pageFlipper.setOutAnimation(context, R.anim.top_in);
            }
        }
    }

    private class SwipeSpielerListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            switchPage(e1, e2, velocityX, velocityY);

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                spielerFlipper.setInAnimation(context, R.anim.right_in);
                spielerFlipper.setOutAnimation(context, R.anim.left_out);
                spielerFlipper.showNext();

            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                spielerFlipper.setInAnimation(context, R.anim.right_out);
                spielerFlipper.setOutAnimation(context, R.anim.left_in);
                spielerFlipper.showPrevious();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private class SwipeKategorieListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            switchPage(e1, e2, velocityX, velocityY);

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                kategorienFlipper.setInAnimation(context, R.anim.right_in);
                kategorienFlipper.setOutAnimation(context, R.anim.left_out);
                kategorienFlipper.showNext();

            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                kategorienFlipper.setInAnimation(context, R.anim.right_out);
                kategorienFlipper.setOutAnimation(context, R.anim.left_in);
                kategorienFlipper.showPrevious();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
