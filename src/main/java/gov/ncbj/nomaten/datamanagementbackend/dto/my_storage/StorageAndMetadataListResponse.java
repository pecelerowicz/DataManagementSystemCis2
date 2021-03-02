package gov.ncbj.nomaten.datamanagementbackend.dto.my_storage;

import gov.ncbj.nomaten.datamanagementbackend.model.StorageAndMetadata;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class StorageAndMetadataListResponse {
    List<StorageAndMetadataResponse> storageAndMetadataResponseList;

    public StorageAndMetadataListResponse(List<StorageAndMetadata> storageAndMetadataList) {
        this.storageAndMetadataResponseList = storageAndMetadataList.stream()
                .map(s -> new StorageAndMetadataResponse(s.getName(), s.isHasStorage(), s.isHasMetadata()))
                .collect(Collectors.toList());
    }

}
