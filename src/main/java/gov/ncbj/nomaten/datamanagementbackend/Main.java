package gov.ncbj.nomaten.datamanagementbackend;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

public class Main {

    public static void main(String[] args) {

        Set<Path> paths = createSortedPaths();
        List<Path> pathList = new LinkedList<>();
        paths.forEach(p -> {
            pathList.add(p);
        });

        Path rootPath = pathList.remove(0);
        PathNode root = new PathNode(rootPath);

        for(Path path: pathList) {
            root = addNode(root, path);
        }

        System.out.println(root);

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




    private static Set<Path> createSortedPaths() {
        Set<Path> paths = new TreeSet<>();
        Path rootPathStorage = FileSystems.getDefault().getPath(STORAGE);
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
        return paths;
    }
}

class PathNode implements Comparable<PathNode> {
    private Path path;
    private Set<PathNode> children = new TreeSet<>();

    public PathNode(Path path) {
        this.path = path;
        this.children = new TreeSet<>();
    }

    public PathNode(Path path, Set<PathNode> children) {
        this.path = path;
        this.children = children;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Set<PathNode> getChildren() {
        return children;
    }

    public void setChildren(Set<PathNode> children) {
        this.children = children;
    }

    @Override
    public int compareTo(PathNode pathNode) {
        return this.path.compareTo(pathNode.getPath());
    }
}
