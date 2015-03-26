package widgets.katItems;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import hilfklassen.DBHandler;
import hilfklassen.Kategorie;
import hilfklassen.Spieler;
import hilfklassen.Statistik;
import hilfklassen.Statistikwerte;
import szut.de.statistikapplication.R;

/**
 * Created by roese on 24.03.2015.
 */
public class KatItemCounter extends LinearLayout{

    private Context context;

    private ImageView icon;
    private TextView zaehler;
    private TextView beschreibung;

    private Statistikwerte statistikwert;
    
    public KatItemCounter(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context);
        init(context, statistik, spieler, kategorie);
    }

    public KatItemCounter(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs);
        init(context, statistik, spieler, kategorie);
    }



    public void init(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie){

        this.context = context;

        DBHandler dbHandler = new DBHandler(context, null, null, 1);

        statistikwert = dbHandler.findStatistikwert(statistik.getId(), spieler.getId(), kategorie.getId());
        statistikwert.setWert("0");
        dbHandler.update(statistikwert);

        dbHandler.close();

        initView(context);
        createListener();
    }

    private void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_counter, this);

        icon = (ImageView) this.findViewById(R.id.icon);
        zaehler = (TextView) this.findViewById(R.id.zaehler);
        beschreibung = (TextView) this.findViewById(R.id.beschreibung);
    }


    private void createListener(){

        View.OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHandler dbHandler = new DBHandler(context, null, null, 1);
                int wert = Integer.parseInt(zaehler.getText().toString());
                wert++;
                zaehler.setText(Integer.toString(wert));
                statistikwert.setWert(String.valueOf(wert));
                dbHandler.update(statistikwert);
                dbHandler.close();
            }
        };

        icon.setOnClickListener(onClickListener);
        zaehler.setOnClickListener(onClickListener);
        beschreibung.setOnClickListener(onClickListener);


        View.OnLongClickListener onLongClickListener = new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DBHandler dbHandler = new DBHandler(context, null, null, 1);
                int wert = Integer.parseInt(zaehler.getText().toString());
                if(wert > 0) {
                    wert--;
                }
                zaehler.setText(Integer.toString(wert));
                statistikwert.setWert(String.valueOf(wert));
                dbHandler.update(statistikwert);
                dbHandler.close();

                return true;
            }
        };

        icon.setOnLongClickListener(onLongClickListener);
        zaehler.setOnLongClickListener(onLongClickListener);
        beschreibung.setOnLongClickListener(onLongClickListener);

    }

    public void setIconImage(Drawable image){
        icon.setImageDrawable(image);
    }

    public void setBeschreibungstext(String text){
        beschreibung.setText(text);
    }

    public TextView getZaehler() {
        return zaehler;
    }
}
