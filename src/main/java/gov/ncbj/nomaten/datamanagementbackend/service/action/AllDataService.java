package gov.ncbj.nomaten.datamanagementbackend.service.action;

import gov.ncbj.nomaten.datamanagementbackend.dto.all_data.GetSearchListRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.Search;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.service.support.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.CheckService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.InfoService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.GENERAL;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;
import static java.nio.file.FileSystems.getDefault;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class AllDataService {

    private final AuthService authService;
    private final InfoService infoService;
    private final FolderService folderService;
    private final CheckService checkService;

    public List<String> getTypeList() {
        return Arrays.asList("General", "Difrractometer"/*, "Test"*/); // TODO change
    }

    public List<User> getUsers() {
        return authService.getUsers();
    }

    public List<Search> getSearchList(GetSearchListRequest getSearchListRequest) {
        String userName = getSearchListRequest.getUserName();
        List<Info> infoList = userName.isEmpty() ? this.infoService.findAll() : this.infoService.findByUserUsername(userName);
        List<Info> publicInfoList = filterPublicInfo(infoList);

        List<Search> searchList;
        if(!getSearchListRequest.isHasInfo()) {
            searchList = infoListToSearchList(publicInfoList);
        } else {
            if(getSearchListRequest.isHasDifrInfo()) {
                searchList = infoListToSearchList(publicInfoList.stream().filter(i -> i.getDifrInfo() != null).collect(toList()));
            } else if(getSearchListRequest.isHasTestInfo()) {
                searchList =  infoListToSearchList(publicInfoList.stream().filter(i -> i.getTestInfo() != null).collect(toList()));
            } else {
                searchList = infoListToSearchList(publicInfoList.stream().filter(i -> i.getTestInfo() == null &&
                                                                                i.getDifrInfo() == null).collect(toList()));
            }
        }
        return searchList;
    }

    public Info getInfoOfUser(String userName, String infoName) {
        User packageOwner = authService.getUserByName(userName);
        Info info = infoService.getInfo(infoName, packageOwner);
        checkService.infoIsPublic(info);
        return info;
    }

    public PathNode getPackageFolderStructureOfUser(String userName, String storageName) {
        User packageOwner = authService.getUserByName(userName);
        Info info = infoService.getInfo(storageName, packageOwner);
        checkService.infoIsPublic(info);
        return folderService.getFolderStructure(getDefault().getPath(STORAGE, GENERAL, userName, storageName));
    }

    public Resource downloadFileOfUser(String userName, String packageName, String fileNameWithPath) {
        User packageOwner = authService.getUserByName(userName);
        Info info = infoService.getInfo(packageName, packageOwner);
        checkService.infoIsPublic(info);
        return folderService.downloadFile(packageName, userName, fileNameWithPath);
    }

    private List<Info> filterPublicInfo(List <Info> infoList) {
        return infoList.stream().filter(i -> i.getAccess().equals(Info.Access.PUBLIC)).collect(toList());
    }

    private List<Search> infoListToSearchList(List<Info> infoList) {
        return infoList.stream()
                .map(i -> Search.builder()
                        .name(i.getInfoName())
                        .username(i.getUser().getUsername())
                        .hasStorage(folderService.itemExists(getPackagePath(i)) && folderService.isDirectory(getPackagePath(i)))
                        .localDateTime(i.getLocalDateTime())
                        .build())
                .collect(toList());
    }

    private Path getPackagePath(Info info) {
        String username = info.getUser().getUsername();
        String name = info.getInfoName();
        return getDefault().getPath(STORAGE, GENERAL, username, name);
    }

}
