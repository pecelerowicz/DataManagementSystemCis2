package gov.ncbj.nomaten.datamanagementbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoragePackage implements Comparable<StoragePackage>{ // TODO isFolder = false; isPackage = false (NOT AT THE SAME TIME).
    private String name;
    private boolean isStorage;
    private boolean isPackage;

    @Override
    public int compareTo(StoragePackage that) {
        return this.name.compareTo(that.name);
    }
}
