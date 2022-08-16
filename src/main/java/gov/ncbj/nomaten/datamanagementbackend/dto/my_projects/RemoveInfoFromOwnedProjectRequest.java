package gov.ncbj.nomaten.datamanagementbackend.dto.my_projects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveInfoFromOwnedProjectRequest {
    private Long projectId;

    private String username;
    private String infoName;
}
