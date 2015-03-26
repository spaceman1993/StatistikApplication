package widgets.katItems;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class KatItemNotiz extends LinearLayout{

    private Context context;

    private ImageView icon;
    private TextView beschreibung;

    private Statistikwerte statistikwert;

    public KatItemNotiz(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context);
        init(context, statistik, spieler, kategorie);
    }

    public KatItemNotiz(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs);
        init(context, statistik, spieler, kategorie);
    }



    public void init(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie){

        DBHandler dbHandler = new DBHandler(context, null, null, 1);

        statistikwert = dbHandler.findStatistikwert(statistik.getId(), spieler.getId(), kategorie.getId());
        statistikwert.setWert("");
        dbHandler.update(statistikwert);

        dbHandler.close();

        this.context = context;
        initView(context);
        createListener();
    }

    private void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_notiz, this);

        icon = (ImageView) this.findViewById(R.id.icon);
        beschreibung = (TextView) this.findViewById(R.id.beschreibung);
    }


    private void createListener(){

        View.OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("NOTIZ");

                final EditText input = new EditText(context);
                input.setSingleLine(false);
                input.setLines(8);
                input.setGravity(Gravity.TOP | Gravity.LEFT);

                alertDialog.setView(input);

                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBHandler dbHandler = new DBHandler(context, null, null, 1);
                                String notiz = input.getText().toString();
                                statistikwert.setWert(notiz);
                                dbHandler.update(statistikwert);
                                dbHandler.close();
                            }
                        });

                alertDialog.show();
            }
        };

        icon.setOnClickListener(onClickListener);
        beschreibung.setOnClickListener(onClickListener);

    }

    public void setIconImage(Drawable image){
        icon.setImageDrawable(image);
    }

    public void setBeschreibungstext(String text){
        beschreibung.setText(text);
    }

}
