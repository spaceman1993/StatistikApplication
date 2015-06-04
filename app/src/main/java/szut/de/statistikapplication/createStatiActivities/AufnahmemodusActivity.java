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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_erfassungsart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
