package gov.ncbj.nomaten.datamanagementbackend.dto.my_storage;

import gov.ncbj.nomaten.datamanagementbackend.model.Info;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class InfoListResponse {
    private List<InfoResponse> infoResponseList;
    public InfoListResponse(List<Info> infoList) {
        this.infoResponseList = infoList.stream()
                .map(InfoResponse::new)
                .collect(Collectors.toList());
    }
}
