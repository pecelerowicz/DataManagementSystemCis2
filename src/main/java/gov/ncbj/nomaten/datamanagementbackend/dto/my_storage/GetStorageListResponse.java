package gov.ncbj.nomaten.datamanagementbackend.dto.my_storage;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class GetStorageListResponse {
    private List<String> storageNameList;
    public static GetStorageListResponse pathNodeToPackagesResponse(PathNode pathNode) {
        List<String> names = pathNode.getChildren()
                .stream()
                .map(p -> p.getPath().toFile().toString())
                .map(p -> p.substring(p.lastIndexOf("/") + 1))
                // TODO remove later for Linux servers
                .map(p -> p.substring(p.lastIndexOf("\\") + 1))
                .collect(toList());
        return new GetStorageListResponse(names);
    }
}


