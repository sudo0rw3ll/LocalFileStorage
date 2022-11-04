package baluni.implementation.comparators;

import baluni.model.Fajl;

import java.util.Comparator;

public class SortByName implements Comparator<Fajl> {

    @Override
    public int compare(Fajl fajl1, Fajl fajl2) {
       return fajl1.getFileName().compareTo(fajl2.getFileName());
    }
}
