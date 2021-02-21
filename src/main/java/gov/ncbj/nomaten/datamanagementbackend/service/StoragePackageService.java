package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.StoragePackage;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.FileSystems.getDefault;

@Service
public class StoragePackageService {

    AuthService authService;

    StorageService storageService;

    @Autowired
    public StoragePackageService(AuthService authService, StorageService storageService) {
        this.authService = authService;
        this.storageService = storageService;
    }


    public List<StoragePackage> getStoragesAndPackages() throws IOException {
        User user = authService.getCurrentUser();
        PathNode storages = storageService.getPackages(); // pozmieniać nazwy trzeba bedzie!

        List<String> packageNames = user.getDataSets()
                .stream()
                .map(p -> p.getName())
                .collect(Collectors.toList());
        Path rootPathStorage = getDefault().getPath("storage", user.getUsername());

        List<String> storageNames = listDirectoriesUsingDirectoryStream(rootPathStorage);

        List<StoragePackage> storagesPackages = combineStoragePackage(packageNames, storageNames);

        return storagesPackages;
    }

    private List<StoragePackage> combineStoragePackage(List<String> packageNames, List<String> storageNames) {
        Set<String> packages = new HashSet<>(packageNames);
        Set<String> storages = new HashSet<>(storageNames);
        List<StoragePackage> storagesPackages = new LinkedList<>();
        for(String pack: packages) {
            if(storages.contains(pack)) {
                storagesPackages.add(new StoragePackage(pack, true, true));
            } else {
                storagesPackages.add(new StoragePackage(pack, false, true));
            }
        }
        for(String storage: storages) {
            if(!packages.contains(storage)) {
                storagesPackages.add(new StoragePackage(storage, true, false));
            }
        }
        Collections.sort(storagesPackages);
        return storagesPackages;
    }

    private List<String> listDirectoriesUsingDirectoryStream(Path dirPath) throws IOException {
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
}
