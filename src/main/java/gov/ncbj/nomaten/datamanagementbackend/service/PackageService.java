package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.DeletePackageRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.Package;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.FileSystems.getDefault;
import static java.util.stream.Collectors.toList;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

@Service
public class PackageService {

    private final AuthService authService;
    private final StorageService storageService;
    private final StorageRepository storageRepository;

    @Autowired
    public PackageService(AuthService authService, StorageService storageService, StorageRepository storageRepository) {
        this.authService = authService;
        this.storageService = storageService;
        this.storageRepository = storageRepository;
    }

    public List<Package> getPackages() throws IOException {
        System.out.println("Case 6");
        User user = authService.getCurrentUser();
        List<Info> infoList = user.getInfoList();
        List<String> storageNames = storageRepository.getDirectSubfolders(getDefault().getPath(STORAGE, user.getUsername()));
        return combineStorageWithMetadata(infoList, storageNames);
    }

    public String createPackage(String packageName) throws IOException {
        System.out.println("case 4");
        User user = authService.getCurrentUser();
        List<String> metadataNames = user.getInfoList().stream().map(Info::getInfoName).collect(toList());
        List<String> storageNames = storageRepository.getDirectSubfolders(getDefault().getPath(STORAGE, user.getUsername()));
        if(metadataNames.contains(packageName) || storageNames.contains(packageName)) {
            throw new RuntimeException("Package " + packageName + " already exists");
        }
        return storageService.createStorage(packageName);
    }

    @Transactional
    public void deletePackage(DeletePackageRequest deletePackageRequest) throws IOException {
        String packageName = deletePackageRequest.getPackageName();
        User user = authService.getCurrentUser();
        String userName = user.getUsername();
        Path storagePath = getDefault().getPath(STORAGE, userName, packageName);

        Optional<Info> maybeTargetInfo = user
                .getInfoList()
                .stream()
                .filter(info -> info.getInfoName().equals(packageName))
                .findFirst();

        if(Files.exists(storagePath) || maybeTargetInfo.isPresent()) {
            if(Files.exists(storagePath)) {
                Files.walk(storagePath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }

            if(maybeTargetInfo.isPresent()) {
                Info targetInfo = maybeTargetInfo.get();
                user.getInfoList().remove(targetInfo);
                targetInfo.setUser(null);
            }
        } else {
            throw new RuntimeException("No package " + deletePackageRequest.getPackageName());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private List<Package> combineStorageWithMetadata(List<Info> infoList, List<String> storageNames) {
        List<Package> packageList = new LinkedList<>();
        for(Info info: infoList) {
            if(storageNames.contains(info.getInfoName())) {
                packageList.add(new Package(info.getInfoName(), true, true, info.getTitle(), info.getShortDescription(), info.getLocalDateTime()));
            } else {
                packageList.add(new Package(info.getInfoName(), false, true, info.getTitle(), info.getShortDescription(), info.getLocalDateTime()));
            }
        }
        Set<String> infoNameSet = infoList.stream().map(Info::getInfoName).collect(Collectors.toSet());
        for(String storageName: storageNames) {
            if(!infoNameSet.contains(storageName)) {
                packageList.add(new Package(storageName, true, false, null, null, null));
            }
        }
        Collections.sort(packageList);
        return packageList;
    }

    @Transactional
    public String createMetadata(String metadataName) throws IOException {
        User user = authService.getCurrentUser();

        List<Package> packageList = getPackages();
        List<Package> filtered = packageList
                .stream()
                .filter(sm -> sm.getName().equals(metadataName))
                .collect(toList());

        if(filtered.size() == 0) {
            throw new RuntimeException("No package " + metadataName);
        } else if(filtered.size() > 1) {
            throw new RuntimeException("Corrupted data");
        } else {
            Package aPackage = filtered.get(0);
            if(aPackage.isHasMetadata()) {
                throw new RuntimeException("Metadata " + metadataName + " already created!");
            }
        }

        Info info = new Info();
        info.setUser(user);
        info.setInfoName(metadataName);
        info.setAccess(Info.Access.PRIVATE);
        user.getInfoList().add(info);
        return metadataName;
    }

}
