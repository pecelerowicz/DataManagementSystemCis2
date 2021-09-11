package gov.ncbj.nomaten.datamanagementbackend.dto.my_project;

import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectInfoResponse {
    private String name;
    private String username;
    public ProjectInfoResponse(Info info) {
        this.name = info.getInfoName();
        this.username = info.getUser().getUsername();
    }
}
