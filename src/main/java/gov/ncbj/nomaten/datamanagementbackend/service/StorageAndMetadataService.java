package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.model.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.StorageAndMetadata;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static gov.ncbj.nomaten.datamanagementbackend.util.DataManipulation.*;
import static java.nio.file.FileSystems.getDefault;
import static java.util.stream.Collectors.toList;

@Service
public class StorageAndMetadataService {

    AuthService authService;

    @Autowired
    public StorageAndMetadataService(AuthService authService) {
        this.authService = authService;
    }

    public PathNode getPackage() {
        String userName = authService.getCurrentUser().getUsername();
        Path rootPath = getDefault().getPath(STORAGE, userName);
        List<Path> paths = createSortedPathsLevelOne();
        PathNode root = new PathNode(paths.remove(0), rootPath);
        for(Path path: paths) {
            root = addNode(root, path, rootPath);
        }
        return root;
    }

    public String createPackage(String newPackageName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path newPackagePath = getDefault().getPath(STORAGE, userName, newPackageName);
        Path createdPackagePath = Files.createDirectory(newPackagePath);
        return createdPackagePath.getFileName().toString();
    }

    public String updatePackage(String oldName, String newName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path oldPackagePath = getDefault().getPath(STORAGE, userName, oldName);
        Path newPackagePath = getDefault().getPath(STORAGE, userName, newName);
        Path createdPackagePath = Files.move(oldPackagePath, newPackagePath);
        return createdPackagePath.toFile().toString();
    }

    @Transactional
    public void deletePackage(String packageName) throws IOException {
        User user = authService.getCurrentUser();
        String userName = user.getUsername();
        Path packagePath = getDefault().getPath(STORAGE, userName, packageName);
        if(Files.exists(packagePath)) {
            Files.walk(packagePath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        Optional<Info> maybeTargetInfo = user
                .getInfoList()
                .stream()
                .filter(info -> info.getName().equals(packageName))
                .findFirst();
        if(maybeTargetInfo.isPresent()) {
            Info targetInfo = maybeTargetInfo.get();
            user.getInfoList().remove(targetInfo);
            targetInfo.setUser(null);
        }
    }



    //TODO move the logic below to the special util package (or sth like that)
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
            if(!metadata.contains(storageName)) {
                storageAndMetadataList.add(new StorageAndMetadata(storageName, true, false));
            }
        }
        Collections.sort(storageAndMetadataList);
        return storageAndMetadataList;
    }

    ////////////////////////////////////////////////

    public String createStorage(String storageName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path newStoragePath = getDefault().getPath(STORAGE, userName, storageName);
        Path createdStoragePath = Files.createDirectory(newStoragePath);
        return createdStoragePath.getFileName().toString();
    }

    @Transactional
    public String createMetadata(String metadataName) throws IOException {
        User user = authService.getCurrentUser();

        List<StorageAndMetadata> storageAndMetadataList = getStorageAndMetadataList();
        List<StorageAndMetadata> filtered = storageAndMetadataList
                .stream()
                .filter(sm -> sm.getName().equals(metadataName))
                .collect(toList());

        if(filtered.size() == 0) {
            throw new RuntimeException("No package " + metadataName);
        } else if(filtered.size() > 1) {
            throw new RuntimeException("Corrupted data");
        } else {
            StorageAndMetadata storageAndMetadata = filtered.get(0);
            if(storageAndMetadata.isHasMetadata()) {
                throw new RuntimeException("Metadata " + metadataName + " already created!");
            }
        }

        Info info = new Info();
        info.setUser(user);
        info.setName(metadataName);
        info.setAccess(Info.Access.PRIVATE);
        user.getInfoList().add(info);
        return metadataName;
    }

}
