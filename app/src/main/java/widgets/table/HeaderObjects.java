package widgets.table;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import database.data.Kategorie;
import hilfklassen.sorter.kategorie.SortWert;
import szut.de.statistikapplication.R;
import szut.de.statistikapplication.createMannschaftActivities.NewSpielerActivity;

/**
 * Created by roese on 26.03.2015.
 */
public class HeaderObjects {

    private Activity activity;

    private ArrayList<Kategorie> kategorien;

    private ArrayList<View> headerViews = new ArrayList<>();



    public HeaderObjects(Activity activity, ArrayList<Kategorie> kategorien){

        this.activity = activity;
        this.kategorien = kategorien;

        initHeaderViews();
    }



    public void initHeaderViews(){

        TextView firstHeader = new TextView(activity.getApplicationContext());
        firstHeader.setMinimumWidth(280);
        firstHeader.setBackgroundResource(R.drawable.tableheader);
        headerViews.add(firstHeader);

        for(int i=0; i<kategorien.size(); i++){
            View v = getKategorienView(kategorien.get(i));
//            if(i == kategorien.size()-1) {
//                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight() + 250, v.getPaddingBottom());
//            }
            v.setMinimumWidth(180);
            headerViews.add(v);
        }
    }

    public View getKategorienView(final Kategorie kategorie){
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.table_kategorien, null);

        ImageView icon = (ImageView) v.findViewById(R.id.image_view);
        TextView kategoriename = (TextView) v.findViewById(R.id.kategorie);

        try{
            icon.setImageBitmap(kategorie.getFoto());
            kategoriename.setText(kategorie.getName());
        }catch (Exception e){

        }
        v.setBackgroundResource(R.drawable.tableheader);
        v.setPadding(v.getPaddingLeft(), v.getPaddingTop()+40, v.getPaddingRight(), v.getPaddingBottom());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    public ArrayList<View> getHeaderViews() {
        return headerViews;
    }
}
