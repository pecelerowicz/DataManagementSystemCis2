package gov.ncbj.nomaten.datamanagementbackend.dto.my_storage;

import gov.ncbj.nomaten.datamanagementbackend.model.Info;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoResponse {
    private String name;
    private String description;
    public InfoResponse(Info info) {
        this.name = info.getName();
        this.description = info.getDescription();
    }
}
