package gov.ncbj.nomaten.datamanagementbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Package implements Comparable<Package>{ // TODO hasStorage = false; hasMetadata = false (NOT AT THE SAME TIME).
    private String name;
    private boolean hasStorage;
    private boolean hasMetadata;
    private LocalDateTime localDateTime;

    @Override
    public int compareTo(Package that) {
        return this.name.compareTo(that.name);
    }
}
