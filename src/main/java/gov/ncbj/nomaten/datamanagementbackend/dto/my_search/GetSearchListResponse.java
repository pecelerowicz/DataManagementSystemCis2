package gov.ncbj.nomaten.datamanagementbackend.dto.my_search;

import gov.ncbj.nomaten.datamanagementbackend.comparator.SearchComparator;
import gov.ncbj.nomaten.datamanagementbackend.model.Search;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GetSearchListResponse {
    List<SearchResponse> searchResponseList;
    public GetSearchListResponse(List<Search> searchList) {
        this.searchResponseList = searchList
                .stream()
                .sorted(new SearchComparator())
                .map(s -> SearchResponse.builder()
                    .name(s.getName())
                    .username(s.getUsername())
                    .hasStorage(s.isHasStorage())
                    .localDate(s.getLocalDateTime().toLocalDate())
                    .build())
                .collect(Collectors.toList());
    }
}
