package szut.de.statistikapplication;

import android.app.Application;

import database.data.Mannschaft;

/**
 * Created by Spaceman on 18.03.2015.
 */
public class Globals extends Application{
    private Mannschaft mannschaft;

    public Mannschaft getMannschaft() {
        return mannschaft;
    }

    public void setMannschaft(Mannschaft mannschaft) {
        this.mannschaft = mannschaft;
    }
}
