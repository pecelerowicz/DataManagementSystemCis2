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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.*;
import static java.nio.file.FileSystems.getDefault;

@Service
@AllArgsConstructor
public class TemService {
    private final AuthService authService;
    private final FolderService folderService;
    private final ZipService zipService;
    private final UserRepository userRepository;

    public TemFolderStructure getTemFolderStructure() {
        List<String> authorities = authService.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return TemFolderStructure.builder() // todo fix
                .canRead(true)
                .canDownload(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_TEM_ADMIN") || authorities.contains("ROLE_TEM_USER"))
                .canModifyContent(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_TEM_ADMIN"))
                .canModifyAuthorities(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_TEM_ADMIN"))
                .folderStructure(folderService.getFolderStructure(getDefault().getPath(STORAGE, TEM)))
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

    @Transactional
    public Resource createZipResource(String fileNameWithPath) {
        folderService.itemExistsOrThrow(FileSystems.getDefault().getPath(STORAGE, TEM, fileNameWithPath));
        Path zipFilePath = zipService.zipFile(fileNameWithPath);
        return folderService.downloadZipFile(zipFilePath.toString());
    }

    @Transactional
    public Resource createZipResource(List<String> fileNamesWithPaths) {
        fileNamesWithPaths.forEach(fn -> folderService.itemExistsOrThrow(FileSystems.getDefault().getPath(STORAGE, TEM, fn)));

        List<Path> filesToZip = fileNamesWithPaths.stream().map(fp -> getDefault().getPath(STORAGE, TEM, fp)).collect(Collectors.toList());

        Path zipFilePath = FileSystems.getDefault().getPath(STORAGE, ZIP, "archive-" + UUID.randomUUID() + ".zip");

        try {
            zipFiles(filesToZip, zipFilePath);
            System.out.println("Files zipped successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return folderService.downloadZipFile(zipFilePath.toString());
    }


    private static void zipFiles(List<Path> filesToZip, Path zipFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            byte[] buffer = new byte[1024];

            for (Path filePath : filesToZip) {
                if (!Files.exists(filePath)) {
                    System.out.println("File not found: " + filePath);
                    continue;
                }

                ZipEntry zipEntry = new ZipEntry(filePath.toString());
                zos.putNextEntry(zipEntry);

                try (InputStream fis = Files.newInputStream(filePath)) {
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                }

                zos.closeEntry();
            }
        }
    }
}
