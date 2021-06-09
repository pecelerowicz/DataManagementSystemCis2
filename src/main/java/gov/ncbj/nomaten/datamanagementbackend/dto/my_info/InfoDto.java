package gov.ncbj.nomaten.datamanagementbackend.dto.my_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.DifrInfoDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.TestInfoDto;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoDto {
    private String infoName;
    private Info.Access access;
    private String shortName;
    private String longName;
    private String description;
    private DifrInfoDto difrInfoDto;
    private TestInfoDto testInfoDto;
}


