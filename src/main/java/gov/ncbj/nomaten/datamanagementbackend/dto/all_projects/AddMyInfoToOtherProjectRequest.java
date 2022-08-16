package gov.ncbj.nomaten.datamanagementbackend.dto.all_projects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddMyInfoToOtherProjectRequest {
    private String infoName;
    private Long projectId;
}
