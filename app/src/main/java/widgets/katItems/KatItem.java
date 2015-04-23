package widgets.katItems;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Spieler;
import database.data.Statistik;
import database.data.Statistikwerte;

/**
 * Created by Spaceman on 23.04.2015.
 */
public abstract class KatItem extends RelativeLayout implements RelativeLayout.OnClickListener, RelativeLayout.OnLongClickListener, KatItemImpl{

    protected Statistikwerte statistikwert;
    protected Context context;

    public KatItem(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie){
        super(context);
        this.context = context;
        init(context, statistik, spieler, kategorie);
    }

    public KatItem(Context context, AttributeSet attrs, Statistik statistik, Spieler spieler, Kategorie kategorie) {
        super(context, attrs);
        this.context = context;
        init(context, statistik, spieler, kategorie);
    }

    public void init(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie){

        DBHandler dbHandler = new DBHandler(context, null, null, 1);

        statistikwert = dbHandler.findStatistikwert(statistik.getId(), spieler.getId(), kategorie.getId());
        statistikwert.setWert("0");
        dbHandler.update(statistikwert);

        dbHandler.close();

        initView(context);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "onClickListener: KatItem", Toast.LENGTH_LONG);
    }

    @Override
    public boolean onLongClick(View v) {
        //long clicked
        Toast.makeText(context, "onLongClickListener: KatItem", Toast.LENGTH_LONG);
        return true;
    }

}
