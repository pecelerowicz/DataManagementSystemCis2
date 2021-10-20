package gov.ncbj.nomaten.datamanagementbackend.dto.my_project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProjectInfoRequest {
    private String userName;
    private String infoName;
    private Long projectId;
}
