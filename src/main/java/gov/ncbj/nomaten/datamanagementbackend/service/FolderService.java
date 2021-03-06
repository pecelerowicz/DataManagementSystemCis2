package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_folder.CreateFolderRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.repository.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;

import static gov.ncbj.nomaten.datamanagementbackend.util.DataManipulation.STORAGE;
import static gov.ncbj.nomaten.datamanagementbackend.util.DataManipulation.readFolderStructure;
import static java.nio.file.FileSystems.getDefault;

@Service
public class FolderService {

    @Autowired
    private AuthService authService;

    @Autowired
    private InfoRepository infoRepository;

    @Autowired
    private ProjectService projectService;

    public PathNode getPackageFolderStructure(String storageName) {
        return readFolderStructure(authService.getCurrentUser(), storageName);
    }

    public PathNode getPackageFolderStructureOfUser(String userName, String storageName) {
        List<Info> infoList = infoRepository.findByUser(authService.getUserByName(userName));
        if(infoList.stream().noneMatch(info -> info.getInfoName().equals(storageName)
                && info.getAccess().equals(Info.Access.PUBLIC))) {
            throw new RuntimeException("User " + userName + " does not have public package " + storageName);
        }
        return readFolderStructure(authService.getUserByName(userName), storageName);
    }

    public PathNode getFullFolderStructure() {
        return readFolderStructure(authService.getCurrentUser());
    }

    public String createFolder(CreateFolderRequest createFolderRequest) throws IOException {
        String newFolderName = createFolderRequest.getNewFolderName();
        String packageName = createFolderRequest.getPackageName();
        String parentFolderRelativePath = createFolderRequest.getParentFolderRelativePath() == null
                ? "" : createFolderRequest.getParentFolderRelativePath(); // todo this is probably redundant now

        String userName = authService.getCurrentUser().getUsername();
        Path newFolderPath = getDefault().getPath(STORAGE, userName, packageName, parentFolderRelativePath, newFolderName);
        Path createdFolderPath = Files.createDirectory(newFolderPath);
        Path basePath = getDefault().getPath(STORAGE, userName, packageName);
        Path subPath = basePath.relativize(createdFolderPath);
        return subPath.toString();
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
    public void uploadFile(MultipartFile file, String packageName, String folderRelativePath) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path rootPathStorage = getDefault().getPath(STORAGE, userName, packageName, folderRelativePath, file.getOriginalFilename());
        file.transferTo(rootPathStorage);
    }

    // TODO download file
    public Resource downloadFile(String packageName, String fileNameWithPath) {
        String userName = authService.getCurrentUser().getUsername();
        try {
            Path filePath = getDefault().getPath(STORAGE, userName, packageName, fileNameWithPath);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileNameWithPath);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileNameWithPath, ex);
        }
    }

    public Resource downloadFileOfUser(String userName, String packageName, String fileNameWithPath) {
        List<Info> infoList = infoRepository.findByUserUsername(userName);

        if(!infoList.stream().anyMatch(info -> info.getInfoName().equals(packageName)
                && info.getAccess().equals(Info.Access.PUBLIC))) {
            throw new RuntimeException("User " + userName + " does not have public package " + packageName);
        }

        try {
            Path filePath = getDefault().getPath(STORAGE, userName, packageName, fileNameWithPath);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileNameWithPath);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileNameWithPath, ex);
        }
    }

    public Resource downloadFileOfProject(String projectId, String userName, String infoName, String fileNameWithPath) {
        Info info = projectService.getInfoOfUserAndProject(Long.parseLong(projectId), userName, infoName); // ugly
        // This particular project contains this particular info of this particular user, and the logged in user
        // is allowed to access, otherwise an exception will be thrown
        try {
            Path filePath = getDefault().getPath(STORAGE, userName, infoName, fileNameWithPath);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileNameWithPath);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileNameWithPath, ex);
        }

    }

}
