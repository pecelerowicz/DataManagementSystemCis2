package gov.ncbj.nomaten.datamanagementbackend.dto.my_package;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteInfoResponse {
    private String deleteMessage;
}
