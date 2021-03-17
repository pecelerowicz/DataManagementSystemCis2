package gov.ncbj.nomaten.datamanagementbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageAndMetadata implements Comparable<StorageAndMetadata>{ // TODO hasStorage = false; hasMetadata = false (NOT AT THE SAME TIME).
    private String name;
    private boolean hasStorage;
    private boolean hasMetadata;

    @Override
    public int compareTo(StorageAndMetadata that) {
        return this.name.compareTo(that.name);
    }
}
