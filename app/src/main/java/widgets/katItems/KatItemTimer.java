package widgets.katItems;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Spieler;
import database.data.Statistik;
import database.data.Statistikwerte;
import szut.de.statistikapplication.R;

/**
 * Created by roese on 24.03.2015.
 */
public class KatItemTimer extends LinearLayout{

    private Context context;
    private int test;

    private ImageView icon;
    private TextView timerText;
    private TextView beschreibung;

    private Handler customHandler = new Handler();
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;

    private boolean isRun = false;

    private Statistikwerte statistikwert;

    public KatItemTimer(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context);
        init(context, statistik, spieler, kategorie);
        test = 1;
        Log.d("null", String.valueOf(test));
    }

    public KatItemTimer(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs);
        init(context, statistik, spieler, kategorie);
    }


    public void init(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie){

        DBHandler dbHandler = new DBHandler(context, null, null, 1);

        statistikwert = dbHandler.findStatistikwert(statistik.getId(), spieler.getId(), kategorie.getId());
        statistikwert.setWert("00:00:00");
        dbHandler.update(statistikwert);

        dbHandler.close();

        this.context = context;
        initView(context);
        createListener();
    }

    private void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_timer, this);

        icon = (ImageView) this.findViewById(R.id.icon);
        timerText = (TextView) this.findViewById(R.id.timerTextView);
        beschreibung = (TextView) this.findViewById(R.id.beschreibung);
    }

    private void createListener(){
        View.OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRun){
                    isRun = true;
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                }
                else{
                    DBHandler dbHandler = new DBHandler(context, null, null, 1);
                    isRun = false;
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);

                    statistikwert.setWert(timerText.getText().toString());
                    dbHandler.update(statistikwert);
                    dbHandler.close();
                }
            }
        };

        icon.setOnClickListener(onClickListener);
        timerText.setOnClickListener(onClickListener);
        beschreibung.setOnClickListener(onClickListener);

        View.OnLongClickListener onLongClickListener = new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //Funktionsbestimmung beim Klicken eines Buttons des Zurücksetzen-Anfrage-Fensters
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            //Timer zurücksetzen
                            case DialogInterface.BUTTON_POSITIVE:
                                DBHandler dbHandler = new DBHandler(context, null, null, 1);
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
        };

        icon.setOnLongClickListener(onLongClickListener);
        timerText.setOnLongClickListener(onLongClickListener);
        beschreibung.setOnLongClickListener(onLongClickListener);
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerText.setText("" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%02d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };
}
