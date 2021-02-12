package gov.ncbj.nomaten.datamanagementbackend.dto.my_storage;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PackagesResponse {
    private List<String> packagesNames;
    public static PackagesResponse pathNodeToPackagesResponse(PathNode pathNode) {
        List<String> names = pathNode.getChildren()
                .stream()
                .map(p -> p.getPath().toFile().toString())
                .map(p -> p.substring(p.lastIndexOf("/") + 1))
                .collect(Collectors.toList());
        return new PackagesResponse(names);
    }
}


