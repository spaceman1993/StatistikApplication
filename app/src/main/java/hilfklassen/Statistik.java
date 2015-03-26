package hilfklassen;

import java.util.Date;

/**
 * Created by roese on 24.03.2015.
 */
public class Statistik extends SelectableItem{

    private int mannschaftsid;
    private String datum;
    private int heim;
    private String gegner;
    private int eigeneTore;
    private int gegnerTore;

    public Statistik(){

    }

    public Statistik(int mannschaftsid, String datum, int heim, String gegner, int eigeneTore, int gegnerTore){
        this.mannschaftsid = mannschaftsid;
        this.datum = datum;
        this.heim = heim;
        this.gegner = gegner;
        this.eigeneTore = eigeneTore;
        this.gegnerTore = gegnerTore;
    }

    public Statistik(int id, int mannschaftsid, String datum, int heim, String gegner, int eigeneTore, int gegnerTore){
        this.id = id;
        this.mannschaftsid = mannschaftsid;
        this.datum = datum;
        this.heim = heim;
        this.gegner = gegner;
        this.eigeneTore = eigeneTore;
        this.gegnerTore = gegnerTore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMannschaftsid() {
        return mannschaftsid;
    }

    public void setMannschaftsid(int mannschaftsid) {
        this.mannschaftsid = mannschaftsid;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getHeim() {
        return heim;
    }

    public void setHeim(int heim) {
        this.heim = heim;
    }

    public String getGegner() {
        return gegner;
    }

    public void setGegner(String gegner) {
        this.gegner = gegner;
    }

    public int getEigeneTore() {
        return eigeneTore;
    }

    public void setEigeneTore(int eigeneTore) {
        this.eigeneTore = eigeneTore;
    }

    public int getGegnerTore() {
        return gegnerTore;
    }

    public void setGegnerTore(int gegnerTore) {
        this.gegnerTore = gegnerTore;
    }
}
