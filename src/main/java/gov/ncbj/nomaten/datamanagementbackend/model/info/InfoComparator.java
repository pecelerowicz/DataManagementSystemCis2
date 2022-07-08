package gov.ncbj.nomaten.datamanagementbackend.model.info;

import java.util.Comparator;

public class InfoComparator implements Comparator<Info> {
    @Override
    public int compare(Info info1, Info info2) {
        return info1.getInfoName().compareTo(info2.getInfoName());
    }
}
