package gov.ncbj.nomaten.datamanagementbackend.dto.shared_info_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.difrinfo.GetDifrInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.testinfo.GetTestInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetInfoResponse {
    private String infoName;
    private boolean archived; // TODO it should probably disappear
    private Info.Access access;
    private String title;
    private String shortDescription;
    private String description;
    private LocalDate localDate;
    private GetDifrInfoResponse getDifrInfoResponse;
    private GetTestInfoResponse getTestInfoResponse;
}


