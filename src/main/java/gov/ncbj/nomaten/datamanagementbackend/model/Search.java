package gov.ncbj.nomaten.datamanagementbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Search implements Comparable<Search> {
    private String name;
    private String username;
    private boolean hasStorage;
    private LocalDateTime localDateTime;

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
