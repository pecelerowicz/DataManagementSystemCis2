package gov.ncbj.nomaten.datamanagementbackend.dto.my_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.CreateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.CreateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInfoRequest {
    private String infoName;

    private Info.Access access;
    private String title;
    private String shortDescription;
    private String description;
    private CreateDifrInfoRequest createDifrInfoRequest;
    private CreateTestInfoRequest createTestInfoRequest;
}
