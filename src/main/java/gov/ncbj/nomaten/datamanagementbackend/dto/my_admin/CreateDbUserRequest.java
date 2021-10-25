package gov.ncbj.nomaten.datamanagementbackend.dto.my_admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDbUserRequest {
    private String email;
    private String username;
    private String password;
}
