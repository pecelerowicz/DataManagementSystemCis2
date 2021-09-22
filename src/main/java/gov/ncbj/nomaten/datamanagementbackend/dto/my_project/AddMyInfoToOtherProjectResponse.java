package gov.ncbj.nomaten.datamanagementbackend.dto.my_project;

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
public class AddMyInfoToOtherProjectResponse {
    private Long projectId;
    private String name;
    private String description;
    private String ownerName;
    private LocalDate localDate;
    private List<String> memberNames;
    private List<ProjectInfoResponse> projectInfoResponseList;
}
