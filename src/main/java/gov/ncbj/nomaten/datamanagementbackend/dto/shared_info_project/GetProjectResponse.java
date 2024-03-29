package gov.ncbj.nomaten.datamanagementbackend.dto.shared_info_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_projects.ProjectInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProjectResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate localDate;
    private String ownerName;
    private List<String> memberNames;
    private List<ProjectInfoResponse> projectInfoResponseList;
}
