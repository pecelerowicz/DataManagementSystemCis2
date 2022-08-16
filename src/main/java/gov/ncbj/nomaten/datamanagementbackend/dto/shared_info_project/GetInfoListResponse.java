package gov.ncbj.nomaten.datamanagementbackend.dto.shared_info_project;

import gov.ncbj.nomaten.datamanagementbackend.comparator.InfoComparator;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetInfoListResponse {
    private List<String> infoNameList;
    public GetInfoListResponse(List<Info> infoList) {
        infoNameList = infoList.stream()
                .sorted(new InfoComparator())
                .map(Info::getInfoName)
                .collect(Collectors.toList());
    }
}
