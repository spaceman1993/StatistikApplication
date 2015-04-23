package widgets.katItems;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Spieler;
import database.data.Statistik;
import database.data.Statistikwerte;
import szut.de.statistikapplication.R;

/**
 * Created by roese on 24.03.2015.
 */
public class KatItemFliessZahl extends KatItem{

    private EditText fliesszahlEdit;

    public KatItemFliessZahl(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, statistik, spieler, kategorie);
    }

    public KatItemFliessZahl(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs, statistik, spieler, kategorie);
    }

    public void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_fliesszahleingabe, this);

        fliesszahlEdit = (EditText) this.findViewById(R.id.fliesszahlText);

        findViewById(R.id.katItemView).setOnClickListener(this);
        findViewById(R.id.katItemView).setOnLongClickListener(this);
    }
}
