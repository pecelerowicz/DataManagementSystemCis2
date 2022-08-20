package gov.ncbj.nomaten.datamanagementbackend.dto.my_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageResponse {
    private String name;
    private boolean hasStorage;
    private boolean hasMetadata;
    private boolean archived;
    private String title;
    private String shortDescription;
    private LocalDate localDate;
}
