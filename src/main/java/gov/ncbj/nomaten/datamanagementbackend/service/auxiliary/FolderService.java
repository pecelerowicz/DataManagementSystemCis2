package gov.ncbj.nomaten.datamanagementbackend.service.auxiliary;

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

    public PathNode getFolderStructure(Path rootPath) {
        if(Files.notExists(rootPath)) {
            throw new RuntimeException("Folder does not exist");
        }
        List<Path> paths = createSortedPaths(rootPath);
        PathNode root = new PathNode(paths.remove(0), rootPath);
        for(Path path: paths) {
            root = addNode(root, path, rootPath);
        }
        return root;
    }

    public Path createFolder(Path newFolderPath) throws IOException {
        return Files.createDirectory(newFolderPath);
    }

    public boolean itemExists(Path itemPath) {
        return Files.exists(itemPath);
    }

    public void deleteItem(Path itemPath) throws IOException {
        Files.walk(itemPath)
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

    public List<String> getDirectSubfolders(Path path) {
        List<Path> paths = createSortedPathsLevelOne(path);
        paths.remove(0);
        return paths.stream()
                .map(p -> p.toFile().toString())
                .map(p -> p.substring(p.lastIndexOf("/") + 1))
                .map(p -> p.substring(p.lastIndexOf("\\") + 1))
                .collect(toList());
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

    public boolean fileOrFolderExists(Path path) {
        return Files.exists(path);
    }

    public boolean isFile(Path path) {
        return Files.isRegularFile(path);
    }

    public boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    private List<Path> createSortedPaths(Path rootPathStorage) {
        Set<Path> paths = new TreeSet<>();
        try {
            Files.walkFileTree(rootPathStorage, new SimpleFileVisitor<Path>() {
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

    private static PathNode addNode(PathNode where, Path what, Path rootPath) {
        if(what.getParent().equals(where.getPath())) {
            where.getChildren().add(new PathNode(what, rootPath));
        } else {
            for(PathNode child: where.getChildren()) {
                addNode(child, what, rootPath);
            }
        }
        return where;
    }

}
