package szut.de.statistikapplication.showStatiActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import database.DBHandler;
import database.data.Statistik;
import szut.de.statistikapplication.Globals;
import szut.de.statistikapplication.R;
import widgets.table.Table;

public class ErgebnistabelleActivity extends Activity {

    private Statistik statistik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_ergebnistabelle);

        statistik = (Statistik) getIntent().getExtras().getParcelable("Statistik");

        if(getIntent().getExtras().getBoolean("Bearbeiten")){
            final View v = getLayoutInflater().inflate(R.layout.dialog_ergebnisabfrage, null);

            Globals g = (Globals)getApplication();

            TextView heimmannschaft = (TextView)v.findViewById(R.id.heimmannschaft);
            TextView gastmannschaft = (TextView)v.findViewById(R.id.gastmannschaft);

            if(statistik.getHeim() == 1) {
                heimmannschaft.setText(g.getMannschaft().getVereinsname());
                gastmannschaft.setText(statistik.getGegner());
            }
            else{
                heimmannschaft.setText(statistik.getGegner());
                gastmannschaft.setText(g.getMannschaft().getVereinsname());
            }

            AlertDialog.Builder alert = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
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

                }
            });

            alert.show();
        }

        RelativeLayout body = (RelativeLayout) findViewById(R.id.body);
        body.addView(new Table(this, statistik));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EinzelStatiActivity.class);
        startActivity(intent);
    }

}
