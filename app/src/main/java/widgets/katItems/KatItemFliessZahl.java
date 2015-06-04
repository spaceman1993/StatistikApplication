package widgets.katItems;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
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
public class KatItemFliessZahl extends KatItem{

    private ImageView icon;
    private TextView beschreibung;
    private EditText fliesszahlEdit;

    public KatItemFliessZahl(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, statistik, spieler, kategorie);
    }

    public KatItemFliessZahl(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs, statistik, spieler, kategorie);
    }

    public void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_fliesszahleingabe, this);

        icon = (ImageView) this.findViewById(R.id.icon);
        beschreibung = (TextView) this.findViewById(R.id.beschreibung);
        fliesszahlEdit = (EditText) this.findViewById(R.id.wert);

        icon.setImageBitmap(kategorie.getFoto());
        beschreibung.setText(kategorie.getName());
        fliesszahlEdit.setText(statistikwert.getWert());

        findViewById(R.id.katItemView).setOnClickListener(this);
        findViewById(R.id.katItemView).setOnLongClickListener(this);

        fliesszahlEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                DBHandler dbHandler = new DBHandler(getContext(), null, null, 1);
                statistikwert.setWert(String.valueOf(fliesszahlEdit.getText()));
                dbHandler.update(statistikwert);
                dbHandler.close();
            }
        });
    }
}
