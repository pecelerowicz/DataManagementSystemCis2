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
        return -this.localDateTime.compareTo(that.localDateTime);
    }
}
