package gov.ncbj.nomaten.datamanagementbackend.dto.shared_info_project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProjectsResponse {
    private List<GetProjectResponse> getProjectResponseList;
}
