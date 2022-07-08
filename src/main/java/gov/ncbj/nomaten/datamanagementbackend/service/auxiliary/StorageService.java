package gov.ncbj.nomaten.datamanagementbackend.service.auxiliary;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.repository.StorageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static java.nio.file.FileSystems.getDefault;
import static java.nio.file.Files.walk;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

@Service
@AllArgsConstructor
public class StorageService { // FolderService will be probably merged with this except for download upload

    private final StorageRepository storageRepository;

    private final FolderService folderService;

    private final AuthService authService;

    public PathNode getInfoListOfUser() {
        String userName = authService.getCurrentUser().getUsername();
        Path rootPath = getDefault().getPath(STORAGE, userName);
        List<Path> paths = createSortedPathsOfUser();
        PathNode root = new PathNode(paths.remove(0), rootPath);
        for(Path path: paths) {
            root = addNode(root, path, rootPath);
        }
        return root;
    }

    public String createStorage(String storageName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path newStoragePath = getDefault().getPath(STORAGE, userName, storageName);
        Path createdStoragePath = Files.createDirectory(newStoragePath);
        return createdStoragePath.getFileName().toString();
    }

    public String updateStorage(String oldName, String newName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path oldStoragePath = getDefault().getPath(STORAGE, userName, oldName);
        Path newStoragePath = getDefault().getPath(STORAGE, userName, newName);
        Path updatedStoragePath = Files.move(oldStoragePath, newStoragePath);
        return updatedStoragePath.getFileName().toString();
    }

    public boolean deleteStorage(String storageName) {
        User user = authService.getCurrentUser();
        String userName = user.getUsername();
        Path storagePath = getDefault().getPath(STORAGE, userName, storageName);

        try {
            walk(storagePath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("No storage " + storageName);
        }
    }

    public PathNode getFolderStructure(Path rootPath) {
        return folderService.getFolderStructure(rootPath);
    }

    private PathNode addNode(PathNode where, Path what, Path rootPath) {
        if(what.getParent().equals(where.getPath())) {
            where.getChildren().add(new PathNode(what, rootPath));
        } else {
            for(PathNode child: where.getChildren()) {
                addNode(child, what, rootPath);
            }
        }
        return where;
    }

    private List<Path> createSortedPathsOfStorage() {
        Path rootPathStorage = getDefault().getPath(STORAGE);
        return storageRepository.createSortedPathsLevelOne(rootPathStorage);
    }

    private List<Path> createSortedPathsOfUser() {
        String userName = authService.getCurrentUser().getUsername();
        Path rootPathStorage = getDefault().getPath(STORAGE, userName);
        return storageRepository.createSortedPathsLevelOne(rootPathStorage);
    }

}
