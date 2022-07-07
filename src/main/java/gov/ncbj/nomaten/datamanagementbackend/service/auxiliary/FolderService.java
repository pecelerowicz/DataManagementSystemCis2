package gov.ncbj.nomaten.datamanagementbackend.service.auxiliary;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_folder.CreateFolderRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.repository.InfoRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.StorageRepository;
import gov.ncbj.nomaten.datamanagementbackend.service.main.AllProjectsService;
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
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.FileSystems.getDefault;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;
import static java.util.stream.Collectors.toList;

@Service
public class FolderService {

    @Autowired
    private InfoRepository infoRepository;

    @Autowired
    private AllProjectsService allProjectsService;

    @Autowired
    private StorageRepository storageRepository;

    public PathNode getPackageFolderStructure(String storageName, String userName) {
        return storageRepository.getFolderStructure(getDefault().getPath(STORAGE, userName, storageName));
    }

    public PathNode getPackageFolderStructureOfUser(String userName, String storageName) {
        List<Info> infoList = infoRepository.findByUserUsername(userName);
        if(infoList.stream().noneMatch(info -> info.getInfoName().equals(storageName)
                && info.getAccess().equals(Info.Access.PUBLIC))) {
            throw new RuntimeException("User " + userName + " does not have public package " + storageName);
        }
        return storageRepository.getFolderStructure(getDefault().getPath(STORAGE, userName, storageName));
    }

    public String createFolder(CreateFolderRequest createFolderRequest, String userName) throws IOException {
        String newFolderName = createFolderRequest.getNewFolderName();
        String packageName = createFolderRequest.getPackageName();
        String parentFolderRelativePath = createFolderRequest.getParentFolderRelativePath() == null
                ? "" : createFolderRequest.getParentFolderRelativePath(); // todo this is probably redundant now

        Path newFolderPath = getDefault().getPath(STORAGE, userName, packageName, parentFolderRelativePath, newFolderName);
        Path createdFolderPath = Files.createDirectory(newFolderPath);
        Path basePath = getDefault().getPath(STORAGE, userName, packageName);
        Path subPath = basePath.relativize(createdFolderPath);
        return subPath.toString();
    }

    public void deleteFolder(String packageName, String userName, String folderPathString) throws IOException {
        Path folderPath = getDefault().getPath(STORAGE, userName, packageName, folderPathString);
        Files.walk(folderPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    // TODO validate (and then append) relativePath. We only want to place files/folders in already existing folders
    public void uploadFile(MultipartFile file, String packageName, String userName, String folderRelativePath) throws IOException {
        Path rootPathStorage = getDefault().getPath(STORAGE, userName, packageName, folderRelativePath, file.getOriginalFilename());
        file.transferTo(rootPathStorage);
    }

    // TODO download file
    public Resource downloadFile(String packageName, String userName, String fileNameWithPath) {
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
        Info info = allProjectsService.getInfoOfUserAndProject(Long.parseLong(projectId), userName, infoName); // ugly
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

    public String createStorage(String storageName, String userName) throws IOException {
        Path newStoragePath = getDefault().getPath(STORAGE, userName, storageName);
        Path createdStoragePath = Files.createDirectory(newStoragePath);
        return createdStoragePath.getFileName().toString();
    }

    public List<String> getDirectSubfolders(Path path) {
        List<Path> paths = createSortedPathsLevelOne(path);
        paths.remove(0);
        return paths.stream()
                .map(p -> p.toFile().toString())
                .map(p -> p.substring(p.lastIndexOf("/") + 1))
                .map(p -> p.substring(p.lastIndexOf("\\") + 1))
                .collect(toList());
    }

    public List<String> createSubfolder(Path where, String name) throws IOException {
        Path subfolderPath = where.resolve(name);
        Files.createDirectory(subfolderPath);
        return getDirectSubfolders(where);
    }

    public List<Path> createSortedPathsLevelOne(Path rootPath) {
        Set<Path> paths = new TreeSet<>();

        try {
            Files.walkFileTree(rootPath, EnumSet.noneOf(FileVisitOption.class),1, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    paths.add(dir);
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    paths.add(file);
                    return super.visitFile(file, attrs);
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return super.visitFileFailed(file, exc);
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return super.postVisitDirectory(dir, exc);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Path> pathList = new LinkedList<>();
        paths.forEach(p -> {
            pathList.add(p);
        });

        return pathList;
    }

}
