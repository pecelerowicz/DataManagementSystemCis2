package gov.ncbj.nomaten.datamanagementbackend.model;

import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XrdFolderStructure {
    private PathNode folderStructure;
    private boolean canRead;
    private boolean canDownload;
    private boolean canModifyContent;
    private boolean canModifyAuthorities;
}
