package gov.ncbj.nomaten.datamanagementbackend.dto.all_data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse {
    private String name;
    private String username;
    private boolean hasStorage;
    private LocalDate localDate;
}
