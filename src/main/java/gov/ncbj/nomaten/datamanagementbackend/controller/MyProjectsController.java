package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.shared_info_project.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.shared_info_project.GetInfoListResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_projects.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.shared_info_project.GetProjectResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.shared_info_project.GetProjectsResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.service.action.MyProjectsService;
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

    /**
     * LEFT PANEL
     * Gives back list of projects
     */
    @GetMapping("/project")
    public ResponseEntity<GetProjectsResponse> getOwnedProjects() {
        return ok(projectListToGetProjectsResponse(myProjectsService.getOwnedProjects()));
    }

    /**
     * LEFT PANEL
     * Used to retrieve 1) description, 2) packages, 3) members
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<GetProjectResponse> getOwnedProject(@PathVariable Long projectId) {
        // TODO validation
        return ok(projectToGetProjectResponse(myProjectsService.getOwnedProject(projectId)));
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
     * LEFT PANEL
     */
    @DeleteMapping("/project")
    // TODO check this method (there was no transactional and it seemed to have worked correctly nevertheless)
    public ResponseEntity<GetProjectsResponse> deleteOwnedProject(@RequestBody DeleteOwnedProjectRequest deleteOwnedProjectRequest) {
        DeleteOwnedProjectRequestValidator.builder().build().validate(deleteOwnedProjectRequest);
        return ok(projectListToGetProjectsResponse(myProjectsService.deleteMyProject(deleteOwnedProjectRequest)));
    }

    /**
     * RIGHT PANEL
     * Updates project description
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
    @DeleteMapping("/project/user")
    // TODO check this method
    public ResponseEntity<RemoveUserFromOwnedProjectResponse> removeUserFromOwnedProject(@RequestBody RemoveUserFromOwnedProjectRequest removeUserFromOwnedProjectRequest) {
        RemoveUserFromOwnedProjectRequestValidator.builder().build().validate(removeUserFromOwnedProjectRequest);
        return ok(projectToRemoveUserFromOwnedProjectResponse(myProjectsService.removeUserFromMyProject(removeUserFromOwnedProjectRequest)));
    }

    /**
     * RIGHT PANEL
     * Gives back info list in a given project
     */
    @GetMapping("/project/info")
    public ResponseEntity<GetInfoListResponse> getInfoList() {
        return ResponseEntity.status(OK).body(new GetInfoListResponse(myProjectsService.getInfoList()));
    }

    /**
     * RIGHT PANEL
     */
    @PostMapping("/project/info")
    public ResponseEntity<AddMyInfoToOwnedProjectResponse> addMyInfoToOwnedProject(@RequestBody AddMyInfoToOwnedProjectRequest addMyInfoToOwnedProjectRequest) {
        AddMyInfoToOwnedProjectRequestValidator.builder().build().validate(addMyInfoToOwnedProjectRequest);
        return ok(projectToAddInfoToOwnedProjectResponse(myProjectsService.addMyInfoToMyProject(addMyInfoToOwnedProjectRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @DeleteMapping("/project/info")
    public ResponseEntity<RemoveInfoFromOwnedProjectResponse> removeInfoFromOwnedProject(@RequestBody RemoveInfoFromOwnedProjectRequest removeInfoFromOwnedProjectRequest) {
        RemoveInfoFromOwnedProjectRequestValidator.builder().build().validate(removeInfoFromOwnedProjectRequest);
        // TODO check if it correctly removes other ppl's Infos from the Project owned by me
        return ok(projectToRemoveInfoFromOwnedProjectResponse(myProjectsService.removeInfoFromMyProject(removeInfoFromOwnedProjectRequest)));
    }

    /*******************************************************************************************************************
     * BELOW CONTROLLERS RELATED TO INFO WITHIN A PROJECT
     ******************************************************************************************************************/

    /**
     * RIGHT PANEL
     */
    @GetMapping("/project/packages/info/{projectId}/{infoName}")
    public ResponseEntity<GetInfoResponse> getMetadataOfInfoInProject(@PathVariable Long projectId,
                                                                      @PathVariable String infoName,
                                                                      @PathVariable String userName) {
        // TODO projectId validation (?)
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(myProjectsService.getInfoInMyProject(projectId, infoName, userName)));
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/project/packages/folder/{projectId}/{userName}/{infoName}")
    public PathNode getPackageFolderStructure(@PathVariable Long projectId, @PathVariable String userName, @PathVariable String infoName) {
        // TODO id validation (?)
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return myProjectsService.getPackageFolderStructure(projectId, userName, infoName);
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFileOfProject(@RequestParam Long projectId, @RequestParam String userName, @RequestParam String infoName, @RequestParam String fileNameWithPath) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        // TODO validate remaining request params
        Resource resource = myProjectsService.downloadFileOfProject(projectId, userName, infoName, fileNameWithPath);
        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }
}
