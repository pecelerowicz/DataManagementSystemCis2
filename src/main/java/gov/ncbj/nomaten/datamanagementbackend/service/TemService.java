package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.GrantAccessTemRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.GrantAccessTemResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.TemFolderStructure;
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

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.TEM;
import static java.nio.file.FileSystems.getDefault;

@Service
@AllArgsConstructor
public class TemService {

    private final AuthService authService;

    private final FolderService folderService;

    private final UserRepository userRepository;

    public TemFolderStructure getTemFolderStructure() {
        List<String> authorities = authService.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return TemFolderStructure.builder() // todo fix
                .canRead(true)
                .canDownload(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_TEM_ADMIN") || authorities.contains("ROLE_TEM_USER"))
                .canModifyContent(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_TEM_ADMIN"))
                .canModifyAuthorities(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_TEM_ADMIN"))
                .folderStructure(folderService.getFolderStructure(getDefault().getPath(TEM)))
                .build();
    }

    public Resource downloadTemFile(String fileNameWithPath) {
        List<String> authorities = authService.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if(!(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_TEM_ADMIN") || authorities.contains("ROLE_TEM_USER"))) {
            throw new RuntimeException("No access for the resource");
        }
        return folderService.downloadTemFile(fileNameWithPath);
    }

    // todo: (download with archivization), upload

    @Transactional
    public GrantAccessTemResponse grantAccessTem(GrantAccessTemRequest grantAccessTemRequest) {
        List<String> authorities = authService.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if(!(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_TEM_ADMIN"))) {
            throw new RuntimeException("No access for the action");
        }
        User user = userRepository.findByUsername(grantAccessTemRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("No user " + grantAccessTemRequest.getUsername()));
        List<String> listOfRoles = Arrays.stream(user.getRoles().split(";")).collect(Collectors.toList());
        if(listOfRoles.contains("ROLE_ADMIN") || listOfRoles.contains("ROLE_TEM_ADMIN") || listOfRoles.contains("ROLE_TEM_USER")) {
            throw new RuntimeException("Access already granted");
        }
        String newRoles = user.getRoles().concat(";ROLE_TEM_USER");
        user.setRoles(newRoles);
        return GrantAccessTemResponse.builder().message("Access granted").build();
    }
}
