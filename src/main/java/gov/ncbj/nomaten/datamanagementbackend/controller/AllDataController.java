package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.comparator.UserComparator;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_search.GetSearchListRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_search.GetSearchListResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.service.action.AllDataService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_search.GetSearchListRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.InfoMapper.infoToGetInfoResponse;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/all-data")
public class AllDataController {

    private final AllDataService allDataService;

    /**
     * LEFT PANEL
     */
    @GetMapping("/types")
    public ResponseEntity<List<String>> getTypeList() {
        return ok(allDataService.getTypeList());
    }

    /**
     * LEFT PANEL
     */
    @GetMapping("/users")
    public ResponseEntity<List<String>> getUsers() { // TODO wrap in a proper DTO
        return ok(allDataService.getUsers().stream().sorted(new UserComparator()).map(User::getUsername).collect(Collectors.toList()));
    }

    /**
     * LEFT PANEL
     * POST just to be able to send JSON. No content creation here!
     */
    @PostMapping("/search")
    public GetSearchListResponse getSearchList(@RequestBody GetSearchListRequest getSearchListRequest) {
        GetSearchListRequestValidator.builder().build().validate(getSearchListRequest);
        return new GetSearchListResponse(allDataService.getSearchList(getSearchListRequest));
    }

    /**
     * LEFT PANEL
     */
    @GetMapping("/info/{userName}/{infoName}")
    public ResponseEntity<GetInfoResponse> getInfoOfUser(@PathVariable String userName, @PathVariable String infoName) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(allDataService.getInfoOfUser(userName, infoName)));
    }

    /**
     * LEFT PANEL
     */
    @GetMapping("/folders/{userName}/{storageName}")
    public PathNode getPackageFolderStructureOfUser(@PathVariable String userName, @PathVariable String storageName) {
        NameValidator.builder().build().validate(storageName);
        UserNameValidator.builder().build().validate(userName);
        return allDataService.getPackageFolderStructureOfUser(userName, storageName);
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/download/user")
    public ResponseEntity<Resource> downloadFileOfUser(@RequestParam String userName, @RequestParam String packageName, @RequestParam String fileNameWithPath) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(packageName);
        // todo validate fileNameWithPath
        Resource resource = allDataService.downloadFileOfUser(userName, packageName, fileNameWithPath);
        return ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }

}
