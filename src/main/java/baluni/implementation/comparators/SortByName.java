package baluni.implementation.comparators;

import baluni.model.Fajl;

import java.util.Comparator;

public class SortByName implements Comparator<Fajl> {

    private boolean sortingFlag;

    public SortByName(boolean sortingFlag){
        this.sortingFlag = sortingFlag;
    }

    @Override
    public int compare(Fajl fajl1, Fajl fajl2) {
       int comparationResult = fajl1.getFileName().compareTo(fajl2.getFileName());

       if(sortingFlag){
           return comparationResult;
       }else{
           return  comparationResult * (-1);
       }
    }
}
