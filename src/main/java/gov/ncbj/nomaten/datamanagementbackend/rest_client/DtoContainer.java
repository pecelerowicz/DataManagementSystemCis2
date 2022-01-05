package gov.ncbj.nomaten.datamanagementbackend.rest_client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoContainer {
    private List<Dto> dtoList;
}
