package database.data;

/**
 * Created by roese on 24.03.2015.
 */
public class Statistikwerte {

    private int id;
    private int statistikId;
    private int spielerId;
    private int kategorieId;
    private String wert;

    public Statistikwerte(){

    }

    public Statistikwerte(int statistikId, int spielerId, int kategorieId, String wert){
        this.statistikId = statistikId;
        this.spielerId = spielerId;
        this.kategorieId = kategorieId;
        this.wert = wert;
    }

    public Statistikwerte(int id, int statistikId, int spielerId, int kategorieId, String wert){
        this.id = id;
        this.statistikId = statistikId;
        this.spielerId = spielerId;
        this.kategorieId = kategorieId;
        this.wert = wert;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatistikId() {
        return statistikId;
    }

    public void setStatistikId(int statistikId) {
        this.statistikId = statistikId;
    }

    public int getSpielerId() {
        return spielerId;
    }

    public void setSpielerId(int spielerId) {
        this.spielerId = spielerId;
    }

    public int getKategorieId() {
        return kategorieId;
    }

    public void setKategorieId(int kategorieId) {
        this.kategorieId = kategorieId;
    }

    public String getWert() {
        return wert;
    }

    public void setWert(String wert) {
        this.wert = wert;
    }
}
