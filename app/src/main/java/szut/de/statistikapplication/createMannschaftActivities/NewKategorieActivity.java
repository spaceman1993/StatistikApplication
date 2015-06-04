package szut.de.statistikapplication.createMannschaftActivities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Mannschaft;
import hilfklassen.OnTouchCloseKeyboardActivity;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.R;

/**
 * Eine Activity, die eine neue Kategorie erzeugt
 */
public class NewKategorieActivity extends OnTouchCloseKeyboardActivity {

    //Global-Varaiblen
    Globals g;

    //Activity
    Context context;

    Mannschaft mannschaft;

    Kategorie kategorie;
    Boolean isUpdate;

    EditText name;
    ArrayAdapter<String> kategorisierungAdapter;
    Spinner kategorisierung;
    Button kategorisierungAdd;
    String kategorisierungsname;
    List<String> kategorisierungListe;
    Spinner art;
    List<String> artenListe;
    GridView gridView;
    ArrayList<Bitmap> imageIDs;

    RectShape rect;
    ShapeDrawable rectShapeDrawable;
    Paint paint;
    int activatedSymbol;


    /**
     * Erzeugt eine Activity ohne Titleanzeige
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_kategorie);
        initCloseEvent();

        initWidgets();
        createListener();
        checkKategorieUpdating();
    }


    /**
     * Initialisiert die Variablen der Activity und belegt sie mit Default-Werten
     */
    public void initWidgets(){

        g = (Globals)getApplication();

        context = this;

        //Objekt
        mannschaft = g.getMannschaft();
        kategorie = new Kategorie();
        isUpdate = getIntent().getExtras().getBoolean("Update");

        //Change-Felder
        name = (EditText) findViewById(R.id.kategoriename);
        kategorisierung = (Spinner) findViewById(R.id.kategorisierung);
        kategorisierungAdd = (Button) findViewById(R.id.kategorisierungAdd);
        art = (Spinner) findViewById(R.id.kategorieart);
        gridView = (GridView) findViewById(R.id.symbolauswahl);

        //Ausgabefelder
        imageIDs = new ArrayList<>();
        artenListe = new ArrayList<>();
        kategorisierungListe = new ArrayList<>();
        rect = new RectShape();
        rectShapeDrawable = new ShapeDrawable(rect);
        paint = rectShapeDrawable.getPaint();

        //Default-Werte setzen
        activatedSymbol = -1;
        initSymbole();
        initListen();

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);

        kategorisierungAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, kategorisierungListe);
        kategorisierungAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategorisierung.setAdapter(kategorisierungAdapter);

        ArrayAdapter<String> artAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, artenListe);
        artAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        art.setAdapter(artAdapter);

        gridView.setAdapter(new ImageAdapter(this));
        gridView.requestFocus();
    }


    /**
     * Erstellung der Listener für die Activity
     */
    public void createListener(){

        kategorisierungAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);

                alert.setTitle("Kategorisierung hinzufügen");
                alert.setMessage("Kategorisierungsname:");

                final EditText input = new EditText(context);
                input.setSingleLine(true);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        kategorisierungsname = input.getText().toString();

                        kategorisierungListe.add(kategorisierungsname);
                        kategorisierungAdapter.notifyDataSetChanged();
                        kategorisierung.setSelection(kategorisierungListe.size()-1);
                    }
                });

                alert.show();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {

                for(int i=0; i<imageIDs.size(); i++){
                    gridView.getChildAt(i).setBackground(null);
                }

                kategorie.setFoto(imageIDs.get(position));
                activatedSymbol = position;
                gridView.getChildAt(position).setBackground(rectShapeDrawable);

            }
        });
    }


    /**
     * Überprüfung, ob Activity anders reagieren muss
     */
    public void checkKategorieUpdating(){

        if(isUpdate){
            kategorie = (Kategorie) getIntent().getExtras().getParcelable("Kategorie");

            ((Button) findViewById(R.id.addbutton)).setText("Update");
            name.setText(kategorie.getName());

            int i = 0;
            boolean gefunden = false;
            String kategorisierungSuche = kategorie.getKategorisierung();
            while(i<kategorisierungListe.size() && !gefunden){
                if(kategorisierungSuche.equals(kategorisierungListe.get(i))){
                    gefunden = true;
                    kategorisierung.setSelection(i);
                }
                i++;
            }


            i = 0;
            gefunden = false;
            String artsuche = kategorie.getArt();
            while(i<artenListe.size() && !gefunden){
                if(artsuche.equals(artenListe.get(i))){
                    gefunden = true;
                    art.setSelection(i);
                }
                i++;
            }

            Bitmap suche = kategorie.getFoto();
            i = 0;
            gefunden = false;
            while(i<imageIDs.size() && !gefunden){
                if(suche.sameAs(imageIDs.get(i))){
                    gefunden = true;
                    activatedSymbol = i;
                }
                i++;
            }
        }
    }


    /**
     * Beim Wechsel des Fokus wird überprüft welches
     * Symbol aktiv ist und dieses wird umrandet
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            if(isUpdate){
                gridView.getChildAt(activatedSymbol).setBackground(rectShapeDrawable);
            }
            else if(activatedSymbol != -1){
                gridView.getChildAt(activatedSymbol).setBackground(rectShapeDrawable);
            }
            else{
                gridView.getChildAt(0).setBackground(rectShapeDrawable);
                kategorie.setFoto(imageIDs.get(0));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_kategorie, menu);
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


    /**
     * Beim Drücken der Zurück-Taste wird die Meldung erzeugt, ob die Erfassung der Daten abgebrochen werden soll.
     * Wenn ja, wird zurückgeleitet zur Kategorien-Activitybei.
     */
    @Override
    public void onBackPressed() {
        String fehlermeldung = "Wollen Sie die Erfassung ohne zu Speichern beenden?";

        Dialog d = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_DARK)
                .setTitle("Achtung")
                .setMessage(fehlermeldung)
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("NEIN", null)
                .create();
        d.show();
    }

    /**
     * Beim Drücken der ADD-Taste wird die Erfassung gespeichert und beendet
     */
    public void addKategorie(View view){
        beenden();
    }


    /**
     * Überprüfung, ob Erfassung Inordnung ist
     */
    public void beenden(){
        String kategorienameText = name.getText().toString();

        if(kategorienameText.isEmpty()){
            String fehlermeldung = "Bitte geben Sie der Kategorie eine Bezeichnung!";

            Dialog d = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_DARK)
                    .setTitle("Achtung")
                    .setMessage(fehlermeldung)
                    .setNegativeButton("OK", null)
                    .create();
            d.show();
        }
        else {
            fortfahren();
        }
    }

    /**
     * Entscheidung, wie die App weiter verlaufen soll. Beim Updaten einer Kategorie wird diese aktualisiert.
     * Bei Neuerfassung hinzufügt. Dann wird die Activity beendet und leitet zurück zur Kategorie-Activity
     */
    public void fortfahren(){
        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        kategorie.setName(name.getText().toString());
        kategorie.setKategorisierung(kategorisierung.getSelectedItem().toString());
        kategorie.setArt(art.getSelectedItem().toString());
        kategorie.setSelected(1);
        kategorie.setEigene(0);
        kategorie.setSportart(mannschaft.getSportart());

        if(isUpdate){
            dbHandler.update(kategorie);
        }
        else {
            dbHandler.add(kategorie);
        }

        finish();
    }


    /**
     * Erzeugung der Symbole für die Kategorien
     */
    public void initSymbole(){
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.tor)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.keintor)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.wuerfe)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.gehalten)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.block)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.tempos)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.siebenmetertor)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.keinsiebenmetertor)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.foul)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.zweiminuten)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.zweiminutenhand)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.gelbekarte)).getBitmap());
        imageIDs.add(((BitmapDrawable)this.getResources().getDrawable(R.drawable.rotekarte)).getBitmap());
    }


    /**
     * Erzeugung der Kategoriearten und Auflistung aller Kategorisierungen
     */
    public void initListen(){
        artenListe.add("Zähler");
        artenListe.add("Fließzahleingabe");
        artenListe.add("Checkbox");

        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        kategorisierungListe = dbHandler.getAllKategorisierungsnamen(mannschaft);
    }


    /**
     * Aufbereitung der Anzeige der Symbole für die Kategorien
     */
    public class ImageAdapter extends BaseAdapter
    {
        private Context context;

        public ImageAdapter(Context c)
        {
            context = c;
        }

        public int getCount() {
            return imageIDs.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(68, 68));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageBitmap(imageIDs.get(position));

            return imageView;
        }
    }
}
