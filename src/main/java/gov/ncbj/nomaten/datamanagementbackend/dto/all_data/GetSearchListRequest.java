package gov.ncbj.nomaten.datamanagementbackend.dto.all_data;

import lombok.Data;

@Data
public class GetSearchListRequest {
    private String userName;

    private boolean hasInfo;
    private boolean hasDifrInfo;
    private boolean hasTestInfo;
}
