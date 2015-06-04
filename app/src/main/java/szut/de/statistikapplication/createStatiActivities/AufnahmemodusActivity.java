package szut.de.statistikapplication.createStatiActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import szut.de.statistikapplication.HauptmenuActivity;
import szut.de.statistikapplication.R;

public class AufnahmemodusActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erfassungsart);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HauptmenuActivity.class);
        startActivity(intent);
    }

    public void live_click(View view){

        Intent intent = new Intent(this, ErfassungsoptionenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("Update", false);
        bundle.putBoolean("Bearbeiten", false);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void nachträglich_click(View view){

        Intent intent = new Intent(this, ErfassungsoptionenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("Update", false);
        bundle.putBoolean("Bearbeiten", true);
        intent.putExtras(bundle);
        startActivity(intent);

    }

}
