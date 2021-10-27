package gov.ncbj.nomaten.datamanagementbackend.dto.my_admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersResponse {
    private List<String> users;
}
