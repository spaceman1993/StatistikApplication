package szut.de.statistikapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import szut.de.statistikapplication.createMannschaftActivities.KaderActivity;
import szut.de.statistikapplication.createMannschaftActivities.KategorieActivity;
import szut.de.statistikapplication.createMannschaftActivities.MannschaftActivity;


public class ConfigActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
    }

    public void mannschaft_click(View view){
        Intent intent = new Intent(this, MannschaftActivity.class);
        intent.putExtra("Update", true);
        startActivity(intent);
    }

    public void kader_click(View view){
        Intent intent = new Intent(this, KaderActivity.class);
        intent.putExtra("Update", true);
        startActivity(intent);
    }

    public void kategorien_click(View view){
        Intent intent = new Intent(this, KategorieActivity.class);
        intent.putExtra("Update", true);
        startActivity(intent);
    }

    public void fertig_click(View view){
        finish();
    }
}
