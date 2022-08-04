package gov.ncbj.nomaten.datamanagementbackend.dto.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.difrinfo.UpdateDifrInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.testinfo.UpdateTestInfoResponse;
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
public class UpdateInfoResponse {
    private String infoName;
    private Info.Access access;
    private String title;
    private String shortDescription;
    private String description;
    private LocalDate localDate;
    private UpdateDifrInfoResponse updateDifrInfoResponse;
    private UpdateTestInfoResponse updateTestInfoResponse;
}
