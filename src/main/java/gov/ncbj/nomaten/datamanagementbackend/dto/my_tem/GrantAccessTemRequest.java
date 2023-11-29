package gov.ncbj.nomaten.datamanagementbackend.dto.my_tem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrantAccessTemRequest {
    private String username;
}
