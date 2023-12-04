package gov.ncbj.nomaten.datamanagementbackend.dto.my_zip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownloadTemZipFileRequest {
    private String fileNameWithPath;
}
