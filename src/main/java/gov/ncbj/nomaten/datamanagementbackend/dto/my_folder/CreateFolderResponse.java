package gov.ncbj.nomaten.datamanagementbackend.dto.my_folder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateFolderResponse {
    private String newFolderFullName;
}
