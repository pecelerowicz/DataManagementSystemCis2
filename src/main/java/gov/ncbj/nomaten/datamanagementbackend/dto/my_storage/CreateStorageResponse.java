package gov.ncbj.nomaten.datamanagementbackend.dto.my_storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStorageResponse {
    private String createStorageMessage;
}
