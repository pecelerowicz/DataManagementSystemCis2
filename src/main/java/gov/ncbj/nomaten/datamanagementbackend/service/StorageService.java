package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static gov.ncbj.nomaten.datamanagementbackend.util.DataManipulation.STORAGE;
import static java.nio.file.FileSystems.getDefault;
import static java.nio.file.Files.walk;

@Service
public class StorageService {

    private AuthService authService;

    @Autowired
    public StorageService(AuthService authService) {
        this.authService = authService;
    }

    public PathNode getStorageList() {
        String userName = authService.getCurrentUser().getUsername();
        Path rootPath = getDefault().getPath(STORAGE, userName);
        List<Path> paths = createSortedPathsLevelOne();
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

    private List<Path> createSortedPathsLevelOne() {

        String userName = authService.getCurrentUser().getUsername();
        Path rootPathStorage = getDefault().getPath("storage", userName);

        Set<Path> paths = new TreeSet<>();

        try {
            Files.walkFileTree(rootPathStorage, EnumSet.noneOf(FileVisitOption.class),1, new SimpleFileVisitor<Path>() {
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
