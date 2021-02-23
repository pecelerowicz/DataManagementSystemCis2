package gov.ncbj.nomaten.datamanagementbackend.util;

import gov.ncbj.nomaten.datamanagementbackend.model.User;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
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

}
