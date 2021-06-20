package gov.ncbj.nomaten.datamanagementbackend.dto.my_storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteStorageResponse {
    private String deleteStorageMessage;
}
