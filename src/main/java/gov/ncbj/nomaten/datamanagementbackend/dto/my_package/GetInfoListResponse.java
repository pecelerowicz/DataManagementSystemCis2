package gov.ncbj.nomaten.datamanagementbackend.dto.my_package;

import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetInfoListResponse {
    private List<String> infoNameList;
    public GetInfoListResponse(List<Info> infoList) {
        infoNameList = infoList.stream().map(info -> info.getInfoName()).collect(Collectors.toList());
    }
}
