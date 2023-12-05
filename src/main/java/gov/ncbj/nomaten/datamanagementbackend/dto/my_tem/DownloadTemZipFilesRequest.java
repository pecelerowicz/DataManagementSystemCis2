package gov.ncbj.nomaten.datamanagementbackend.dto.my_tem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownloadTemZipFilesRequest {
    private List<String> fileNamesWithPaths;
}
