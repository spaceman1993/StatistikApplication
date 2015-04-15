package hilfklassen.sorter;

import java.util.Comparator;

import database.data.Kategorie;
import database.data.Spieler;

/**
 * Created by roese on 14.04.2015.
 */
public class SortName implements Comparator<Kategorie> {
    @Override
    public int compare(Kategorie k1, Kategorie k2) {
        return k1.getName().compareTo(k2.getName());
    }
}