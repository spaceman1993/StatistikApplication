package hilfklassen.sorter;

import java.util.Comparator;

import database.data.Spieler;

/**
 * Created by roese on 14.04.2015.
 */
public class SortTrikonummer implements Comparator<Spieler> {
    @Override
    public int compare(Spieler s1, Spieler s2) {
        return s1.getTrikonummer() - s2.getTrikonummer();
    }
}