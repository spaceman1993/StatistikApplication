package widgets.katItems;

import android.content.Context;
import android.util.AttributeSet;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Spieler;
import database.data.Statistik;
import database.data.Statistikwerte;

/**
 * Created by Spaceman on 23.04.2015.
 */
public interface KatItemImpl {

    Statistikwerte statistikwert = null;

    void init(Context context, Statistik statistik, Spieler spieler, Kategorie kategorie);

    void initView(Context context);
}
