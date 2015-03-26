package widgets.table;

import java.util.ArrayList;
import java.util.List;

import hilfklassen.Kategorie;
import hilfklassen.Spieler;
import hilfklassen.Statistikwerte;

public class DataObject {

    private Spieler spieler;
    private ArrayList<Statistikwerte> statistikwerte;

    private String spielerText;
    private ArrayList<String> datas = new ArrayList<String>();


    public DataObject(Spieler spieler, ArrayList<Statistikwerte> statistikwerte){

        this.spieler = spieler;
        this.statistikwerte = statistikwerte;

        initData();
    }

    public void initData(){
        spielerText = spieler.getNachname() + " " + spieler.getVorname();

        datas.add(spielerText);

        for(int i=0; i<statistikwerte.size(); i++){
            datas.add(statistikwerte.get(i).getWert());
        }
    }

    public ArrayList<String> getDatas() {
        return datas;
    }
}