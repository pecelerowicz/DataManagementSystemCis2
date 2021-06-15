package gov.ncbj.nomaten.datamanagementbackend.dto.my_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.GetDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.GetDifrInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.GetTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.GetTestInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetInfoResponse {
    private String infoName;

    private Info.Access access;
    private String shortName;
    private String longName;
    private String description;
    private GetDifrInfoResponse getDifrInfoResponse;
    private GetTestInfoResponse getTestInfoResponse;
}


