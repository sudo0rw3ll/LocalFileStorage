package baluni.implementation.comparators;

import baluni.model.Fajl;

import java.util.Comparator;

public class SortByModificationDate implements Comparator<Fajl> {

    private boolean sortingFlag = true;

    public SortByModificationDate(boolean sortingFlag){
        this.sortingFlag = sortingFlag;
    }

    @Override
    public int compare(Fajl o1, Fajl o2) {
        int comparationResult = o1.getModificationDate().compareTo(o2.getModificationDate());

        if(sortingFlag){
            return comparationResult;
        }else{
            return comparationResult * (-1);
        }
    }
}
