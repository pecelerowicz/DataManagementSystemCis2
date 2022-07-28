package gov.ncbj.nomaten.datamanagementbackend.comparator;

import gov.ncbj.nomaten.datamanagementbackend.model.Search;

import java.util.Comparator;

public class SearchComparator implements Comparator<Search> {
    @Override
    public int compare(Search s1, Search s2) {
        return -s1.getLocalDateTime().compareTo(s2.getLocalDateTime());
    }
}
