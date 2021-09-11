package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.AddMyInfoToOwnedProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.AddUserRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.CreateProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.UpdateProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.repository.InfoRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ProjectService {

    @Autowired
    AuthService authService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    InfoRepository infoRepository;

    public Project getOwnedProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new RuntimeException("No project with id " + projectId));
        if(!project.getOwnerName().equals(authService.getCurrentUser().getUsername())) {
            throw new RuntimeException("Project with id " + projectId + " does not belong to the logged in user");
        }
        return project;
    }

    public List<Project> getOwnedProjects() {
        User user = authService.getCurrentUser();
        return user.getProjects().stream().filter(p -> p.getOwnerName().equals(user.getUsername())).collect(toList());
    }

    @Transactional
    public Project createOwnedProject(CreateProjectRequest createProjectRequest) {
        User user = authService.getCurrentUser();
        List<String> projectNames = user.getProjects().stream().map(Project::getProjectName).collect(toList());
        if(projectNames.contains(createProjectRequest.getProjectName())) {
            throw new RuntimeException("Project with this name already exists for the logged in user");
        }
        Project project = Project.builder()
                .projectName(createProjectRequest.getProjectName())
                .description(createProjectRequest.getDescription())
                .ownerName(user.getUsername())
                .build();
        user.getProjects().add(project);
        return project;
    }

    @Transactional
    public Project updateOwnedProject(UpdateProjectRequest updateProjectRequest) {
        Long projectId = updateProjectRequest.getProjectId();
        String newProjectName = updateProjectRequest.getNewName();
        String newDescription = updateProjectRequest.getNewDescription();
        User user = authService.getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("No project with id " + projectId));
        if(!project.getOwnerName().equals(user.getUsername())) {
            throw new RuntimeException("Project " + projectId + " is not owned by the logged in user");
        }
        project.setProjectName(newProjectName);
        project.setDescription(newDescription);
        return project;
    }

    @Transactional
    public Project addUserToOwnedProject(AddUserRequest addUserRequest) {
        Long projectId = addUserRequest.getProjectId();
        String newUserName = addUserRequest.getUserName();
        User newUser = authService.getUserByName(newUserName);
        User owner = authService.getCurrentUser();
        String ownerName = owner.getUsername();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("There is no project with id " + projectId));
        if(!project.getOwnerName().equals(ownerName)) {
            throw new RuntimeException("Project with id " + projectId + " is not owned by " + ownerName);
        }
        List<String> memberNames = project.getUsers().stream().map(User::getUsername).collect(toList());
        if(memberNames.contains(newUserName)) {
            throw new RuntimeException("Project with id " + projectId + " already contains user " + newUserName);
        }
        project.getUsers().add(newUser);
        newUser.getProjects().add(project);
        return project;
    }

    @Transactional
    public Project addMyInfoToOwnedProject(AddMyInfoToOwnedProjectRequest addMyInfoToOwnedProjectRequest) {
        String ownerName = authService.getCurrentUser().getUsername();
        Long projectId = addMyInfoToOwnedProjectRequest.getProjectId();
        String infoName = addMyInfoToOwnedProjectRequest.getInfoName();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("No project with id " + projectId));
        if(!project.getOwnerName().equals(ownerName)) {
            throw new RuntimeException("Project with id " + projectId + "is not owned by the logged in user");
        }
        Info info = infoRepository.findByUserUsername(ownerName)
                .stream()
                .filter(i -> i.getInfoName().equals(infoName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("User " + ownerName + " does not have info " + infoName));
        project.getInfoList().add(info);
        info.getProjects().add(project);
        return project;
    }




    // poniżej te, których nie jestem właścicielem

    public List<Project> getProjects() {
        return authService.getCurrentUser().getProjects();
    }



}
