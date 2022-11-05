package baluni.implementation.comparators;

import baluni.model.Fajl;

import java.util.Comparator;

public class SortByCreationDate implements Comparator<Fajl> {

    private boolean sortingFlag = true;

    public SortByCreationDate(boolean sortingFlag){
        this.sortingFlag = sortingFlag;
    }

    @Override
    public int compare(Fajl o1, Fajl o2) {
        int comparationResult = o1.getCreationDate().compareTo(o2.getCreationDate());

        if(sortingFlag){
            return comparationResult;
        }else{
            return comparationResult * (-1);
        }
    }
}
