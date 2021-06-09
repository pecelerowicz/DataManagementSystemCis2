package gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestInfoResponse {
    private String infoName;
    private String message;
}
