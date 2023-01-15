package gov.ncbj.nomaten.datamanagementbackend.comparator;

import gov.ncbj.nomaten.datamanagementbackend.model.Package;

import java.util.Comparator;

public class PackageComparator implements Comparator<Package> {
    @Override
    public int compare(Package p1, Package p2) {
        if(!p1.isHasMetadata() && !p2.isHasMetadata()) {
            return p1.getName().compareTo(p2.getName());
        } else if(p1.isHasMetadata() && p2.isHasMetadata()) {
            if(p1.isArchived() && p2.isArchived()) {
                return -p1.getLocalDateTime().compareTo(p2.getLocalDateTime());
            } else if(!p1.isArchived() && !p2.isArchived()) {
                return -p1.getLocalDateTime().compareTo(p2.getLocalDateTime());
            } else if(p1.isArchived() && !p2.isArchived()) {
                return 1;
            } else {
                return -1;
            }
        } else if(p1.isHasMetadata() && !p2.isHasMetadata()) {
            return 1;
        } else {
            return -1;
        }
    }
}
