package gov.ncbj.nomaten.datamanagementbackend.dto.my_project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String ownerName;
    private List<String> memberNames;
    private List<ProjectInfoResponse> projectInfoResponseList;
}
