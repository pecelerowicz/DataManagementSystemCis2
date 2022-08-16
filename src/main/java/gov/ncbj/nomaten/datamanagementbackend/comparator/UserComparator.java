package gov.ncbj.nomaten.datamanagementbackend.comparator;

import gov.ncbj.nomaten.datamanagementbackend.model.User;

import java.util.Comparator;

public class UserComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        return u1.getUsername().compareTo(u2.getUsername());
    }
}
