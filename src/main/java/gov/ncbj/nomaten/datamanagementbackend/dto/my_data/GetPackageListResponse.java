package gov.ncbj.nomaten.datamanagementbackend.dto.my_data;

import gov.ncbj.nomaten.datamanagementbackend.comparator.PackageComparator;
import gov.ncbj.nomaten.datamanagementbackend.model.Package;
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
                .sorted(new PackageComparator())
                .map(s -> new PackageResponse(s.getName(),
                        s.isHasStorage(),
                        s.isHasMetadata(),
                        s.isArchived(),
                        s.getTitle() != null ? s.getTitle() : null,
                        s.getShortDescription() != null ? s.getShortDescription() : null,
                        s.getLocalDateTime() != null ? s.getLocalDateTime().toLocalDate() : null))
                .collect(Collectors.toList());
    }

}
