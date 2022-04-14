package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.service.ProjectService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_project.*;
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


    // OTHER PROJECTS
    @GetMapping("/project/all/{projectId}")
    // TODO check this method
    public ResponseEntity<GetProjectResponse> getProject(@PathVariable Long projectId) {
        // TODO idValidation (?)
        return ok(projectToGetProjectResponse(projectService.getProject(projectId)));
    }

    @GetMapping("/project/all")
    // TODO check this method
    public ResponseEntity<GetProjectsResponse> getProjects() {
        return ok(projectListToGetProjectsResponse(projectService.getProjects()));
    }

    @PostMapping("/project/all/info")
    public ResponseEntity<AddMyInfoToOtherProjectResponse> addMyInfoToOtherProject(@RequestBody AddMyInfoToOtherProjectRequest addMyInfoToOtherProjectRequest) {
        AddMyInfoToOtherProjectRequestValidator.builder().build().validate(addMyInfoToOtherProjectRequest);
        return ok(projectToAddMyInfoToOtherProjectResponse(projectService.addMyInfoToOtherProject(addMyInfoToOtherProjectRequest)));
    }

    @DeleteMapping("/project/all/info")
    // TODO check this method
    public ResponseEntity<RemoveMyInfoFromOtherProjectResponse> removeMyInfoFromOtherProject(@RequestBody RemoveMyInfoFromOtherProjectRequest removeMyInfoFromOtherProjectRequest) {
        RemoveMyInfoFromOtherProjectRequestValidator.builder().build().validate(removeMyInfoFromOtherProjectRequest);
        return ok(projectToRemoveMyInfoFromOtherProjectResponse(projectService.removeMyInfoFromOtherProject(removeMyInfoFromOtherProjectRequest)));
    }

    @DeleteMapping("/project/all/user")
    // TODO check this method
    public ResponseEntity<RemoveMyFromOtherProjectResponse> removeMyFromOtherProject(@RequestBody RemoveMyFromOtherProjectRequest removeMyFromOtherProjectRequest) {
        RemoveMyFromOtherProjectRequestValidator.builder().build().validate(removeMyFromOtherProjectRequest);
        return ok(projectToRemoveMyFromOtherProjectResponse(projectService.removeMyFromOtherProject(removeMyFromOtherProjectRequest)));
    }

    // TODO check what happens if I delete Info which belongs to the project (my project, other project, multiple projects)

    // PACKAGES IN PROJECTS
    @GetMapping("/project/packages/info/{projectId}/{userName}/{infoName}") // response type is borrowed for now
    public ResponseEntity<GetInfoResponse> getInfoOfUserAndProject(@PathVariable Long projectId, @PathVariable String userName, @PathVariable String infoName) {
        // TODO id validation (?)
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(projectService.getInfoOfUserAndProject(projectId, userName, infoName)));
    }

    @GetMapping("/project/packages/folder/{projectId}/{userName}/{infoName}")
    public PathNode getPackageFolderStructureOfUserAndProject(@PathVariable Long projectId, @PathVariable String userName, @PathVariable String infoName) {
        // TODO id validation (?)
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return projectService.getPackageFolderStructureOfUserAndProject(projectId, userName, infoName);
    }


}
