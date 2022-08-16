package gov.ncbj.nomaten.datamanagementbackend.comparator;

import gov.ncbj.nomaten.datamanagementbackend.model.Project;

import java.util.Comparator;

public class ProjectComparator implements Comparator<Project> {
    @Override
    public int compare(Project p1, Project p2) {
        return -p1.getLocalDateTime().compareTo(p2.getLocalDateTime());
    }
}
