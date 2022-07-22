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

    public List<Package> getPackageList() throws IOException {
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
        Path createdPackagePath = folderService.createFolder(getDefault().getPath(STORAGE, user.getUsername(), packageName));
        return getDefault().getPath(STORAGE, user.getUsername()).relativize(createdPackagePath).toString();
    }

    @Transactional
    // TODO może uniemoliwić usuwanie, gdy pakiet należy do projektu?
    public void deletePackage(DeletePackageRequest deletePackageRequest) throws IOException {
        User user = authService.getCurrentUser();
        String userName = user.getUsername();
        String packageName = deletePackageRequest.getPackageName();
        Path storagePath = getDefault().getPath(STORAGE, userName, packageName);

        Optional<Info> maybeInfo = user
                .getInfoList()
                .stream()
                .filter(info -> info.getInfoName().equals(packageName))
                .findFirst();

        boolean storageExists = folderService.itemExists(storagePath);
        boolean infoExists = maybeInfo.isPresent();
        if(storageExists || infoExists) {
            if(infoExists && maybeInfo.get().getProjects().size() > 0) {
                throw new RuntimeException("Remove package from the projects");
            }
            if(storageExists) {
                folderService.deleteItem(storagePath);
            }
            if(infoExists) {
                Info info = maybeInfo.get();
                user.getInfoList().remove(info);
                info.setUser(null);
            }
        } else {
            throw new RuntimeException("No package " + deletePackageRequest.getPackageName());
        }
    }

    public Info getInfo(String infoName) {
        User user = authService.getCurrentUser();
        return infoService.getInfo(infoName, user);
    }

    public Info createInfo(CreateInfoRequest createInfoRequest) {
        User user = authService.getCurrentUser();
        return infoService.createInfo(createInfoRequest, user);
    }

    public Info updateInfo(UpdateInfoRequest updateInfoRequest) {
        User user = authService.getCurrentUser();
        return infoService.updateInfo(updateInfoRequest, user);
    }

    public String createStorage(String storageName) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        Path newStoragePath = getDefault().getPath(STORAGE, userName, storageName);
        return folderService.createFolder(newStoragePath).getFileName().toString();
    }

    public PathNode getPackageFolderStructure(String storageName) {
        String userName = authService.getCurrentUser().getUsername();
        return folderService.getFolderStructure(getDefault().getPath(STORAGE, userName, storageName));
    }

    public String createFolder(CreateFolderRequest createFolderRequest) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        String packageName = createFolderRequest.getPackageName();
        String parentFolderRelativePath = createFolderRequest.getParentFolderRelativePath();
        String newFolderName = createFolderRequest.getNewFolderName();
        Path newFolderPath = getDefault().getPath(STORAGE, userName, packageName,
                parentFolderRelativePath, newFolderName);
        Path createdFolderPath = folderService.createFolder(newFolderPath);
        Path basePath = getDefault().getPath(STORAGE, userName, packageName);
        Path subPath = basePath.relativize(createdFolderPath);
        return subPath.toString();
    }

    public void deleteItem(String packageName, String folderPathString) throws IOException {
        Path folderPath = getDefault().getPath(STORAGE,
                authService.getCurrentUser().getUsername(), packageName, folderPathString);
        folderService.deleteItem(folderPath);
    }

    public void uploadFile(MultipartFile file, String packageName, String folderRelativePath) throws IOException {
        String userName = authService.getCurrentUser().getUsername();
        folderService.uploadFile(file, packageName, userName, folderRelativePath);
    }

    public Resource downloadFile(String packageName, String fileNameWithPath) {
        String userName = authService.getCurrentUser().getUsername();
        return folderService.downloadFile(packageName, userName, fileNameWithPath);
    }

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
