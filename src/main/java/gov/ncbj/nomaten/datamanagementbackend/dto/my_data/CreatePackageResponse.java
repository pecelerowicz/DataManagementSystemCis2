package gov.ncbj.nomaten.datamanagementbackend.dto.my_data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePackageResponse {
    private String createPackageMessage;
}
