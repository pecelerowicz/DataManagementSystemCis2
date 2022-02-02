package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_search.GetSearchListRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.Search;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.repository.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static java.nio.file.FileSystems.getDefault;
import static java.util.stream.Collectors.toList;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

@Service
public class SearchService {

    @Autowired
    InfoRepository infoRepository;

    public List<Search> getSearchList(GetSearchListRequest getSearchListRequest) {
        String userName = getSearchListRequest.getUserName();
        boolean hasInfo = getSearchListRequest.isHasInfo();
        boolean hasDifrInfo = getSearchListRequest.isHasDifrInfo();
        boolean hasTestInfo = getSearchListRequest.isHasTestInfo();

        List<Info> infoList;
        if(userName.isEmpty()) {
            infoList = this.infoRepository.findAll();
        } else {
            infoList = this.infoRepository.findByUserUsername(userName);
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

    private List<Search> infoListToSearchList(List<Info> infoList) {
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
