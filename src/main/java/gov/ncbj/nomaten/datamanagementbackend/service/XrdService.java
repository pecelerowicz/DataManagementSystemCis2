package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.model.XrdFolderStructure;
import gov.ncbj.nomaten.datamanagementbackend.service.support.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.FolderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.XRD;
import static java.nio.file.FileSystems.getDefault;

@Service
@AllArgsConstructor
public class XrdService {

    private final AuthService authService;

    private final FolderService folderService;

    public XrdFolderStructure getXrdFolderStructure() {
        List<String> authorities = authService.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return XrdFolderStructure.builder() // todo fix
                .canRead(true)
                .canDownload(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_XRD_ADMIN") || authorities.contains("ROLE_XRD_USER"))
                .canModifyContent(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_XRD_ADMIN"))
                .canModifyAuthorities(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_XRD_ADMIN"))
                .folderStructure(folderService.getFolderStructure(getDefault().getPath(XRD)))
                .build();
    }

    // todo: download (extra archivization), upload, grant access
}
