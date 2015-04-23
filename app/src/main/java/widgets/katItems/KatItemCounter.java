package widgets.katItems;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
public class KatItemCounter extends KatItem{

    private Context context;

    private ImageView icon;
    private TextView zaehler;
    private TextView beschreibung;
    
    public KatItemCounter(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, statistik, spieler, kategorie);
    }

    public KatItemCounter(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs, statistik, spieler, kategorie);
    }

    public void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_counter, this);

        icon = (ImageView) this.findViewById(R.id.icon);
        zaehler = (TextView) this.findViewById(R.id.zaehler);
        beschreibung = (TextView) this.findViewById(R.id.beschreibung);

        findViewById(R.id.katItemView).setOnClickListener(this);
        findViewById(R.id.katItemView).setOnLongClickListener(this);

    }

    @Override
    public void onClick(View v) {
        DBHandler dbHandler = new DBHandler(getContext(), null, null, 1);
        int wert = Integer.parseInt(zaehler.getText().toString());
        wert++;
        zaehler.setText(Integer.toString(wert));
        statistikwert.setWert(String.valueOf(wert));
        dbHandler.update(statistikwert);
        dbHandler.close();

    }

    @Override
    public boolean onLongClick(View v) {

        DBHandler dbHandler = new DBHandler(getContext(), null, null, 1);
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
