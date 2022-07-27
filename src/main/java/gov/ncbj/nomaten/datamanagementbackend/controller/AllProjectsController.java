package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.GetInfoListResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.service.action.AllProjectsService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_project.AddMyInfoToOtherProjectRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_project.RemoveMyInfoFromOtherProjectRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.InfoMapper.infoToGetInfoResponse;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.project.ProjectMapper.*;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/all-projects")
public class AllProjectsController {

    private final AllProjectsService allProjectsService;

    /**
     * LEFT PANEL
     * Used for retrieving description, packages and members
     */
    @GetMapping("/project/{projectId}")
    // TODO check this method
    public ResponseEntity<GetProjectResponse> getProject(@PathVariable Long projectId) {
        // TODO idValidation
        return ok(projectToGetProjectResponse(allProjectsService.getProject(projectId)));
    }

    /**
     * LEFT PANEL
     */
    @GetMapping("/project")
    // TODO check this method
    public ResponseEntity<GetProjectsResponse> getProjects() {
        return ok(projectListToGetProjectsResponse(allProjectsService.getProjects()));
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/info")
    public ResponseEntity<GetInfoListResponse> getInfoList() {
        return ok(new GetInfoListResponse(allProjectsService.getInfoList()));
    }

    /**
     * RIGHT PANEL
     */
    @PostMapping("/project/info")
    public ResponseEntity<AddMyInfoToOtherProjectResponse> addMyInfoToOtherProject(@RequestBody AddMyInfoToOtherProjectRequest addMyInfoToOtherProjectRequest) {
        AddMyInfoToOtherProjectRequestValidator.builder().build().validate(addMyInfoToOtherProjectRequest);
        return ok(projectToAddMyInfoToOtherProjectResponse(allProjectsService.addMyInfoToOtherProject(addMyInfoToOtherProjectRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @DeleteMapping("/project/info")
    // TODO check this method
    public ResponseEntity<RemoveMyInfoFromOtherProjectResponse> removeMyInfoFromOtherProject(@RequestBody RemoveMyInfoFromOtherProjectRequest removeMyInfoFromOtherProjectRequest) {
        RemoveMyInfoFromOtherProjectRequestValidator.builder().build().validate(removeMyInfoFromOtherProjectRequest);
        return ok(projectToRemoveMyInfoFromOtherProjectResponse(allProjectsService.removeMyInfoFromOtherProject(removeMyInfoFromOtherProjectRequest)));
    }

    // TODO check what happens if I delete Info which belongs to the project (my project, other project, multiple projects)
    /**
     * RIGHT PANEL
     */
    @GetMapping("/project/packages/info/{projectId}/{userName}/{infoName}") // response type is borrowed for now
    public ResponseEntity<GetInfoResponse> getInfoOfUserAndProject(@PathVariable Long projectId, @PathVariable String userName, @PathVariable String infoName) {
        // TODO id validation (?)
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(allProjectsService.getInfoOfUserAndProject(projectId, userName, infoName)));
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/project/packages/folder/{projectId}/{userName}/{infoName}")
    public PathNode getPackageFolderStructureOfUserAndProject(@PathVariable Long projectId, @PathVariable String userName, @PathVariable String infoName) {
        // TODO id validation (?)
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return allProjectsService.getPackageFolderStructureOfUserAndProject(projectId, userName, infoName);
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFileOfProject(@RequestParam Long projectId, @RequestParam String userName, @RequestParam String infoName, @RequestParam String fileNameWithPath) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        // TODO validate remaining request params
        Resource resource = allProjectsService.downloadFileOfProject(projectId, userName, infoName, fileNameWithPath);
        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }
}
