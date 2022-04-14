package gov.ncbj.nomaten.datamanagementbackend.controller.refactor;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.GetInfoListResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.service.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.service.InfoService;
import gov.ncbj.nomaten.datamanagementbackend.service.ProjectService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_project.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.project.ProjectMapper.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/my-projects")
public class MyProjectsController {

    private final ProjectService projectService;
    private final FolderService folderService;
    private final InfoService infoService;

    // OWNED PROJECTS
    @GetMapping("/project/{projectId}")
    public ResponseEntity<GetProjectResponse> getOwnedProject(@PathVariable Long projectId) {
        // TODO validation
        return ok(projectToGetProjectResponse(projectService.getOwnedProject(projectId)));
    }

    @GetMapping("/project")
    public ResponseEntity<GetProjectsResponse> getOwnedProjects() {
        return ok(projectListToGetProjectsResponse(projectService.getOwnedProjects()));
    }

    @PostMapping("/project")
    public ResponseEntity<CreateProjectResponse> createOwnedProject(@RequestBody CreateProjectRequest createProjectRequest) {
        CreateProjectRequestValidator.builder().build().validate(createProjectRequest);
        return ok(projectToCreateProjectResponse(projectService.createOwnedProject(createProjectRequest)));
    }

    @PutMapping("/project")
    public ResponseEntity<UpdateProjectResponse> updateOwnedProject(@RequestBody UpdateProjectRequest updateProjectRequest) {
        UpdateProjectRequestValidator.builder().build().validate(updateProjectRequest);
        return ok(projectToUpdateProjectResponse(projectService.updateOwnedProject(updateProjectRequest)));
    }

    @PostMapping("/project/user")
    public ResponseEntity<AddUserResponse> addUserToOwnedProject(@RequestBody AddUserRequest addUserRequest) {
        AddUserRequestValidator.builder().build().validate(addUserRequest);
        return ok(projectToAddUserResponse(projectService.addUserToOwnedProject(addUserRequest)));
    }

    @GetMapping("/info")
    public ResponseEntity<GetInfoListResponse> getInfoList() {
        return ResponseEntity.status(OK).body(new GetInfoListResponse(infoService.getInfoList()));
    }

    @PostMapping("/project/info")
    public ResponseEntity<AddMyInfoToOwnedProjectResponse> addMyInfoToOwnedProject(@RequestBody AddMyInfoToOwnedProjectRequest addMyInfoToOwnedProjectRequest) {
        AddMyInfoToOwnedProjectRequestValidator.builder().build().validate(addMyInfoToOwnedProjectRequest);
        return ok(projectToAddInfoToOwnedProjectResponse(projectService.addMyInfoToOwnedProject(addMyInfoToOwnedProjectRequest)));
    }

    @DeleteMapping("/project/info")
    public ResponseEntity<RemoveInfoFromOwnedProjectResponse> removeInfoFromOwnedProject(@RequestBody RemoveInfoFromOwnedProjectRequest removeInfoFromOwnedProjectRequest) {
        RemoveInfoFromOwnedProjectRequestValidator.builder().build().validate(removeInfoFromOwnedProjectRequest);
        // TODO check if it correctly removes other ppl's Infos from the Project owned by me
        return ok(projectToRemoveInfoFromOwnedProjectResponse(projectService.removeInfoFromOwnedProject(removeInfoFromOwnedProjectRequest)));
    }

    @DeleteMapping("/project/user")
    // TODO check this method
    public ResponseEntity<RemoveUserFromOwnedProjectResponse> removeUserFromOwnedProject(@RequestBody RemoveUserFromOwnedProjectRequest removeUserFromOwnedProjectRequest) {
        RemoveUserFromOwnedProjectRequestValidator.builder().build().validate(removeUserFromOwnedProjectRequest);
        return ok(projectToRemoveUserFromOwnedProjectResponse(projectService.removeUserFromOwnedProject(removeUserFromOwnedProjectRequest)));
    }

    @DeleteMapping("/project")
    // TODO check this method (there was no transactional and it seemed to have worked correctly nevertheless)
    public ResponseEntity<GetProjectsResponse> deleteOwnedProject(@RequestBody DeleteOwnedProjectRequest deleteOwnedProjectRequest) {
        DeleteOwnedProjectRequestValidator.builder().build().validate(deleteOwnedProjectRequest);
        return ok(projectListToGetProjectsResponse(projectService.deleteOwnedProject(deleteOwnedProjectRequest)));
    }





    /////////////////////

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
