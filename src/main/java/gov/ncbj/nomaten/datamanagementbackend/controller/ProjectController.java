package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.service.ProjectService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.InfoMapper.infoToGetInfoResponse;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.project.ProjectMapper.*;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

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
        // TODO validation
        return ok(projectToCreateProjectResponse(projectService.createOwnedProject(createProjectRequest)));
    }

    @PutMapping("/project")
    public ResponseEntity<UpdateProjectResponse> updateOwnedProject(@RequestBody UpdateProjectRequest updateProjectRequest) {
        // TODO validation
        return ok(projectToUpdateProjectResponse(projectService.updateOwnedProject(updateProjectRequest)));
    }

    @PostMapping("/project/user")
    public ResponseEntity<AddUserResponse> addUserToOwnedProject(@RequestBody AddUserRequest addUserRequest) {
        // TODO validation
        return ok(projectToAddUserResponse(projectService.addUserToOwnedProject(addUserRequest)));
    }

    @PostMapping("/project/info")
    public ResponseEntity<AddMyInfoToOwnedProjectResponse> addMyInfoToOwnedProject(@RequestBody AddMyInfoToOwnedProjectRequest addMyInfoToOwnedProjectRequest) {
        // TODO validation
        return ok(projectToAddInfoToOwnedProjectResponse(projectService.addMyInfoToOwnedProject(addMyInfoToOwnedProjectRequest)));
    }

    @DeleteMapping("/project/info")
    public ResponseEntity<RemoveInfoFromOwnedProjectResponse> removeInfoFromOwnedProject(@RequestBody RemoveInfoFromOwnedProjectRequest removeInfoFromOwnedProjectRequest) {
        // TODO validation
        // TODO check if it correctly removes other ppl's Infos from the Project owned by me
        return ok(projectToRemoveInfoFromOwnedProjectResponse(projectService.removeInfoFromOwnedProject(removeInfoFromOwnedProjectRequest)));
    }

    @DeleteMapping("/project/user")
    // TODO check this method
    public ResponseEntity<RemoveUserFromOwnedProjectResponse> removeUserFromOwnedProject(@RequestBody RemoveUserFromOwnedProjectRequest removeUserFromOwnedProjectRequest) {
        // TODO validation
        return ok(projectToRemoveUserFromOwnedProjectResponse(projectService.removeUserFromOwnedProject(removeUserFromOwnedProjectRequest)));
    }

    @DeleteMapping("/project")
    // TODO check this method (there was no transactional and it seemed to have worked correctly nevertheless)
    public ResponseEntity<GetProjectsResponse> deleteOwnedProject(@RequestBody DeleteOwnedProjectRequest deleteOwnedProjectRequest) {
        // TODO validation
        return ok(projectListToGetProjectsResponse(projectService.deleteOwnedProject(deleteOwnedProjectRequest)));
    }

    // OTHER PROJECTS
    @GetMapping("/project/all/{projectId}")
    // TODO check this method
    public ResponseEntity<GetProjectResponse> getProject(@PathVariable Long projectId) {
        // TODO validation
        return ok(projectToGetProjectResponse(projectService.getProject(projectId)));
    }

    @GetMapping("/project/all")
    // TODO check this method
    public ResponseEntity<GetProjectsResponse> getProjects() {
        return ok(projectListToGetProjectsResponse(projectService.getProjects()));
    }

    @PostMapping("/project/all/info")
    public ResponseEntity<AddMyInfoToOtherProjectResponse> addMyInfoToOtherProject(@RequestBody AddMyInfoToOtherProjectRequest addMyInfoToOtherProjectRequest) {
        // TODO validation
        return ok(projectToAddMyInfoToOtherProjectResponse(projectService.addMyInfoToOtherProject(addMyInfoToOtherProjectRequest)));
    }

    @DeleteMapping("/project/all/info")
    // TODO check this method
    public ResponseEntity<RemoveMyInfoFromOtherProjectResponse> removeMyInfoFromOtherProject(@RequestBody RemoveMyInfoFromOtherProjectRequest removeMyInfoFromOtherProjectRequest) {
        // TODO validation
        return ok(projectToRemoveMyInfoFromOtherProjectResponse(projectService.removeMyInfoFromOtherProject(removeMyInfoFromOtherProjectRequest)));
    }

    @DeleteMapping("/project/all/user")
    // TODO check this method
    public ResponseEntity<RemoveMyFromOtherProjectResponse> removeMyFromOtherProject(@RequestBody RemoveMyFromOtherProjectRequest removeMyFromOtherProjectRequest) {
        return ok(projectToRemoveMyFromOtherProjectResponse(projectService.removeMyFromOtherProject(removeMyFromOtherProjectRequest)));
    }

    // TODO check what happens if I delete Info which belongs to the project (my project, other project, multiple projects)

    // PACKAGES IN PROJECTS
    @GetMapping("/project/packages/info/{projectId}/{userName}/{infoName}") // response type is borrowed for now
    public ResponseEntity<GetInfoResponse> getInfoOfUserAndProject(@PathVariable Long projectId, @PathVariable String userName, @PathVariable String infoName) {
        // TODO VALIDATION
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(projectService.getInfoOfUserAndProject(projectId, userName, infoName)));
    }

    @GetMapping("/project/packages/folder/{projectId}/{userName}/{infoName}")
    public PathNode getPackageFolderStructureOfUserAndProject(@PathVariable Long projectId, @PathVariable String userName, @PathVariable String infoName) {
        // TODO VALIDATION
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return projectService.getPackageFolderStructureOfUserAndProject(projectId, userName, infoName);
    }

    // downloading files in FolderController

}
