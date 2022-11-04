package baluni.implementation.comparators;

import baluni.model.Fajl;

import java.util.Comparator;

public class SortByCreationDate implements Comparator<Fajl> {

    @Override
    public int compare(Fajl o1, Fajl o2) {
        return o1.getCreationDate().compareTo(o2.getCreationDate());
    }
}
