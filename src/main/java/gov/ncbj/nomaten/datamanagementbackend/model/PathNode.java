package gov.ncbj.nomaten.datamanagementbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

public class PathNode implements Comparable<PathNode> {
    @JsonIgnore
    private Path path;
    private String relative;
    private boolean isFolder;
    private Set<PathNode> children = new TreeSet<>();

    public PathNode(Path path) {
        this.path = path;
        this.relative = path.toString();
        this.isFolder = Files.isDirectory(this.path);
        this.children = new TreeSet<>();
    }

    public PathNode(Path path, Set<PathNode> children) {
        this.path = path;
        this.relative = path.toString();
        this.isFolder = Files.isDirectory(this.path);
        this.children = children;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getRelative() {
        return relative;
    }

    public void setRelative(String relative) {
        this.relative = relative;
    }

    public Set<PathNode> getChildren() {
        return children;
    }

    public void setChildren(Set<PathNode> children) {
        this.children = children;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    @Override
    public int compareTo(PathNode pathNode) {
        return this.path.compareTo(pathNode.getPath());
    }
}