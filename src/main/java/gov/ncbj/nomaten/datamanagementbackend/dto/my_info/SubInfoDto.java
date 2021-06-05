package gov.ncbj.nomaten.datamanagementbackend.dto.my_info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubInfoDto {
    private String infoName;
    private String message;
}
