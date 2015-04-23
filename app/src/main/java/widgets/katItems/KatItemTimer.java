package widgets.katItems;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Spieler;
import database.data.Statistik;
import database.data.Statistikwerte;
import szut.de.statistikapplication.R;

/**
 * Created by roese on 24.03.2015.
 */
public class KatItemTimer extends KatItem{

    private Context context;

    private ImageView icon;
    private TextView timerText;
    private TextView beschreibung;

    private Handler customHandler = new Handler();
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;

    private boolean isRun = false;

    public KatItemTimer(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, statistik, spieler, kategorie);
    }

    public KatItemTimer(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs, statistik, spieler, kategorie);
    }


    public void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_timer, this);

        icon = (ImageView) this.findViewById(R.id.icon);
        timerText = (TextView) this.findViewById(R.id.timerTextView);
        beschreibung = (TextView) this.findViewById(R.id.beschreibung);

        findViewById(R.id.katItemView).setOnClickListener(this);
        findViewById(R.id.katItemView).setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!isRun){
            isRun = true;
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
        }
        else{
            DBHandler dbHandler = new DBHandler(getContext(), null, null, 1);
            isRun = false;
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);

            statistikwert.setWert(timerText.getText().toString());
            dbHandler.update(statistikwert);
            dbHandler.close();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        //long clicked
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    //Timer zurücksetzen
                    case DialogInterface.BUTTON_POSITIVE:
                        DBHandler dbHandler = new DBHandler(getContext(), null, null, 1);
                        isRun = false;
                        customHandler.removeCallbacks(updateTimerThread);

                        timerText.setText("00:00:00");
                        startTime = 0L;
                        timeInMilliseconds = 0L;
                        timeSwapBuff = 0L;
                        updatedTime = 0L;

                        statistikwert.setWert(timerText.getText().toString());
                        dbHandler.update(statistikwert);
                        dbHandler.close();
                        break;

                    //Timer nicht zurücksetzen
                    case DialogInterface.BUTTON_NEGATIVE:
                        //Nichts passiert
                        break;
                }
            }
        };

        //Zurücksetzen-Anfrage-Fenster erstellen
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Timer zurücksetzen?")
                .setPositiveButton("JA", dialogClickListener)
                .setNegativeButton("NEIN", dialogClickListener)
                .show();

        return true;
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            timerText.setText("" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };
}
