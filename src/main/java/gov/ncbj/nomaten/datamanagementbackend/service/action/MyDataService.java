package gov.ncbj.nomaten.datamanagementbackend.service.action;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.*;
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

    /**
     * Might cause consistency issues.
     * Very defensive approach here.
     * Investigate why order matters.
     */
    @Transactional
    public void renamePackage(RenamePackageRequest renamePackageRequest) throws IOException {
        User currentUser = authService.getCurrentUser();
        checkService.packageExists(currentUser, renamePackageRequest.getPackageOldName());
        checkService.packageDoesNotExist(currentUser, renamePackageRequest.getPackageNewName());
        if(infoService.infoExists(renamePackageRequest.getPackageOldName(), currentUser)) {
            Info info = infoService.getInfo(renamePackageRequest.getPackageOldName(), currentUser);
            checkService.infoIsNotArchived(info, "package archived");
        }
        if(folderService.itemExists(getDefault().getPath(STORAGE, currentUser.getUsername(), renamePackageRequest.getPackageOldName())) &&
           folderService.isDirectory(getDefault().getPath(STORAGE, currentUser.getUsername(), renamePackageRequest.getPackageOldName()))) {
                folderService.renameItem(getDefault().getPath(STORAGE, currentUser.getUsername()),
                        renamePackageRequest.getPackageOldName(), renamePackageRequest.getPackageNewName());
        }
        if(infoService.infoExists(renamePackageRequest.getPackageOldName(), currentUser)) {
            Info info = infoService.getInfo(renamePackageRequest.getPackageOldName(), currentUser);
            checkService.infoIsNotArchived(info, "package archived");
            info.setInfoName(renamePackageRequest.getPackageNewName());
        }
    }

    @Transactional
    public void deletePackage(DeletePackageRequest deletePackageRequest) throws IOException {
        User currentUser = authService.getCurrentUser();
        String packageName = deletePackageRequest.getPackageName();
        checkService.packageExists(currentUser, packageName);
        if(infoService.infoExists(packageName, currentUser)) {
            Info info = infoService.getInfo(packageName, currentUser);
            checkService.infoIsNotArchived(info, "The package is archived and cannot be deleted.");
            checkService.infoIsNotInProject(info);
            infoService.deleteInfo(info.getInfoName(), currentUser);
        }
        if(folderService.itemExists(getDefault().getPath(STORAGE, currentUser.getUsername(), packageName))) {
            folderService.deleteItem(getDefault().getPath(STORAGE, currentUser.getUsername(), packageName));
        }
    }

    @Transactional
    public void archivePackage(ArchivePackageRequest archivePackageRequest) {
        User currentUser = authService.getCurrentUser();
        String packageName = archivePackageRequest.getPackageName();
        checkService.packageExists(currentUser, packageName);
        checkService.packageIsReadyToBeArchived(currentUser, packageName);
        Info info = infoService.getInfo(packageName, currentUser);
        info.setArchived(true);
    }

    // TODO should probably be removed later
    public boolean isArchived(String packageName) {
        User currentUser = authService.getCurrentUser();
        if(infoService.infoExists(packageName, currentUser)) {
            Info info = infoService.getInfo(packageName, currentUser);
            return info.getArchived() != null ? info.getArchived() : false;
        }
        return false;
    }

    public Info getInfo(String infoName) {
        User currentUser = authService.getCurrentUser();
        return infoService.getInfo(infoName, currentUser);
    }

    public Info createInfo(CreateInfoRequest createInfoRequest) {
        User currentUser = authService.getCurrentUser();
        checkService.userDoesNotHaveInfo(createInfoRequest.getInfoName(), currentUser);
        return infoService.createInfo(createInfoRequest, currentUser);
    }

    public Info updateInfo(UpdateInfoRequest updateInfoRequest) {
        User currentUser = authService.getCurrentUser();
        checkService.userHasInfo(updateInfoRequest.getInfoName(), currentUser);
        checkService.packageIsNotArchived(currentUser, updateInfoRequest.getInfoName(), "package archived");
        return infoService.updateInfo(updateInfoRequest, currentUser);
    }

    public String createStorage(String storageName) throws IOException {
        User currentUser = authService.getCurrentUser();
        checkService.folderDoesNotExist(getDefault().getPath(STORAGE, currentUser.getUsername(), storageName));
        return folderService.createFolder(getDefault().getPath(STORAGE, currentUser.getUsername(), storageName)).getFileName().toString();
    }

    public PathNode getPackageFolderStructure(String storageName) {
        User currentUser = authService.getCurrentUser();
        checkService.folderExists(getDefault().getPath(STORAGE, currentUser.getUsername(), storageName),
                "Package " + storageName + " does not exist");
        return folderService.getFolderStructure(getDefault().getPath(STORAGE, currentUser.getUsername(), storageName));
    }

    public String createFolder(CreateFolderRequest createFolderRequest) throws IOException {
        User currentUser = authService.getCurrentUser();
        Path newFolderPath = getDefault().getPath(STORAGE, currentUser.getUsername(), createFolderRequest.getPackageName(),
                createFolderRequest.getParentFolderRelativePath(), createFolderRequest.getNewFolderName());
        checkService.packageIsNotArchived(currentUser, createFolderRequest.getPackageName(),
                "The package is archived and cannot be modified.");
        checkService.folderDoesNotExist(newFolderPath);
        Path createdFolderPath = folderService.createFolder(newFolderPath);
        Path basePath = getDefault().getPath(STORAGE, currentUser.getUsername(), createFolderRequest.getPackageName());
        Path subPath = basePath.relativize(createdFolderPath);
        return subPath.toString();
    }

    public void deleteItem(DeleteItemRequest deleteItemRequest) throws IOException {
        User currentUser = authService.getCurrentUser();
        Path folderPath = getDefault().getPath(STORAGE, currentUser.getUsername(),
                deleteItemRequest.getPackageName(), deleteItemRequest.getItemPathString());
        checkService.packageIsNotArchived(currentUser, deleteItemRequest.getPackageName(),
                "The package is archived and cannot be modified.");
        folderService.deleteItem(folderPath);
    }

    public void uploadFile(MultipartFile file, String packageName, String folderRelativePath) throws IOException {
        User currentUser = authService.getCurrentUser();
        checkService.packageIsNotArchived(currentUser, packageName, "The package is archived and cannot be modified.");
        folderService.uploadFile(file, packageName, currentUser.getUsername(), folderRelativePath);
    }

    public Resource downloadFile(String packageName, String fileNameWithPath) {
        String userName = authService.getCurrentUser().getUsername();
        return folderService.downloadFile(packageName, userName, fileNameWithPath);
    }

    private List<Package> combineStorageWithMetadata(List<Info> infoList, List<String> storageNames) {
        List<Package> packageList = new LinkedList<>();
        for(Info info: infoList) {
            if(storageNames.contains(info.getInfoName())) {
                packageList.add(new Package(info.getInfoName(), true, true, isArchived(info.getArchived()), info.getTitle(), info.getShortDescription(), info.getLocalDateTime()));
            } else {
                packageList.add(new Package(info.getInfoName(), false, true, isArchived(info.getArchived()), info.getTitle(), info.getShortDescription(), info.getLocalDateTime()));
            }
        }
        Set<String> infoNameSet = infoList.stream().map(Info::getInfoName).collect(Collectors.toSet());
        for(String storageName: storageNames) {
            if(!infoNameSet.contains(storageName)) {
                packageList.add(new Package(storageName, true, false, false, null, null, null));
            }
        }
        return packageList;
    }

    private boolean isArchived(Boolean isArchived) {
        return isArchived != null && isArchived;
    }

}
