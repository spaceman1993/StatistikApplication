package szut.de.statistikapplication.showStatiActivities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.GestureDetectorCompat;
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
import android.widget.Toast;
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

    private GestureDetectorCompat mDetector;

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


        //Ausgabefelder
        spielerstatis = (AdapterViewFlipper) findViewById(R.id.adapterViewFlipper);
        spielerstatis.setAdapter(new MyCustomAdapter(this, R.layout.swipeview_spieler, spieler));


        //Variablen
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        spielerstatis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });

        dbHandler.close();
    }


    private void SwipeRight(){
        spielerstatis.setInAnimation(this, R.anim.right_out);
        spielerstatis.setOutAnimation(this, R.anim.left_in);
        spielerstatis.showPrevious();
    }

    private void SwipeLeft(){
        spielerstatis.setInAnimation(this, R.anim.right_in);
        spielerstatis.setOutAnimation(this, R.anim.left_out);
        spielerstatis.showNext();
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

    class SwipeGestureListener extends SimpleOnGestureListener implements
            OnTouchListener {
        Context context;
        GestureDetector gDetector;
        static final int SWIPE_MIN_DISTANCE = 120;
        static final int SWIPE_MAX_OFF_PATH = 250;
        static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public SwipeGestureListener() {
            super();
        }

        public SwipeGestureListener(Context context) {
            this(context, null);
        }

        public SwipeGestureListener(Context context, GestureDetector gDetector) {

            if (gDetector == null)
                gDetector = new GestureDetector(context);

            this.context = context;
            this.gDetector = gDetector;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {



            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH
                        || Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY) {
                    return false;
                }
                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
                    Toast.makeText(DemoSwipe.this, "bottomToTop" + countryName,
                            Toast.LENGTH_SHORT).show();
                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
                    Toast.makeText(DemoSwipe.this,
                            "topToBottom  " + countryName, Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                    return false;
                }
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                    Toast.makeText(DemoSwipe.this,
                            "swipe RightToLeft " + countryName, 5000).show();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                    Toast.makeText(DemoSwipe.this,
                            "swipe LeftToright  " + countryName, 5000).show();
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);

        }


    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float sensitvity = 50;
            if((e1.getX() - e2.getX()) > sensitvity){
                Log.d("LEFT", String.valueOf(e1.getX() - e2.getX()));
                SwipeLeft();
            }else if((e2.getX() - e1.getX()) > sensitvity){
                Log.d("RIGHT", String.valueOf(e2.getX() - e1.getX()));
                SwipeRight();
            }

            return true;
        }
    }


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
