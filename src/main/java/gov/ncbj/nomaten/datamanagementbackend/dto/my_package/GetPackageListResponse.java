package gov.ncbj.nomaten.datamanagementbackend.dto.my_package;

import gov.ncbj.nomaten.datamanagementbackend.model.Package;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GetPackageListResponse {
    List<PackageResponse> packageResponseList;

    public GetPackageListResponse(List<Package> packageList) {
        this.packageResponseList = packageList.stream()
                .map(s -> new PackageResponse(s.getName(),
                        s.isHasStorage(),
                        s.isHasMetadata(),
                        s.getTitle() != null ? s.getTitle() : null,
                        s.getShortDescription() != null ? s.getShortDescription() : null,
                        s.getLocalDateTime() != null ? s.getLocalDateTime().toLocalDate() : null))
                .collect(Collectors.toList());
    }

}
