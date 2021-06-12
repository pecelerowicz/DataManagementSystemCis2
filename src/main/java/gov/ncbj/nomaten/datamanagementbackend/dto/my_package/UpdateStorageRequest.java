package gov.ncbj.nomaten.datamanagementbackend.dto.my_package;

import lombok.Data;

@Data
public class UpdateStorageRequest {
    private String oldName;
    private String newName;
}
