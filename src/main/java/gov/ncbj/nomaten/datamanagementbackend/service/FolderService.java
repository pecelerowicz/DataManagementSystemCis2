package gov.ncbj.nomaten.datamanagementbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static gov.ncbj.nomaten.datamanagementbackend.util.DataManipulation.STORAGE;
import static gov.ncbj.nomaten.datamanagementbackend.util.DataManipulation.readFolderStructure;
import static java.nio.file.FileSystems.getDefault;

@Service
public class FolderService {

    @Autowired
    private AuthService authService;

    public PathNode getFolderStructure() {
        return readFolderStructure(authService.getCurrentUser());
    }

    public String createFolder(String newFolderName, String parentFolderFullPath) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path newFolderPath = getDefault().getPath(STORAGE, userName, parentFolderFullPath, newFolderName);
        Path createdFolderPath = Files.createDirectory(newFolderPath);
        return createdFolderPath.toString();
    }

    public void deleteFolder(String packageName, String folderPathString) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path folderPath = getDefault().getPath(STORAGE, userName, packageName, folderPathString);
        Files.walk(folderPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    // TODO validate (and then append) relativePath. We only want to place files/folders in already existing folders
    public void uploadFile(MultipartFile file, String relativePath) throws IOException {
        Path rootPathStorage = getDefault().getPath(relativePath, file.getOriginalFilename());
        file.transferTo(rootPathStorage);
    }

}



