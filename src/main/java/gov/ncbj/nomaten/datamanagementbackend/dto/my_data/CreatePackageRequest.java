package gov.ncbj.nomaten.datamanagementbackend.dto.my_data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePackageRequest {
    private String packageName;
}
