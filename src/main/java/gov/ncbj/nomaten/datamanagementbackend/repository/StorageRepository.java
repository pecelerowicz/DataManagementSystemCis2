package gov.ncbj.nomaten.datamanagementbackend.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static gov.ncbj.nomaten.datamanagementbackend.util.DataManipulation.STORAGE;
import static gov.ncbj.nomaten.datamanagementbackend.util.DataManipulation.createSortedPathsLevelOne;
import static java.nio.file.FileSystems.getDefault;
import static java.util.stream.Collectors.toList;

/**
 * This is the new repository intended to perform all low-level operations on folder structure. For now it is only used
 * by the AdminService class, but later all low-level folder operations are meant to be single-threaded through this
 * class
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

}
