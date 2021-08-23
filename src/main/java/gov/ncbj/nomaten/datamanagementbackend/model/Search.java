package gov.ncbj.nomaten.datamanagementbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Search implements Comparable<Search> {
    private String name;
    private String username;
    private boolean hasStorage;

    @Override
    public int compareTo(Search that) {
        if(this.username.compareTo(that.username) > 0) {
            return 1;
        } else if(this.username.compareTo(that.username) < 0) {
            return -1;
        } else {
            return this.name.compareTo(that.name);
        }
    }
}
