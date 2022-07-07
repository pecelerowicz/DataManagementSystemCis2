package gov.ncbj.nomaten.datamanagementbackend.service.main;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_folder.CreateFolderRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.CreateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.UpdateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.DeletePackageRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.Package;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.FileSystems.getDefault;
import static java.util.stream.Collectors.toList;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

@Service
public class MyDataService {

    private final AuthService authService;
    private final InfoService infoService;
    private final FolderService folderService;

    @Autowired
    public MyDataService(AuthService authService,
                         InfoService infoService, FolderService folderService) {
        this.authService = authService;
        this.infoService = infoService;
        this.folderService = folderService;
    }

    public Info getInfo(String infoName) {
        return infoService.getInfo(infoName);
    }

    public Info createInfo(CreateInfoRequest createInfoRequest) {
        return infoService.createInfo(createInfoRequest);
    }

    public Info updateInfo(UpdateInfoRequest updateInfoRequest) {
        return infoService.updateInfo(updateInfoRequest);
    }

    public List<Package> getPackages() throws IOException {
        User user = authService.getCurrentUser();
        List<Info> infoList = infoService.findByUser(user);
        List<String> storageNames = folderService.getDirectSubfolders(getDefault().getPath(STORAGE, user.getUsername()));
        return combineStorageWithMetadata(infoList, storageNames);
    }

    public String createPackage(String packageName) throws IOException {
        User user = authService.getCurrentUser();
        List<String> metadataNames = user.getInfoList().stream().map(Info::getInfoName).collect(toList());
        List<String> storageNames = folderService.getDirectSubfolders(getDefault().getPath(STORAGE, user.getUsername()));
        if(metadataNames.contains(packageName) || storageNames.contains(packageName)) {
            throw new RuntimeException("Package " + packageName + " already exists");
        }
        folderService.createSubfolder(getDefault().getPath(STORAGE, user.getUsername()), packageName);
        return packageName; // do poprawy
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

    public PathNode getPackageFolderStructure(String storageName) {
        return folderService.getPackageFolderStructure(storageName);
    }

    public String createFolder(CreateFolderRequest createFolderRequest) throws IOException {
        return folderService.createFolder(createFolderRequest);
    }

    public void deleteFolder(String packageName, String folderPathString) throws IOException {
        folderService.deleteFolder(packageName, folderPathString);
    }

    public void uploadFile(MultipartFile file, String packageName, String folderRelativePath) throws IOException {
        folderService.uploadFile(file, packageName, folderRelativePath);
    }

    public Resource downloadFile(String packageName, String fileNameWithPath) {
        return folderService.downloadFile(packageName, fileNameWithPath);
    }

    public String createStorage(String storageName) throws IOException {
        return folderService.createStorage(storageName);
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

}
