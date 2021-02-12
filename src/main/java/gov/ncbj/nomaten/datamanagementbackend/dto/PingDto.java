package gov.ncbj.nomaten.datamanagementbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PingDto {
    private Long id;
    private String name;
    private String description;
    private String number;
}
