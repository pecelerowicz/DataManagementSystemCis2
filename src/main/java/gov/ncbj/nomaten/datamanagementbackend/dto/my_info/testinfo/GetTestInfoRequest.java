package gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetTestInfoRequest {
    private String infoName;
}
