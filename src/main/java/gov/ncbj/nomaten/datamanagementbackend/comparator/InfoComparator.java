package gov.ncbj.nomaten.datamanagementbackend.comparator;

import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;

import java.util.Comparator;

public class InfoComparator implements Comparator<Info> {
    @Override
    public int compare(Info i1, Info i2) {
        return i1.getInfoName().compareTo(i2.getInfoName());
    }
}
