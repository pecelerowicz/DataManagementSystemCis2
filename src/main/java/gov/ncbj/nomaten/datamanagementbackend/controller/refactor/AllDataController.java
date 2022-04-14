package gov.ncbj.nomaten.datamanagementbackend.controller.refactor;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_search.GetSearchListRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_search.GetSearchListResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.service.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.service.InfoService;
import gov.ncbj.nomaten.datamanagementbackend.service.SearchService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_search.GetSearchListRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.InfoMapper.infoToGetInfoResponse;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/all-data")
public class AllDataController {

    private final InfoService infoService;
    private final FolderService folderService;
    private final SearchService searchService;

    @PostMapping("/search") // POST just to be able to send JSON. No content creation here!
    public GetSearchListResponse getSearchList(@RequestBody GetSearchListRequest getSearchListRequest) {
        GetSearchListRequestValidator.builder().build().validate(getSearchListRequest);
        return new GetSearchListResponse(searchService.getSearchList(getSearchListRequest));
    }

    // TODO it might be better not to hardcode it ...
    @GetMapping("/types")
    public ResponseEntity<List<String>> getTypeList() {
        return ok(Arrays.asList("General", "Difrractometer"/*, "Test"*/));
    }

    @GetMapping("/info/{userName}/{infoName}") // używane w search
    public ResponseEntity<GetInfoResponse> getInfoOfUser(@PathVariable String userName, @PathVariable String infoName) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(infoService.getInfoOfUser(userName, infoName)));
    }

    @GetMapping("/folders/{userName}/{storageName}")
    public PathNode getPackageFolderStructureOfUser(@PathVariable String userName, @PathVariable String storageName) {
        NameValidator.builder().build().validate(storageName);
        UserNameValidator.builder().build().validate(userName);
        return folderService.getPackageFolderStructureOfUser(userName, storageName);
    }

    @GetMapping("/download/user")
    public ResponseEntity<Resource> downloadFileOfUser(@RequestParam String userName, @RequestParam String packageName, @RequestParam String fileNameWithPath) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(packageName);
        // todo validate fileNameWithPath
        Resource resource = folderService.downloadFileOfUser(userName, packageName, fileNameWithPath);
        return ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }


}
