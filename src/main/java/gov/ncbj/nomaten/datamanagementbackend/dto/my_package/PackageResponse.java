package gov.ncbj.nomaten.datamanagementbackend.dto.my_package;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageResponse {
    private String name;
    private boolean hasStorage;
    private boolean hasMetadata;
}
