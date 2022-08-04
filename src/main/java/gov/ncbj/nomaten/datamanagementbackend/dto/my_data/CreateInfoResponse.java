package gov.ncbj.nomaten.datamanagementbackend.dto.my_data;

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
public class CreateInfoResponse {
    private String infoName;

    private Info.Access access;
    private String title;
    private String shortDescription;
    private String description;
    private LocalDate localDate;
}
