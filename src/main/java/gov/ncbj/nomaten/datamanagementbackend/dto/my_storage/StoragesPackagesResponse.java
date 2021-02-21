package gov.ncbj.nomaten.datamanagementbackend.dto.my_storage;

import gov.ncbj.nomaten.datamanagementbackend.model.StoragePackage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoragesPackagesResponse {
    List<StoragePackage> storagesPackages;
}
