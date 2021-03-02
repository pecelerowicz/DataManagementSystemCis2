package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.StorageAndMetadata;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static gov.ncbj.nomaten.datamanagementbackend.util.DataManipulation.*;
import static java.nio.file.FileSystems.getDefault;

@Service
public class StorageAndMetadataService {

    AuthService authService;

    @Autowired
    public StorageAndMetadataService(AuthService authService) {
        this.authService = authService;
    }

    public PathNode getStorage() {
        List<Path> paths = createSortedPathsLevelOne();
        PathNode root = new PathNode(paths.remove(0));
        for(Path path: paths) {
            root = addNode(root, path);
        }
        return root;
    }

    public String createStorage(String newPackageName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path newPackagePath = getDefault().getPath(STORAGE, userName, newPackageName);
        Path createdPackagePath = Files.createDirectory(newPackagePath);
        return createdPackagePath.getFileName().toString();
    }

    public String updateStorage(String oldName, String newName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path oldPackagePath = getDefault().getPath(STORAGE, userName, oldName);
        Path newPackagePath = getDefault().getPath(STORAGE, userName, newName);
        Path createdPackagePath = Files.move(oldPackagePath, newPackagePath);
        return createdPackagePath.toFile().toString();
    }

    public void deleteStorage(String packageName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path packagePath = getDefault().getPath(STORAGE, userName, packageName);
        Files.walk(packagePath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }



    //TODO move the logic below to the special util package (or sth like that)
    private PathNode addNode(PathNode where, Path what) {
        if(what.getParent().equals(where.getPath())) {
            where.getChildren().add(new PathNode(what));
        } else {
            for(PathNode child: where.getChildren()) {
                addNode(child, what);
            }
        }
        return where;
    }



    private List<Path> createSortedPathsLevelOne() {

        String userName = authService.getCurrentUser().getUsername();
        Path rootPathStorage = getDefault().getPath("storage", userName);

        Set<Path> paths = new TreeSet<>();

        try {
            Files.walkFileTree(rootPathStorage, EnumSet.noneOf(FileVisitOption.class),1, new SimpleFileVisitor<>() {
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


    ///////////////////////////////////////

    public List<StorageAndMetadata> getStorageAndMetadataList() throws IOException {
        User user = authService.getCurrentUser();
        List<String> metadataNames = metadataNamesOfUser(user);
        List<String> storageNames = storageNamesOfUser(user);
        return combineStorageWithMetadata(metadataNames, storageNames);
    }

    private List<StorageAndMetadata> combineStorageWithMetadata(List<String> metadataNames, List<String> storageNames) {
        Set<String> metadata = new HashSet<>(metadataNames);
        Set<String> storage = new HashSet<>(storageNames);
        List<StorageAndMetadata> storageAndMetadataList = new LinkedList<>();
        for(String matadataName: metadata) {
            if(storage.contains(matadataName)) {
                storageAndMetadataList.add(new StorageAndMetadata(matadataName, true, true));
            } else {
                storageAndMetadataList.add(new StorageAndMetadata(matadataName, false, true));
            }
        }
        for(String storageName: storage) {
            if(!metadata.contains(storage)) {
                storageAndMetadataList.add(new StorageAndMetadata(storageName, true, false));
            }
        }
        Collections.sort(storageAndMetadataList);
        return storageAndMetadataList;
    }


}
