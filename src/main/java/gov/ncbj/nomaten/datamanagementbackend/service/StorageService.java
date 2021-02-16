package gov.ncbj.nomaten.datamanagementbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.FileSystems.getDefault;

@Service
public class StorageService {

    private static String STORAGE = "storage";

    @Autowired
    private AuthService authService;

    public PathNode getStorage() {
        List<Path> paths = createSortedPaths();
        PathNode root = new PathNode(paths.remove(0));
        for(Path path: paths) {
            root = addNode(root, path);
        }
        return root;
    }

    public PathNode getPackages() {
        List<Path> paths = createSortedPathsLevelOne();
        PathNode root = new PathNode(paths.remove(0));
        for(Path path: paths) {
            root = addNode(root, path);
        }
        return root;
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

    private List<Path> createSortedPaths() {

        String userName = authService.getCurrentUser().getUsername();
        Path rootPathStorage = getDefault().getPath("storage", userName);

        Set<Path> paths = new TreeSet<>();

        try {
            Files.walkFileTree(rootPathStorage, new SimpleFileVisitor<>() {
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

    // TODO validate (and then append) relativePath. We only want to place files/folders in already existing folders
    public void upload(MultipartFile file, String relativePath) {
        Path rootPathStorage = getDefault().getPath(
                relativePath,
                file.getOriginalFilename());
        try {

            System.out.println("przed");
            file.transferTo(rootPathStorage);
            System.out.println("po");

//            int l = file.getBytes().length;
//
//            byte[] data = file.getBytes();
//            Files.write(rootPathStorage, data);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String create(String newPackageName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path newPackagePath = getDefault().getPath(STORAGE, userName, newPackageName);
        Path createdPackagePath = Files.createDirectory(newPackagePath);
        return createdPackagePath.getFileName().toString();
    }

    public String update(String oldName, String newName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path oldPackagePath = getDefault().getPath(STORAGE, userName, oldName);
        Path newPackagePath = getDefault().getPath(STORAGE, userName, newName);
        Path createdPackagePath = Files.move(oldPackagePath, newPackagePath);
        return createdPackagePath.toFile().toString();
    }

    public void delete(String packageName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path packagePath = getDefault().getPath(STORAGE, userName, packageName);
        Files.delete(packagePath);
    }

    public String createFolder(String newFolderName, String parentFolderFullPath) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path newFolderPath = getDefault().getPath(STORAGE, userName, parentFolderFullPath, newFolderName);
        Path createdFolderPath = Files.createDirectory(newFolderPath);
        return createdFolderPath.toString();
    }

    public void deleteFolder(String packageName, String folderPath) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path folderPathString = getDefault().getPath(STORAGE, userName, packageName, folderPath);
        Files.walk(folderPathString)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

}



