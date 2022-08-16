package gov.ncbj.nomaten.datamanagementbackend.dto.adminauth.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordResponse {
    private String message;
}
