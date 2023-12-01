package gov.ncbj.nomaten.datamanagementbackend.dto.my_tem;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemFolderStructure {
    private PathNode folderStructure;
    private boolean canRead;
    private boolean canDownload;
    private boolean canModifyContent;
    private boolean canModifyAuthorities;
}
