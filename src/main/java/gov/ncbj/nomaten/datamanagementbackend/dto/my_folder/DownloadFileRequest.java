package gov.ncbj.nomaten.datamanagementbackend.dto.my_folder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadFileRequest {
    private String packageName;
    private String folderPath;
    private String fileName;
}
