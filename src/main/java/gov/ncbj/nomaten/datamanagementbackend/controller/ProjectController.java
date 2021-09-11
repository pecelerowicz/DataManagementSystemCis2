package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.project.ProjectMapper.*;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

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
    public ResponseEntity<RemoveMyInfoFromOwnedProjectResponse> removeMyInfoFromOwnedProject(@RequestBody RemoveMyInfoFromOwnedProjectRequest removeMyInfoFromOwnedProjectRequest) {
        // TODO validation
        return ok(projectToRemoveMyInfoFromOwnedProjectResponse(projectService.removeMyInfoFromOwnedProject(removeMyInfoFromOwnedProjectRequest)));
    }

    // TODO removeOtherInfoFromOwnedProject

    // TODO removeUserFromOwnedProject (remove all its infos automatically? Prevent from removing user, who has infos in the project?)

    // TODO deleteOwnedProject




    // TODO getAllProjects  (te, w których jestem)
    @GetMapping("/project/all")
    public ResponseEntity<GetProjectsResponse> getProjects() {
        List<Project> projects = projectService.getProjects();

        return null;
    }

    // TODO addMyInfoToOtherProject

    // TODO removeMyFromOtherProject

    // TODO removeMyInfoFromOtherProject

    // TODO check what happens if I delete Info which belongs to the project (my project, other project, multiple projects)
}
