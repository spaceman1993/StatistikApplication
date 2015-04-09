package szut.de.statistikapplication.createMannschaftActivities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import database.DBHandler;
import database.data.Mannschaft;
import hilfklassen.OnTouchCloseKeyboardActivity;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.R;
import widgets.cropOption.CropOption;
import widgets.cropOption.CropOptionAdapter;


public class MannschaftActivity extends OnTouchCloseKeyboardActivity {

    //Global-Varaiblen
    Globals g;

    //Activity-Variablen
    private Resources res;
    private Context context;

    //Objekt-Variablen
    private Mannschaft mannschaft;
    private boolean isUpdate;

    //Hilfsvariablen
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Uri mImageCaptureUri;
    private ImageView mImageView;

    private final int CROP_FROM_CAMERA = 2;
    private final int PICK_FROM_FILE = 3;

    //Widget-Variablen
    private TextView überschrift;
    private EditText vereinsname;
    private EditText mannschaftsname;
    private Bitmap foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mannschaft);
        initCloseEvent();

        initWidgets();
        createListener();

        checkMannschaftUpdating();
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
        isUpdate = getIntent().getExtras().getBoolean("Update");

        //Change-Felder
        vereinsname = ((EditText)findViewById(R.id.vereinsname));
        mannschaftsname = ((EditText)findViewById(R.id.mannschaftsname));
        mImageView = (ImageView) findViewById(R.id.vereinslogo);
        builder	= new AlertDialog.Builder(this);
        dialog = builder.create();

        //Ausgabefelder
        überschrift = (TextView)findViewById(R.id.mannschaftÜberschrift);

        //Default-Werte setzen
        setÜberschrift();

        foto = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.defaultvereinslogo)).getBitmap();
        String [] items	= new String [] {"Galarie"};
        ArrayAdapter<String> adapter = new ArrayAdapter<> (context, android.R.layout.select_dialog_item,items);

    }

    /**
     * Ermittelt die Sportart und setzt diese als Überschrift der Activity
     */
    private void setÜberschrift() {

        String sportart = mannschaft.getSportart();

        if (sportart.equals("handball")){
            überschrift.setText(getString(R.string.handball));
        }
        else if (sportart.equals("fussball")){
            überschrift.setText(getString(R.string.fussball));
        }
        else if (sportart.equals("basketball")){
            überschrift.setText(getString(R.string.basketball));
        }
        else if (sportart.equals("eigene")){
            überschrift.setText(getString(R.string.eigene));
        }
    }

    /**
     * Erzeugung alle Listener, die für die Klasse von Bedeutung sind
     */
    public void createListener(){
        //On-Click-Listener für die ImageView
        //Durch das Klicken kann das Vereinslogo geändert werden
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Wähle Bibliothek"), PICK_FROM_FILE);
            }
        });
    }

    /**
     * Überprüfung, ob Activity anders reagieren muss
     */
    public void checkMannschaftUpdating(){
        if(isUpdate){
            ((Button) findViewById(R.id.addbutton)).setText("Update");
            vereinsname.setText(mannschaft.getVereinsname());
            mannschaftsname.setText(mannschaft.getMannschaftsname());
            mImageView.setImageBitmap(mannschaft.getVereinslogo());
            foto = mannschaft.getVereinslogo();
        }
    }

    /**
     * Button-On-Click-Listener
     * Bei der Auswahl des Buttons "FERTIG" bzw. "UPDATE" wird  die Kader-Activity gestartet
     * bzw. der Datensatz der betroffenen Mannschaft geupdatet und zurück zum Config-Menü geleitet
     * @param view
     */
    public void fertig_Click(View view) {



        String vereinsnameText = vereinsname.getText().toString();
        String mannschaftsnameText = mannschaftsname.getText().toString();

        if(vereinsnameText.isEmpty() || mannschaftsnameText.isEmpty()){
            String fehlermeldung = "Bitte füllen Sie folgende Felder vollständig aus:\n";
            if(vereinsnameText.isEmpty()){
                fehlermeldung += "\nVereinsname";
            }
            if(mannschaftsnameText.isEmpty()){
                fehlermeldung += "\nMannschaftsname";
            }

            Dialog d = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_DARK)
                    .setTitle("Fehler")
                    .setMessage(fehlermeldung)
                    .setNegativeButton("OK", null)
                    .create();
            d.show();
        }

        else{
            mannschaft.setVereinsname(vereinsnameText);
            mannschaft.setMannschaftsname(mannschaftsnameText);
            mannschaft.setVereinslogo(foto);

            DBHandler dbHandler = new DBHandler(this, null, null, 1);
            dbHandler.update(mannschaft);
            dbHandler.close();

            if(isUpdate){
                finish();
            }
            else {
                Intent intent = new Intent(context, KaderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("Update", false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();

                doCrop();

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    foto = photo;
                    mImageView.setImageBitmap(photo);
                }

                File f = new File(mImageCaptureUri.getPath());

                if (f.exists()) f.delete();

                break;

        }
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1000);
            intent.putExtra("aspectY", 1000);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i 		= new Intent(intent);
                ResolveInfo res	= list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title 	= getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon		= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);

                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mannschaft, menu);
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

}
