package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.xrd.GrantAccessXrdRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.xrd.GrantAccessXrdResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.XrdFolderStructure;
import gov.ncbj.nomaten.datamanagementbackend.repository.UserRepository;
import gov.ncbj.nomaten.datamanagementbackend.service.support.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.FolderService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.XRD;
import static java.nio.file.FileSystems.getDefault;

@Service
@AllArgsConstructor
public class XrdService {

    private final AuthService authService;

    private final FolderService folderService;

    private final UserRepository userRepository;

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

    public Resource downloadXrdFile(String fileNameWithPath) {
        List<String> authorities = authService.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if(!(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_XRD_ADMIN") || authorities.contains("ROLE_XRD_USER"))) {
            throw new RuntimeException("No access for the resource");
        }
        return folderService.downloadXrdFile(fileNameWithPath);
    }

    // todo: (download with archivization), upload

    @Transactional
    public GrantAccessXrdResponse grantAccessXrd(GrantAccessXrdRequest grantAccessXrdRequest) {
        List<String> authorities = authService.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if(!(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_XRD_ADMIN"))) {
            throw new RuntimeException("No access for the action");
        }
        User user = userRepository.findByUsername(grantAccessXrdRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("No user " + grantAccessXrdRequest.getUsername()));
        List<String> listOfRoles = Arrays.stream(user.getRoles().split(";")).collect(Collectors.toList());
        if(listOfRoles.contains("ROLE_ADMIN") || listOfRoles.contains("ROLE_XRD_ADMIN") || listOfRoles.contains("ROLE_XRD_USER")) {
            throw new RuntimeException("Access already granted");
        }
        String newRoles = user.getRoles().concat(";ROLE_XRD_USER");
        user.setRoles(newRoles);
        return GrantAccessXrdResponse.builder().message("Access granted").build();
    }
}
