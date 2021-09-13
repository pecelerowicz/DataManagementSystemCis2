package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.repository.InfoRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.ProjectRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ProjectService {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

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
            throw new RuntimeException("Project with id " + projectId + " is not owned by the logged in user");
        }
        List<String> infoNameList = project.getInfoList().stream().map(Info::getInfoName).collect(toList());
        if(infoNameList.contains(infoName)) {
            throw new RuntimeException("Project with id " + projectId + " already contains info " + infoName);
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

    @Transactional
    public Project removeInfoFromOwnedProject(RemoveInfoFromOwnedProjectRequest removeInfoFromOwnedProjectRequest) {
        String ownerName = authService.getCurrentUser().getUsername();
        Long projectId = removeInfoFromOwnedProjectRequest.getProjectId();

        String userName = removeInfoFromOwnedProjectRequest.getUsername();
        String infoName = removeInfoFromOwnedProjectRequest.getInfoName();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("No project with id " + projectId));
        if(!project.getOwnerName().equals(ownerName)) {
            throw new RuntimeException("Project with id " + projectId + " is not owned by the logged in user");
        }
        Info info = project.getInfoList().stream()
                .filter(i -> i.getUser().getUsername().equals(userName) && i.getInfoName().equals(infoName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Project with id " + projectId +
                        " does not contain info " + infoName + " of " + userName));
        project.getInfoList().remove(info);
        info.getProjects().remove(project);
        return project;
    }

    @Transactional
    public Project removeUserFromOwnedProject(RemoveUserFromOwnedProjectRequest removeUserFromOwnedProjectRequest) {
        String ownerName = authService.getCurrentUser().getUsername();
        String userName = removeUserFromOwnedProjectRequest.getUserName();
        Long projectId = removeUserFromOwnedProjectRequest.getProjectId();
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("No user " + userName + " found"));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("No project with id " + projectId));
        if(ownerName.equals(userName)) {
            throw new RuntimeException("Cannot remove project owner from the project");
        }
        if(project.getUsers().stream().noneMatch(u -> u.getUsername().equals(userName))) {
            throw new RuntimeException("No user " + userName + " found in the project with id " + projectId);
        }
        if(!project.getOwnerName().equals(ownerName)) {
            throw new RuntimeException("Project with id " + projectId + " is not owned by the logged in user");
        }
        if(project.getInfoList().stream().anyMatch(i -> i.getUser().getUsername().equals(userName))) {
            throw new RuntimeException("Project with id " + projectId + " contains infos by user " + userName);
        }
        project.getUsers().remove(user);
        user.getProjects().remove(project);
        return project;
    }

    public List<Project> deleteOwnedProject(DeleteOwnedProjectRequest deleteOwnedProjectRequest) {
        User owner = authService.getCurrentUser();
        String ownerName = owner.getUsername();
        Long projectId = deleteOwnedProjectRequest.getProjectId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("No project with id " + projectId));
        if(!project.getOwnerName().equals(ownerName)) {
            throw new RuntimeException("Project with id " + projectId + " is not owned by the logged in user");
        }
        if(!project.getInfoList().isEmpty()) {
            throw new RuntimeException("Project with id " + projectId + " contains infos");
        }
        if(project.getUsers().size() > 1) {
            throw new RuntimeException("Project with id " + projectId + " contains member users");
        }
        owner.getProjects().remove(project);
        project.getUsers().remove(owner);
        projectRepository.delete(project);
        return owner.getProjects();
    }






    // poniżej operacje na projektach, których nie jestem właścicielem

    public List<Project> getProjects() {
        return authService.getCurrentUser().getProjects();
    }



}
