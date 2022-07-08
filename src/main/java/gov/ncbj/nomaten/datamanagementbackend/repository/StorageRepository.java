package gov.ncbj.nomaten.datamanagementbackend.repository;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * This is the new repository intended to perform all low-level operations on the folder structure. All low-level folder
 * operations are meant to be single-threaded through this class.
 */
@Repository
@AllArgsConstructor
public class StorageRepository {

    public boolean folderExists(Path path) {
        return path.toFile().exists();
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

    public List<String> createSubfolder(Path where, String name) throws IOException {
        Path subfolderPath = where.resolve(name);
        Files.createDirectory(subfolderPath);
        return getDirectSubfolders(where);
    }

    public List<String> deleteSubfolder(Path where, String name) throws IOException {
        Path subfolderPath = where.resolve(name);
        Files.delete(subfolderPath);
        return getDirectSubfolders(where);
    }

//    public PathNode getFolderStructure(Path rootPath) {
//        if(Files.notExists(rootPath)) {
//            throw new RuntimeException("Folder does not exist");
//        }
//        List<Path> paths = createSortedPaths(rootPath);
//        PathNode root = new PathNode(paths.remove(0), rootPath);
//        for(Path path: paths) {
//            root = addNode(root, path, rootPath);
//        }
//        return root;
//    }

//    private List<Path> createSortedPaths(Path rootPathStorage) {
//        Set<Path> paths = new TreeSet<>();
//        try {
//            Files.walkFileTree(rootPathStorage, new SimpleFileVisitor<Path>() {
//                @Override
//                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                    paths.add(dir);
//                    return super.preVisitDirectory(dir, attrs);
//                }
//
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                    paths.add(file);
//                    return super.visitFile(file, attrs);
//                }
//
//                @Override
//                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
//                    return super.visitFileFailed(file, exc);
//                }
//
//                @Override
//                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//                    return super.postVisitDirectory(dir, exc);
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        List<Path> pathList = new LinkedList<>();
//        paths.forEach(p -> {
//            pathList.add(p);
//        });
//
//        return pathList;
//    }

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
