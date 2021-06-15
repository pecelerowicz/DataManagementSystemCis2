package gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetDifrInfoRequest {
    private String infoName;
}
