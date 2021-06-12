package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.DeleteInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.DeletePackageRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.Package;
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
import static java.nio.file.Files.walk;
import static java.util.stream.Collectors.toList;

@Service
public class PackageService {

    AuthService authService;

    @Autowired
    public PackageService(AuthService authService) {
        this.authService = authService;
    }

    public List<Info> getInfoList() {
        return authService.getCurrentUser().getInfoList();
    }

    @Transactional
    public void deleteInfo(DeleteInfoRequest deleteInfoRequest) {
        String infoName = deleteInfoRequest.getInfoName();
        User user = authService.getCurrentUser();

        Optional<Info> maybeTargetInfo = user
                .getInfoList()
                .stream()
                .filter(info -> info.getInfoName().equals(infoName))
                .findFirst();
        if(maybeTargetInfo.isPresent()) {
            Info targetInfo = maybeTargetInfo.get();
            user.getInfoList().remove(targetInfo);
            targetInfo.setUser(null);
        } else {
            throw new RuntimeException("No info " + infoName);
        }
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
        Path oldPackagePath = getDefault().getPath(STORAGE, userName, oldName);
        Path newPackagePath = getDefault().getPath(STORAGE, userName, newName);
        Path createdPackagePath = Files.move(oldPackagePath, newPackagePath);
        return createdPackagePath.toFile().toString();
    }

    public boolean deleteStorage(String storageName) throws IOException {
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

    public List<Package> getPackages() throws IOException {
        User user = authService.getCurrentUser();
        List<String> metadataNames = metadataNamesOfUser(user);
        List<String> storageNames = storageNamesOfUser(user);
        return combineStorageWithMetadata(metadataNames, storageNames);
    }

    @Transactional
    public void deletePackage(DeletePackageRequest deletePackageRequest) throws IOException {
        String packageName = deletePackageRequest.getPackageName();
        User user = authService.getCurrentUser();
        String userName = user.getUsername();
        Path storagePath = getDefault().getPath(STORAGE, userName, packageName);

        Optional<Info> maybeTargetInfo = user
                .getInfoList()
                .stream()
                .filter(info -> info.getInfoName().equals(packageName))
                .findFirst();

        if(Files.exists(storagePath) || maybeTargetInfo.isPresent()) {
            if(Files.exists(storagePath)) {
                Files.walk(storagePath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }

            if(maybeTargetInfo.isPresent()) {
                Info targetInfo = maybeTargetInfo.get();
                user.getInfoList().remove(targetInfo);
                targetInfo.setUser(null);
            }
        } else {
            throw new RuntimeException("No package " + deletePackageRequest.getPackageName());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    private List<Package> combineStorageWithMetadata(List<String> metadataNames, List<String> storageNames) {
        Set<String> metadata = new HashSet<>(metadataNames);
        Set<String> storage = new HashSet<>(storageNames);
        List<Package> packageList = new LinkedList<>();
        for(String matadataName: metadata) {
            if(storage.contains(matadataName)) {
                packageList.add(new Package(matadataName, true, true));
            } else {
                packageList.add(new Package(matadataName, false, true));
            }
        }
        for(String storageName: storage) {
            if(!metadata.contains(storageName)) {
                packageList.add(new Package(storageName, true, false));
            }
        }
        Collections.sort(packageList);
        return packageList;
    }

    @Transactional
    public String createMetadata(String metadataName) throws IOException {
        User user = authService.getCurrentUser();

        List<Package> packageList = getPackages();
        List<Package> filtered = packageList
                .stream()
                .filter(sm -> sm.getName().equals(metadataName))
                .collect(toList());

        if(filtered.size() == 0) {
            throw new RuntimeException("No package " + metadataName);
        } else if(filtered.size() > 1) {
            throw new RuntimeException("Corrupted data");
        } else {
            Package aPackage = filtered.get(0);
            if(aPackage.isHasMetadata()) {
                throw new RuntimeException("Metadata " + metadataName + " already created!");
            }
        }

        Info info = new Info();
        info.setUser(user);
        info.setInfoName(metadataName);
        info.setAccess(Info.Access.PRIVATE);
        user.getInfoList().add(info);
        return metadataName;
    }

}
