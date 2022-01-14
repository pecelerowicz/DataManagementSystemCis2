package gov.ncbj.nomaten.datamanagementbackend.rest_client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Dto {
    private String id;
    private String firstName;
    private String secondName;
}
