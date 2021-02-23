package gov.ncbj.nomaten.datamanagementbackend.util;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.User;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
        return user.getDataSets()
                .stream()
                .map(p -> p.getName())
                .collect(Collectors.toList());
    }

    public static List<String> storageNamesOfUser(User user) throws IOException {
        Path rootPathStorage = getDefault().getPath(STORAGE, user.getUsername());
        return listDirectSubdirectories(rootPathStorage);
    }

    public static PathNode readFolderStructure(User user) {
        List<Path> paths = createSortedPaths(user);
        PathNode root = new PathNode(paths.remove(0));
        for(Path path: paths) {
            root = addNode(root, path);
        }
        return root;
    }

    private static List<Path> createSortedPaths(User user) {

        String userName = user.getUsername();
        Path rootPathStorage = getDefault().getPath(STORAGE, userName);

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

    private static PathNode addNode(PathNode where, Path what) {
        if(what.getParent().equals(where.getPath())) {
            where.getChildren().add(new PathNode(what));
        } else {
            for(PathNode child: where.getChildren()) {
                addNode(child, what);
            }
        }
        return where;
    }

}
