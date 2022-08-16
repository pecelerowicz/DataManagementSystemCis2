package gov.ncbj.nomaten.datamanagementbackend.dto.my_data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeletePackageResponse {
    private String deleteMessage;
}
