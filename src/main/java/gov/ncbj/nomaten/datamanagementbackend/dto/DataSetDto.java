package gov.ncbj.nomaten.datamanagementbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataSetDto {
    private Long id;
    private String name;
    private String description;
}
