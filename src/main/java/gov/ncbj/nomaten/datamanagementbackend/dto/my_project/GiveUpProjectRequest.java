package gov.ncbj.nomaten.datamanagementbackend.dto.my_project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiveUpProjectRequest {
    private String projectName;
}
