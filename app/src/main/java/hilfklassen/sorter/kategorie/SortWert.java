package hilfklassen.sorter.kategorie;

import java.util.Comparator;

import database.data.Kategorie;
import database.data.Statistikwerte;
import hilfklassen.sorter.Sorter;

/**
 * Created by roese on 20.04.2015.
 */
public class SortWert extends Sorter implements Comparator<Statistikwerte> {
    @Override
    public int compare(Statistikwerte s1, Statistikwerte s2) {
        return s1.getWert().compareTo(s2.getWert());
    }
}
