package gov.ncbj.nomaten.datamanagementbackend.util;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.User;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.FileSystems.getDefault;

public class DataManipulation {

    public static String STORAGE = "storage";

    public static List<String> listDirectSubdirectories(Path dirPath) throws IOException {
        List<String> fileList = new LinkedList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    fileList.add(path.getFileName()
                            .toString());
                }
            }
        }
        return fileList;
    }

    public static List<String> metadataNamesOfUser(User user) {
        return user.getInfoList()
                .stream()
                .map(p -> p.getInfoName())
                .collect(Collectors.toList());
    }

    public static List<String> storageNamesOfUser(User user) throws IOException {
        Path rootPathStorage = getDefault().getPath(STORAGE, user.getUsername());
        return listDirectSubdirectories(rootPathStorage);
    }

    public static PathNode readFolderStructure(User user) {
        String userName = user.getUsername();
        Path rootPath = getDefault().getPath(STORAGE, userName);
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

    public static PathNode readFolderStructure(User user, String storageName) {
        String userName = user.getUsername();
        Path rootPath = getDefault().getPath(STORAGE, userName, storageName);
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

    private static List<Path> createSortedPaths(Path rootPathStorage) {
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

    public static List<Path> createSortedPathsLevelOne(Path rootPath) {
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
