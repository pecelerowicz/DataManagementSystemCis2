package gov.ncbj.nomaten.datamanagementbackend.dto.my_info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteInfoRequest {
    private String infoName;
}
