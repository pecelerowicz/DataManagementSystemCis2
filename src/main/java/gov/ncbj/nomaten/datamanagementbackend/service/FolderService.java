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
public class FolderService {

    private static String STORAGE = "storage";

    @Autowired
    private AuthService authService;

    public PathNode getFolderStructure() {
        List<Path> paths = createSortedPaths();
        PathNode root = new PathNode(paths.remove(0));
        for(Path path: paths) {
            root = addNode(root, path);
        }
        return root;
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

}



