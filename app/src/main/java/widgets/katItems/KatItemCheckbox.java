package widgets.katItems;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
public class KatItemCheckbox extends LinearLayout{

    private ImageView icon;
    private CheckBox checkBox;
    private TextView beschreibung;

    private Statistikwerte statistikwert;

    public KatItemCheckbox(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context);
        init(context, statistik, spieler, kategorie);
    }

    public KatItemCheckbox(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs);
        init(context, statistik, spieler, kategorie);
    }



    public void init(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie){

        DBHandler dbHandler = new DBHandler(context, null, null, 1);

        statistikwert = dbHandler.findStatistikwert(statistik.getId(), spieler.getId(), kategorie.getId());
        statistikwert.setWert("0");
        dbHandler.update(statistikwert);

        dbHandler.close();

        initView(context);
        createListener();
    }

    private void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_checkbox, this);

        icon = (ImageView) this.findViewById(R.id.icon);
        checkBox = (CheckBox) this.findViewById(R.id.checkbox);
        beschreibung = (TextView) this.findViewById(R.id.beschreibung);
    }


    private void createListener(){

        View.OnClickListener onClickListener = new OnClickListener() {
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
        };

        icon.setOnClickListener(onClickListener);
        beschreibung.setOnClickListener(onClickListener);

    }

}
