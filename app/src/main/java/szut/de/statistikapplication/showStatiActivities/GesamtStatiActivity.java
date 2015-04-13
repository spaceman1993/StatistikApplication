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
    private Mannschaft mannschaft;
    private ArrayList<Spieler> spieler;
    private ArrayList<Kategorie> kategorien;

    private ViewFlipper pageFlipper;
    private AdapterViewFlipper spielerFlipper;

    private GestureDetector swipeDetector;
    private View.OnTouchListener switchListener;

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
        mannschaft = g.getMannschaft();
        spieler = dbHandler.getSpielerDerMannschaft(mannschaft);
        kategorien = dbHandler.getKategorienDerMannschaft(mannschaft);


        //Ausgabefelder
        pageFlipper = (ViewFlipper) findViewById(R.id.pageFlippper);
        spielerFlipper = (AdapterViewFlipper) findViewById(R.id.spielerViewFlipper);

        //Variablen
        swipeDetector = new GestureDetector(new SwipeDetector());
        switchListener = new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (swipeDetector.onTouchEvent(event)) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        initViews();

        //pageFlipper.setAdapter(new PageFlipperAdapter(this, 0, pages));
        pageFlipper.setOnTouchListener(switchListener);


        dbHandler.close();
    }

    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (swipeDetector.onTouchEvent(event)) {
            return false;
        } else {
            return true;
        }
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
        spielerFlipper.setOnTouchListener(switchListener);
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


    private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            System.out.println(" in onFling() :: ");

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

            else if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
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


//    public class PageFlipperAdapter extends ArrayAdapter<View>{
//        protected LayoutInflater inflater;
//        protected int layout;
//        protected Activity activity;
//
//        private PageFlipperAdapter(Activity activity, int resourceId, ArrayList<View> objects){
//            super(activity, resourceId, objects);
//            this.activity = activity;
//            layout = resourceId;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = getItem(position);
//            }
//            return convertView;
//        }
//    }



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

            iv.setImageBitmap(spieler.getFoto());
            vorname.setText(spieler.getVorname());
            nachname.setText(spieler.getNachname());
            trikonummer.setText(String.valueOf(spieler.getTrikonummer()));
            groesse.setText(String.valueOf(spieler.getGroesse()));

            GridView kategorientabelle = (GridView) convertView.findViewById(R.id.katView);

            kategorientabelle.setAdapter(new GridViewAdapter(activity, R.layout.gridview_spieler_item, spieler, kategorien));

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
}
