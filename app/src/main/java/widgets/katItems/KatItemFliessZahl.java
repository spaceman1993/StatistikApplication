package widgets.katItems;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import hilfklassen.DBHandler;
import hilfklassen.Kategorie;
import hilfklassen.Spieler;
import hilfklassen.Statistik;
import hilfklassen.Statistikwerte;
import szut.de.statistikapplication.R;

/**
 * Created by roese on 24.03.2015.
 */
public class KatItemFliessZahl extends LinearLayout{

    private Context context;

    private EditText fliesszahlEdit;

    private Statistikwerte statistikwert;

    public KatItemFliessZahl(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context);
        init(context, statistik, spieler, kategorie);
    }

    public KatItemFliessZahl(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs);
        init(context, statistik, spieler, kategorie);
    }

    public void init(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie){

        this.context = context;

        DBHandler dbHandler = new DBHandler(context, null, null, 1);

        statistikwert = dbHandler.findStatistikwert(statistik.getId(), spieler.getId(), kategorie.getId());
        statistikwert.setWert("0,00");
        dbHandler.update(statistikwert);

        dbHandler.close();

        initView(context);
        createListener();
    }

    private void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_fliesszahleingabe, this);

        fliesszahlEdit = (EditText) this.findViewById(R.id.fliesszahlText);

    }

    private void createListener() {


    }


}
