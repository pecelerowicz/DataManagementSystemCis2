package gov.ncbj.nomaten.datamanagementbackend.service.action;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.GrantAccessTemRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.GrantAccessTemResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.TemFolderStructure;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.repository.UserRepository;
import gov.ncbj.nomaten.datamanagementbackend.service.support.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.ZipService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.*;
import static java.nio.file.FileSystems.getDefault;

@Service
@AllArgsConstructor
public class TemService {
    private final AuthService authService;
    private final FolderService folderService;
    private final ZipService zipService;
    private final UserRepository userRepository;

    public TemFolderStructure getFolderStructure() {
        return TemFolderStructure.builder() // todo test
                .canRead(true)
                .canDownload(authService.isAuthorizedAsEither("ROLE_ADMIN", "ROLE_TEM_ADMIN", "ROLE_TEM_USER"))
                .canModifyContent(authService.isAuthorizedAsEither("ROLE_ADMIN", "ROLE_TEM_ADMIN"))
                .canModifyAuthorities(authService.isAuthorizedAsEither("ROLE_ADMIN", "ROLE_TEM_ADMIN"))
                .folderStructure(folderService.getFolderStructure(getDefault().getPath(STORAGE, TEM)))
                .build();
    }

    public Resource createFileResource(String fileNameWithPath) {
        authService.isAuthorizedAsEitherOrThrow("ROLE_ADMIN", "ROLE_TEM_ADMIN", "ROLE_TEM_USER");
        Path resourcePath = getDefault().getPath(STORAGE, TEM, fileNameWithPath);
        // TODO check if: resourcePath exists, resourcePath is a file

        return folderService.retrieveResource(resourcePath);
    }

    @Transactional
    public Resource createZipFileResource(String fileNameWithPath) {
        authService.isAuthorizedAsEitherOrThrow("ROLE_ADMIN", "ROLE_TEM_ADMIN", "ROLE_TEM_USER");

        Path sourcePath = FileSystems.getDefault().getPath(STORAGE, TEM, fileNameWithPath);
        Path outputPath = FileSystems.getDefault().getPath(STORAGE, ZIP, "archive-file-" + UUID.randomUUID() + ".zip");
        // TODO check if: sourcePath exists, sourcePath is a file, outputPath doesn't exist

        zipService.zipFile(sourcePath, outputPath);
        // TODO check if: outputPath exists

        return folderService.retrieveResource(outputPath);
    }

    @Transactional
    public Resource createZipFilesResource(List<String> fileNamesWithPaths) {
        authService.isAuthorizedAsEitherOrThrow("ROLE_ADMIN", "ROLE_TEM_ADMIN", "ROLE_TEM_USER");

        List<Path> sourcePaths = fileNamesWithPaths.stream()
                .map(n -> FileSystems.getDefault().getPath(STORAGE, TEM, n))
                .collect(Collectors.toList());
        Path outputPath = FileSystems.getDefault().getPath(STORAGE, ZIP, "archive-files-" + UUID.randomUUID() + ".zip");
        // TODO check if: all source paths exist (done, check), all source paths are files, output path doesn't exist
        sourcePaths.forEach(folderService::itemExistsOrThrow); // todo fix

        zipService.zipFiles(sourcePaths, outputPath);
        // TODO check if: outputPath exists

        return folderService.retrieveResource(outputPath);
    }

    public Resource createZipFolderResource(String folderNameWithPath) {
        authService.isAuthorizedAsEitherOrThrow("ROLE_ADMIN", "ROLE_TEM_ADMIN", "ROLE_TEM_USER");

        Path sourcePath = FileSystems.getDefault().getPath(STORAGE, TEM, folderNameWithPath);
        Path outputPath = FileSystems.getDefault().getPath(STORAGE, ZIP, "archive-folder-" + UUID.randomUUID() + ".zip");
        // TODO check if: sourcePath exists, sourcePath is a folder, outputPath doesn't exist

        zipService.zipFolder(sourcePath, outputPath);
        // TODO check if: outputPath exists

        return folderService.retrieveResource(outputPath);
    }

    @Transactional
    public GrantAccessTemResponse grantAccessTem(GrantAccessTemRequest grantAccessTemRequest) {
        authService.isAuthorizedAsEitherOrThrow("ROLE_ADMIN", "ROLE_TEM_ADMIN");
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

    // todo: upload
}
