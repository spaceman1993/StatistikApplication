package widgets.katItems;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
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
public class KatItemCheckbox extends KatItem{

    private ImageView icon;
    private CheckBox checkBox;
    private TextView beschreibung;

    public KatItemCheckbox(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, statistik, spieler, kategorie);
    }

    public KatItemCheckbox(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs, statistik, spieler, kategorie);
    }

    public void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_checkbox, this, true);

        icon = (ImageView) this.findViewById(R.id.icon);
        checkBox = (CheckBox) this.findViewById(R.id.wert);
        beschreibung = (TextView) this.findViewById(R.id.beschreibung);

        icon.setImageBitmap(kategorie.getFoto());
        beschreibung.setText(kategorie.getName());
        if(statistikwert.getWert().equals("1")) {
            checkBox.setChecked(true);
        }
        else{
            checkBox.setChecked(false);
        }


        findViewById(R.id.katItemView).setOnClickListener(this);
        findViewById(R.id.katItemView).setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        DBHandler dbHandler = new DBHandler(getContext(), null, null, 1);

        if(checkBox.isChecked()){
            checkBox.setChecked(false);
            statistikwert.setWert("0");
        }
        else{
            checkBox.setChecked(true);
            statistikwert.setWert("1");
        }

        dbHandler.update(statistikwert);
        dbHandler.close();

    }

    @Override
    public boolean onLongClick(View v) {
        //long clicked
        return true;
    }

}
