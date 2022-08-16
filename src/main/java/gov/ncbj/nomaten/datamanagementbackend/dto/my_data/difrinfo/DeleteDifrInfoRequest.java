package gov.ncbj.nomaten.datamanagementbackend.dto.my_data.difrinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DeleteDifrInfoRequest {
    private String infoName;
}
