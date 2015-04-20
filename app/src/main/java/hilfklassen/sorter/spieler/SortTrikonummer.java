package hilfklassen.sorter.spieler;

import java.util.Comparator;

import database.data.Spieler;
import hilfklassen.sorter.Sorter;

/**
 * Created by roese on 14.04.2015.
 */
public class SortTrikonummer extends Sorter implements Comparator<Spieler> {
    @Override
    public int compare(Spieler s1, Spieler s2) {
        return s1.getTrikonummer() - s2.getTrikonummer();
    }
}