package gov.ncbj.nomaten.datamanagementbackend.dto.my_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileRequest {
    private String packageName;
    private String folderRelativePath;
}
