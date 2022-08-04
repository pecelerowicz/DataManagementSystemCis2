package gov.ncbj.nomaten.datamanagementbackend.dto.adminauth.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteUserResponse {
    private String message;
}