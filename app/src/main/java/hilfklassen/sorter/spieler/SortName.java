package hilfklassen.sorter.spieler;

import java.util.Comparator;

import database.data.Kategorie;
import database.data.Spieler;
import hilfklassen.sorter.Sorter;

/**
 * Created by roese on 14.04.2015.
 */
public class SortName extends Sorter implements Comparator<Spieler> {
    @Override
    public int compare(Spieler s1, Spieler s2) {
        return s1.getNachname().compareTo(s2.getNachname());
    }
}