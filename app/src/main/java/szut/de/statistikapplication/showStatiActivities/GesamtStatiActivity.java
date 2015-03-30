package szut.de.statistikapplication.showStatiActivities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

import hilfklassen.DBHandler;
import hilfklassen.Kategorie;
import hilfklassen.Mannschaft;
import hilfklassen.SelectableItem;
import hilfklassen.Spieler;
import hilfklassen.Statistik;
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

    private AdapterViewFlipper spielerstatis;

    private Animation animFlipInForeward;
    private Animation animFlipOutForeward;
    private Animation animFlipInBackward;
    private Animation animFlipOutBackward;


    private GestureDetector.SimpleOnGestureListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        //Variablen
        animFlipInForeward = AnimationUtils.loadAnimation(this, R.anim.flipin);
        animFlipOutForeward = AnimationUtils.loadAnimation(this, R.anim.flipout);
        animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse);
        animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse);

        //Ausgabefelder
        spielerstatis = (AdapterViewFlipper) findViewById(R.id.adapterViewFlipper);
        spielerstatis.setAdapter(new MyCustomAdapter(this, R.layout.swipeview_spieler, spieler));

        dbHandler.close();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return gestureDetector.onTouchEvent(event);
    }


    private void SwipeRight(){
        spielerstatis.setInAnimation(animFlipInBackward);
        spielerstatis.setOutAnimation(animFlipOutBackward);
        spielerstatis.showPrevious();
    }

    private void SwipeLeft(){
        spielerstatis.setInAnimation(animFlipInForeward);
        spielerstatis.setOutAnimation(animFlipOutForeward);
        spielerstatis.showNext();
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            float sensitvity = 100;
            if((e1.getX() - e2.getX()) > sensitvity){
                SwipeLeft();
            }else if((e2.getX() - e1.getX()) > sensitvity){
                SwipeRight();
            }

            return true;
        }

    };

    GestureDetector gestureDetector
            = new GestureDetector(simpleOnGestureListener);


    public class MyCustomAdapter extends ArrayAdapter<Spieler> {
        protected LayoutInflater inflater;
        protected int layout;
        protected Activity activity;

        private MyCustomAdapter(Activity activity, int resourceId, ArrayList<Spieler> objects){
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

            GridView kategorientabelle = (GridView) convertView.findViewById(R.id.katview);

            kategorientabelle.setAdapter(new GridViewAdapter(activity, R.layout.gridview_spieler_item, kategorien));

            return convertView;
        }
    }



    public class GridViewAdapter extends ArrayAdapter<Kategorie> {
        protected LayoutInflater inflater;
        protected int layout;

        public GridViewAdapter(Activity activity, int resourceId, ArrayList<Kategorie> objects){
            super(activity, resourceId, objects);
            layout = resourceId;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            wert.setText("123");

            return convertView;
        }
    }


}
