package gov.ncbj.nomaten.datamanagementbackend.dto.my_projects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveUserFromOwnedProjectResponse {
    private Long projectId;
    private String name;
    private String description;
    private LocalDate localDate;
    private String ownerName;
    private List<String> memberNames;
    private List<ProjectInfoResponse> projectInfoResponseList;
}
