package gov.ncbj.nomaten.datamanagementbackend.service.main;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_search.GetSearchListRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.Search;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.repository.InfoRepository;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.InfoService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;
import static java.nio.file.FileSystems.getDefault;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class AllDataService {

    private final AuthService authService;
    private final InfoService infoService;
    private final FolderService folderService;

    public List<String> getTypeList() {
        return Arrays.asList("General", "Difrractometer"/*, "Test"*/); // TODO change
    }

    public List<String> getUsers() {
        return authService.getUsers();
    }

    public List<Search> getSearchList(GetSearchListRequest getSearchListRequest) {
        String userName = getSearchListRequest.getUserName();
        boolean hasInfo = getSearchListRequest.isHasInfo();
        boolean hasDifrInfo = getSearchListRequest.isHasDifrInfo();
        boolean hasTestInfo = getSearchListRequest.isHasTestInfo();

        List<Info> infoList;
        if(userName.isEmpty()) {
            infoList = this.infoService.findAll();
        } else {
            infoList = this.infoService.findByUserUsername(userName);
        }

        List<Search> searchList;
        if(!hasInfo) {
            searchList = infoListToSearchList(infoList);
        } else {
            if(hasDifrInfo) {
                searchList = infoListToSearchList(infoList.stream().filter(i -> i.getDifrInfo() != null).collect(toList()));
            } else if(hasTestInfo) {
                searchList =  infoListToSearchList(infoList.stream().filter(i -> i.getTestInfo() != null).collect(toList()));
            } else {
                searchList = infoListToSearchList(infoList.stream().filter(i -> i.getTestInfo() == null && i.getDifrInfo() == null).collect(toList()));
            }
        }
        Collections.sort(searchList);
        return searchList;
    }

    public Info getInfoOfUser(String userName, String infoName) {
        User user = authService.getUserByName(userName);
        return infoService.getInfoOfUser(user, infoName);
    }

    public PathNode getPackageFolderStructureOfUser(String userName, String storageName) {
        return folderService.getFolderStructure(getDefault().getPath(STORAGE, userName, storageName));
    }

    public Resource downloadFileOfUser(String userName, String packageName, String fileNameWithPath) {
        return folderService.downloadFileOfUser(userName, packageName, fileNameWithPath);
    }

    private List<Search> infoListToSearchList(List<Info> infoList) { // TODO clean
        return infoList.stream()
                .filter(i -> i.getAccess().equals(Info.Access.PUBLIC))
                .map(i -> Search.builder()
                        .name(i.getInfoName())
                        .username(i.getUser().getUsername())
                        .hasStorage(Files.exists(getPackagePath(i)) && Files.isDirectory(getPackagePath(i)))
                        .localDateTime(i.getLocalDateTime())
                        .build())
                .sorted()
                .collect(toList());
    }

    private Path getPackagePath(Info info) {
        String username = info.getUser().getUsername();
        String name = info.getInfoName();
        return getDefault().getPath(STORAGE, username, name);
    }

}
