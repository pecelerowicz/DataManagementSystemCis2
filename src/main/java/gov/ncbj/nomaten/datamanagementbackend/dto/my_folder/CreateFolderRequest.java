package gov.ncbj.nomaten.datamanagementbackend.dto.my_folder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFolderRequest {
    private String newFolderName;
    private String packageName;
    private String parentFolderRelativePath;
}
