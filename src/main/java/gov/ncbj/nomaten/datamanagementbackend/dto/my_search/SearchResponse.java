package gov.ncbj.nomaten.datamanagementbackend.dto.my_search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse {
    private String name;
    private String username;
    private boolean hasStorage;
}
