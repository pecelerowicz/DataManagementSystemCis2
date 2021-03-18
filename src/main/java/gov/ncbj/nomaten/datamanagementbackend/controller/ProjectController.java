package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/project")
    public ResponseEntity<ProjectsResponse> getProjects() {
        List<ProjectResponse> projectResponseList = projectService.getProjects()
                .stream()
                .map(Project::getName)
                .map(ProjectResponse::new)
                .collect(toList());
        return ResponseEntity.status(OK).body(ProjectsResponse.builder().projectResponseList(projectResponseList).build());
    }

    @GetMapping("/project-all")
    public ResponseEntity<ProjectsResponse> getAllProjects() {
        List<ProjectResponse> projectResponseList = projectService.getAllProjects()
                .stream()
                .map(Project::getName)
                .map(ProjectResponse::new)
                .collect(toList());
        return ResponseEntity.status(OK).body(ProjectsResponse.builder().projectResponseList(projectResponseList).build());
    }

    //TODO nie można zrobić dwóch projektów o tej samej nazwie
    @PostMapping("/project")
    public ResponseEntity<CreateProjectResponse> createProject(@RequestBody CreateProjectRequest createProjectRequest) {
        CreateProjectResponse createProjectResponse = CreateProjectResponse
                .builder()
                .name(projectService.createProject(createProjectRequest.getName(), createProjectRequest.getDescription()))
                .build();
        return ResponseEntity.status(OK).body(createProjectResponse);
    }

    @PostMapping("/project-user")
    public ResponseEntity<AttachUserToProjectResponse> attachUserToProject(@RequestBody AttachUserToProjectRequest attachUserToProjectRequest) {
        projectService.attachUserToProject(attachUserToProjectRequest.getAnotherUserName(), attachUserToProjectRequest.getProjectName());
        AttachUserToProjectResponse attachUserToProjectResponse = AttachUserToProjectResponse
                .builder()
                .anotherUserName(attachUserToProjectRequest.getAnotherUserName())
                .projectName(attachUserToProjectRequest.getProjectName())
                .build();
        return ResponseEntity.status(OK).body(attachUserToProjectResponse);
    }

    @DeleteMapping("/project-user")
    public ResponseEntity<GiveUpProjectResponse> giveUpProject(@RequestBody GiveUpProjectRequest giveUpProjectRequest) {
        projectService.giveUpProject(giveUpProjectRequest.getProjectName());
        GiveUpProjectResponse giveUpProjectResponse = GiveUpProjectResponse.builder()
                .projectName(giveUpProjectRequest.getProjectName())
                .build();
        return ResponseEntity.status(OK).body(giveUpProjectResponse);
    }

}
