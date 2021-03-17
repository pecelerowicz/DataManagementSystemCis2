package gov.ncbj.nomaten.datamanagementbackend.dto.my_storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMetadataRequest {
    private String metadataName;
}
