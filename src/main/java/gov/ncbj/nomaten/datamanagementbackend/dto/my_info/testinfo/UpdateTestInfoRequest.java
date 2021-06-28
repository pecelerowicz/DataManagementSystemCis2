package gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateTestInfoRequest {
    private String infoName;

    private String testField1;
    private String testField2;
    private String testField3;
    private String testField4;
    private String testField5;
}
