package szut.de.statistikapplication.createMannschaftActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import hilfklassen.DBHandler;
import hilfklassen.Mannschaft;
import hilfklassen.OnTouchCloseKeyboardActivity;
import hilfklassen.Position;
import hilfklassen.Spieler;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.R;

public class NewSpielerActivity extends OnTouchCloseKeyboardActivity {

    //Global-Varaiblen
    Globals g;

    //Activity
    Context context;

    //Objekt
    Mannschaft mannschaft;
    Spieler spieler;
    Boolean isUpdate;

    //Changer
    int mannschaftsID;
    EditText vorname;
    EditText nachname;
    SeekBar groesse;
    SeekBar trikoNr;
    AlertDialog position;
    Bitmap foto;

    //Ausgaben
    TextView groesseTV;
    TextView trikoNrTV;
    ArrayList<Position> selectedPositionen = new ArrayList<>();

    //Positionsbild-Variablen
    ArrayList<Position> positionsListe;
    ArrayList<Position> seletedItems = new ArrayList<Position>();
    String[] items;
    boolean[] isCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_spieler);
        initCloseEvent();
        context = this;

        initWidgets();
        createListener();
        checkSpielerUpdating();

        createPositionChanger();
    }

    public void initWidgets(){

        g = (Globals)getApplication();

        //Okjekt
        spieler = new Spieler();
        mannschaft = g.getMannschaft();
        isUpdate = getIntent().getExtras().getBoolean("Update");

        //Changefelder
        groesse = (SeekBar)findViewById(R.id.spielerGroesseSeekBar);
        trikoNr = (SeekBar)findViewById(R.id.spielerTrikonummerSeekBar);
        vorname = (EditText) findViewById(R.id.spielerVorname);
        nachname = (EditText) findViewById(R.id.spielerNachname);

        //Ausgabefelder
        groesseTV = (TextView)findViewById(R.id.spielerGroesse);
        trikoNrTV = (TextView)findViewById(R.id.spielerTrikonummer);

        //Default-Werte setzen
        mannschaftsID = mannschaft.getId();
        groesse.setProgress(50);
        trikoNr.setProgress(0);
        foto = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_unbekannt);

        DBHandler dbHandler = new DBHandler(this, null, null, 0);
        positionsListe = (ArrayList<Position>)dbHandler.findObjectsDerSportart(new Position(), mannschaft.getSportart());
        dbHandler.close();

        isCheck = new boolean[positionsListe.size()];

        ArrayList<String> posNamen = new ArrayList<>();

        for(int i=0; i<positionsListe.size(); i++) {
            posNamen.add(positionsListe.get(i).getNameLang());
        }

        items = new String[posNamen.size()];
        items = posNamen.toArray(items);

        groesseTV.requestFocus();
    }

    public void createListener(){

        groesse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                groesseTV.setText(String.valueOf(progress+120));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        trikoNr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                trikoNrTV.setText(String.valueOf(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void checkSpielerUpdating(){

        if(isUpdate){
            spieler = (Spieler) getIntent().getExtras().getParcelable("Spieler");

            ((Button) findViewById(R.id.addbutton)).setText("Update");
            mannschaftsID = spieler.getMannschaftsID();
            vorname.setText(spieler.getVorname());
            nachname.setText(spieler.getNachname());
            groesse.setProgress(spieler.getGroesse()-120);
            trikoNr.setProgress(spieler.getTrikonummer()-1);
            foto = spieler.getFoto();

            for(int i=0; i<positionsListe.size(); i++) {
                int j=0;
                boolean gefunden = false;
                while(j<spieler.getPosition().size() && !gefunden){
                    if(positionsListe.get(i).getId() == spieler.getPosition().get(j).getId()){
                        isCheck[i] = true;
                        gefunden = true;
                    }
                    j++;
                }
                if(!gefunden){
                    isCheck[i] = false;
                }
            }
        }
        else{
                for (int i = 0; i < isCheck.length; i++) {
                    isCheck[i] = false;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_spieler, menu);
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

    public void spielerFoto_click(View view){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent intent = new Intent(this, SpielerbildActivity.class);
        intent.putExtra("foto",byteArray);
        startActivityForResult(intent, 1);
    }

    public void createPositionChanger(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wähle Position/en");
        builder.setMultiChoiceItems(items, isCheck,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                            isCheck[indexSelected] = true;
                        } else {
                            isCheck[indexSelected] = false;
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Log.d("POS", String.valueOf(selectedPositionen.size()));
                    }
                });

        position = builder.create();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                byte[] byteArray = data.getByteArrayExtra("foto");
                foto = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                break;
        }
    }

    public void spielerAdd_click(View view){

        //Aktivierte Positionen finden
        for(int i=0; i<isCheck.length; i++){
            if(isCheck[i])
                selectedPositionen.add(positionsListe.get(i));
        }

        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        Log.d("ManschaftsIDSPIELER", String.valueOf(mannschaftsID));
        spieler.setMannschaftsID(mannschaftsID);
        spieler.setVorname(vorname.getText().toString());
        spieler.setNachname(nachname.getText().toString());
        spieler.setTrikonummer(Integer.parseInt(((TextView) findViewById(R.id.spielerTrikonummer)).getText().toString()));
        spieler.setGroesse(Integer.parseInt(((TextView) findViewById(R.id.spielerGroesse)).getText().toString()));
        spieler.setPosition(selectedPositionen);
        spieler.setFoto(foto);
        spieler.setSelected(1);
        spieler.setSportart(mannschaft.getSportart());

        if(isUpdate){
            dbHandler.update(spieler);
        }
        else {
            dbHandler.add(spieler);
        }

        finish();
    }

    public void positionWählen_click(View view){
        selectedPositionen.removeAll(selectedPositionen);
        position.show();
    }


    public Spieler getNewspieler() {
        return spieler;
    }
}
