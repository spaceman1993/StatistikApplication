package widgets.katItems;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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
public class KatItemNotiz extends KatItem{

    private Context context;

    private ImageView icon;
    private TextView beschreibung;

    public KatItemNotiz(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, statistik, spieler, kategorie);
    }

    public KatItemNotiz(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs, statistik, spieler, kategorie);
    }

    public void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.build_notiz, this);

        icon = (ImageView) this.findViewById(R.id.icon);
        beschreibung = (TextView) this.findViewById(R.id.beschreibung);

        icon.setImageBitmap(kategorie.getFoto());
        beschreibung.setText(kategorie.getName());

        findViewById(R.id.katItemView).setOnClickListener(this);
        findViewById(R.id.katItemView).setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("NOTIZ");

        final EditText input = new EditText(getContext());
        input.setSingleLine(false);
        input.setLines(8);
        input.setGravity(Gravity.TOP | Gravity.LEFT);

        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DBHandler dbHandler = new DBHandler(getContext(), null, null, 1);
                        String notiz = input.getText().toString();
                        statistikwert.setWert(notiz);
                        dbHandler.update(statistikwert);
                        dbHandler.close();
                    }
                });

        alertDialog.show();
    }

    @Override
    public boolean onLongClick(View v) {
        //long clicked
        return true;
    }

    public void setIconImage(Drawable image){
        icon.setImageDrawable(image);
    }

    public void setBeschreibungstext(String text){
        beschreibung.setText(text);
    }

}
