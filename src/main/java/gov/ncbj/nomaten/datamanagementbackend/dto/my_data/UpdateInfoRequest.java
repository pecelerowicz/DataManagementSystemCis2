package gov.ncbj.nomaten.datamanagementbackend.dto.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.difrinfo.CreateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.testinfo.CreateTestInfoRequest;
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
