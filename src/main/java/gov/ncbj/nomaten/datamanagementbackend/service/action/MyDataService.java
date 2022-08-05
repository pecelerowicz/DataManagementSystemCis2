package gov.ncbj.nomaten.datamanagementbackend.service.action;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.CreateFolderRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.CreateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.UpdateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.DeletePackageRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.Package;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.service.support.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.CheckService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.InfoService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.FileSystems.getDefault;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

@Service
@AllArgsConstructor
public class MyDataService {

    private final AuthService authService;
    private final InfoService infoService;
    private final FolderService folderService;
    private final CheckService checkService;

    public List<Package> getPackageList() {
        User currentUser = authService.getCurrentUser();
        List<Info> infoList = infoService.getInfoListByUser(currentUser);
        List<String> storageNames = folderService.getDirectSubfolders(getDefault().getPath(STORAGE, currentUser.getUsername()));
        return combineStorageWithMetadata(infoList, storageNames);
    }

    public String createPackage(String packageName) throws IOException {
        User currentUser = authService.getCurrentUser();
        checkService.packageDoesNotExist(currentUser, packageName);
        Path createdPackagePath = folderService.createFolder(getDefault().getPath(STORAGE, currentUser.getUsername(), packageName));
        return getDefault().getPath(STORAGE, currentUser.getUsername()).relativize(createdPackagePath).toString();
    }

    @Transactional
    public void deletePackage(DeletePackageRequest deletePackageRequest) throws IOException {
        User currentUser = authService.getCurrentUser();
        String packageName = deletePackageRequest.getPackageName();
        checkService.packageExists(currentUser, packageName);
        if(infoService.infoExists(packageName, currentUser)) {
            Info info = infoService.getInfo(packageName, currentUser);
            checkService.infoIsNotInProject(info);
            infoService.deleteInfo(info.getInfoName(), currentUser);
        }
        if(folderService.itemExists(getDefault().getPath(STORAGE, currentUser.getUsername(), packageName))) {
            folderService.deleteItem(getDefault().getPath(STORAGE, currentUser.getUsername(), packageName));
        }
    }

    public Info getInfo(String infoName) {
        User currentUser = authService.getCurrentUser();
        return infoService.getInfo(infoName, currentUser);
    }

    public Info createInfo(CreateInfoRequest createInfoRequest) {
        User currentUser = authService.getCurrentUser();
        return infoService.createInfo(createInfoRequest, currentUser);
    }

    public Info updateInfo(UpdateInfoRequest updateInfoRequest) {
        User currentUser = authService.getCurrentUser();
        return infoService.updateInfo(updateInfoRequest, currentUser);
    }

    public String createStorage(String storageName) throws IOException {
        User currentUser = authService.getCurrentUser();
        return folderService.createFolder(getDefault().getPath(STORAGE, currentUser.getUsername(), storageName)).getFileName().toString();
    }

    public PathNode getPackageFolderStructure(String storageName) {
        User currentUser = authService.getCurrentUser();
        return folderService.getFolderStructure(getDefault().getPath(STORAGE, currentUser.getUsername(), storageName));
    }

    public String createFolder(CreateFolderRequest createFolderRequest) throws IOException {
        User currentUser = authService.getCurrentUser();
        Path newFolderPath = getDefault().getPath(STORAGE, currentUser.getUsername(), createFolderRequest.getPackageName(),
                createFolderRequest.getParentFolderRelativePath(), createFolderRequest.getNewFolderName());

        checkService.folderDoesNotExist(newFolderPath);

        Path createdFolderPath = folderService.createFolder(newFolderPath);
        Path basePath = getDefault().getPath(STORAGE, currentUser.getUsername(), createFolderRequest.getPackageName());
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
        return packageList;
    }

}
