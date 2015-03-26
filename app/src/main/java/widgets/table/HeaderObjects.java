package widgets.table;

import java.util.ArrayList;
import java.util.List;

import hilfklassen.Kategorie;
import hilfklassen.Spieler;
import hilfklassen.Statistikwerte;

/**
 * Created by roese on 26.03.2015.
 */
public class HeaderObjects {

    private ArrayList<Kategorie> kategorien;

    private ArrayList<String> headers = new ArrayList<String>();


    public HeaderObjects(ArrayList<Kategorie> kategorien){

        this.kategorien = kategorien;

        initData();
    }

    public void initData(){

        headers.add("SPIELER");

        for(int i=0; i<kategorien.size(); i++){
            headers.add(kategorien.get(i).getName());
        }
    }

    public ArrayList<String> getHeaders() {
        return headers;
    }
}
