package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.GetInfoListResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.InfoService;
import gov.ncbj.nomaten.datamanagementbackend.service.main.MyProjectsService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_project.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.InfoMapper.infoToGetInfoResponse;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.project.ProjectMapper.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/my-projects")
public class MyProjectsController {

    private final MyProjectsService myProjectsService;
    private final FolderService folderService;
    private final InfoService infoService; // to be removed from here

    private final AuthService authService; // for now. To be removed later

    /**
     * LEFT PANEL
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<GetProjectResponse> getOwnedProject(@PathVariable Long projectId) {
        // TODO validation
        return ok(projectToGetProjectResponse(myProjectsService.getOwnedProject(projectId)));
    }

    /**
     * LEFT PANEL
     */
    @GetMapping("/project")
    public ResponseEntity<GetProjectsResponse> getOwnedProjects() {
        return ok(projectListToGetProjectsResponse(myProjectsService.getOwnedProjects()));
    }

    /**
     * LEFT PANEL
     */
    @PostMapping("/project")
    public ResponseEntity<CreateProjectResponse> createOwnedProject(@RequestBody CreateProjectRequest createProjectRequest) {
        CreateProjectRequestValidator.builder().build().validate(createProjectRequest);
        return ok(projectToCreateProjectResponse(myProjectsService.createOwnedProject(createProjectRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @PutMapping("/project")
    public ResponseEntity<UpdateProjectResponse> updateOwnedProject(@RequestBody UpdateProjectRequest updateProjectRequest) {
        UpdateProjectRequestValidator.builder().build().validate(updateProjectRequest);
        return ok(projectToUpdateProjectResponse(myProjectsService.updateOwnedProject(updateProjectRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @PostMapping("/project/user")
    public ResponseEntity<AddUserResponse> addUserToOwnedProject(@RequestBody AddUserRequest addUserRequest) {
        AddUserRequestValidator.builder().build().validate(addUserRequest);
        return ok(projectToAddUserResponse(myProjectsService.addUserToOwnedProject(addUserRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/project/info")
    public ResponseEntity<GetInfoListResponse> getInfoList() {
        User user = authService.getCurrentUser(); // infoService to be removed from here
        return ResponseEntity.status(OK).body(new GetInfoListResponse(infoService.getInfoList(user)));
    }

    /**
     * RIGHT PANEL
     */
    @PostMapping("/project/info")
    public ResponseEntity<AddMyInfoToOwnedProjectResponse> addMyInfoToOwnedProject(@RequestBody AddMyInfoToOwnedProjectRequest addMyInfoToOwnedProjectRequest) {
        AddMyInfoToOwnedProjectRequestValidator.builder().build().validate(addMyInfoToOwnedProjectRequest);
        return ok(projectToAddInfoToOwnedProjectResponse(myProjectsService.addMyInfoToOwnedProject(addMyInfoToOwnedProjectRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @DeleteMapping("/project/info")
    public ResponseEntity<RemoveInfoFromOwnedProjectResponse> removeInfoFromOwnedProject(@RequestBody RemoveInfoFromOwnedProjectRequest removeInfoFromOwnedProjectRequest) {
        RemoveInfoFromOwnedProjectRequestValidator.builder().build().validate(removeInfoFromOwnedProjectRequest);
        // TODO check if it correctly removes other ppl's Infos from the Project owned by me
        return ok(projectToRemoveInfoFromOwnedProjectResponse(myProjectsService.removeInfoFromOwnedProject(removeInfoFromOwnedProjectRequest)));
    }

    //////

    @GetMapping("/project/packages/info/{projectId}/{infoName}")
    public ResponseEntity<GetInfoResponse> getMetadataOfInfoInProject(@PathVariable Long projectId,
                                                                      @PathVariable String infoName,
                                                                      @PathVariable String userName) {
        // TODO projectId validation (?)
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(myProjectsService.getInfoInMyProject(projectId, infoName, userName)));
    }

    //////



    /**
     * RIGHT PANEL
     */
    @DeleteMapping("/project/user")
    // TODO check this method
    public ResponseEntity<RemoveUserFromOwnedProjectResponse> removeUserFromOwnedProject(@RequestBody RemoveUserFromOwnedProjectRequest removeUserFromOwnedProjectRequest) {
        RemoveUserFromOwnedProjectRequestValidator.builder().build().validate(removeUserFromOwnedProjectRequest);
        return ok(projectToRemoveUserFromOwnedProjectResponse(myProjectsService.removeUserFromOwnedProject(removeUserFromOwnedProjectRequest)));
    }

    /**
     * LEFT PANEL
     */
    @DeleteMapping("/project")
    // TODO check this method (there was no transactional and it seemed to have worked correctly nevertheless)
    public ResponseEntity<GetProjectsResponse> deleteOwnedProject(@RequestBody DeleteOwnedProjectRequest deleteOwnedProjectRequest) {
        DeleteOwnedProjectRequestValidator.builder().build().validate(deleteOwnedProjectRequest);
        return ok(projectListToGetProjectsResponse(myProjectsService.deleteOwnedProject(deleteOwnedProjectRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFileOfProject(@RequestParam String projectId, @RequestParam String userName, @RequestParam String infoName, @RequestParam String fileNameWithPath) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        // TODO validate remaining request params
        Resource resource = folderService.downloadFileOfProject(projectId, userName, infoName, fileNameWithPath);
        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }
}
