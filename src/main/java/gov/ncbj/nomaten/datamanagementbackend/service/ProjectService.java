package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.repository.InfoRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.ProjectRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.StorageRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.nio.file.FileSystems.getDefault;
import static java.util.stream.Collectors.toList;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

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

    @Autowired
    StorageRepository storageRepository;

    // OWNED PROJECTS
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
        return user.getProjects()
                .stream()
                .filter(p -> p.getOwnerName().equals(user.getUsername()))
                .sorted()
                .collect(toList());
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
                .localDateTime(LocalDateTime.now().plusHours(2)) // ?
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

    @Transactional // to debug! (?)
    public Project addMyInfoToOwnedProject(AddMyInfoToOwnedProjectRequest addMyInfoToOwnedProjectRequest) {
        String ownerName = authService.getCurrentUser().getUsername();
        Long projectId = addMyInfoToOwnedProjectRequest.getProjectId();
        String infoName = addMyInfoToOwnedProjectRequest.getInfoName();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("No project with id " + projectId));
        if(!project.getOwnerName().equals(ownerName)) {
            throw new RuntimeException("Project with id " + projectId + " is not owned by the logged in user");
        }
        if(project.getInfoList().stream().anyMatch(i -> i.getInfoName().equals(infoName) && i.getUser().getUsername().equals(ownerName))) {
            throw new RuntimeException("Project with id " + projectId + " already contains info " + infoName + " of user " + ownerName);
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
            throw new RuntimeException("Project with id " + projectId + " contains data by user " + userName);
        }
        project.getUsers().remove(user);
        user.getProjects().remove(project);
        return project;
    }

    @Transactional
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
            throw new RuntimeException("Project with id " + projectId + " contains data");
        }
        if(project.getUsers().size() > 1) {
            throw new RuntimeException("Project with id " + projectId + " contains member users");
        }
        owner.getProjects().remove(project);
        project.getUsers().remove(owner);
        projectRepository.delete(project);
        return owner.getProjects();
    }

    // OTHER PROJECTS
    public Project getProject(Long projectId) {
        String userName = authService.getCurrentUser().getUsername();
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new RuntimeException("No project with id " + projectId));
        if(project.getUsers().stream().noneMatch(u -> u.getUsername().equals(userName))) {
            throw new RuntimeException("Project with id " + projectId + " does not contain user " + userName);
        }
        return project;
    }

    public List<Project> getProjects() {
        User user = authService.getCurrentUser();
        return user.getProjects()
                .stream()
                .filter(p -> !p.getOwnerName().equals(user.getUsername()))
                .sorted()
                .collect(toList());
    }

    @Transactional
    public Project addMyInfoToOtherProject(AddMyInfoToOtherProjectRequest addMyInfoToOtherProjectRequest) {
        User user = authService.getCurrentUser();
        String userName = user.getUsername();
        String infoName = addMyInfoToOtherProjectRequest.getInfoName();
        Long projectId = addMyInfoToOtherProjectRequest.getProjectId();
        Info info = user.getInfoList().stream().filter(i -> i.getInfoName().equals(infoName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User " + userName + " does not have info " + infoName));
        Project project = user.getProjects().stream().filter(p -> p.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User " + userName + " does not have project with id " + projectId));
        if(project.getOwnerName().equals(userName)) {
            throw new RuntimeException("Project with id " + projectId + " is owned by user " + userName);
        }
        if(project.getInfoList().stream().anyMatch(i -> i.getInfoName().equals(infoName) && i.getUser().getUsername().equals(userName))) {
            throw new RuntimeException("Project with id " + projectId + " already has info " + infoName + " owned by user " + userName);
        }
        info.getProjects().add(project);
        project.getInfoList().add(info);
        return project;
    }

    @Transactional
    public Project removeMyInfoFromOtherProject(RemoveMyInfoFromOtherProjectRequest removeMyInfoFromOtherProjectRequest) {
        User user = authService.getCurrentUser();
        String userName = user.getUsername();
        String infoName = removeMyInfoFromOtherProjectRequest.getInfoName();
        Long projectId = removeMyInfoFromOtherProjectRequest.getProjectId();
        Info info = findInfoOfUser(infoName, user);
        Project project = findMemberProjectOfUser(projectId, user);
        if(project.getOwnerName().equals(userName)) {
            throw new RuntimeException("Project with id " + projectId + " is owned by user " + userName);
        }
        if(project.getInfoList().stream().noneMatch(i -> i.getInfoName().equals(infoName) && i.getUser().getUsername().equals(userName))) {
            throw new RuntimeException("Project with id " + projectId + " does not have info " + infoName + " owned by user " + userName);
        }
        info.getProjects().remove(project);
        project.getInfoList().remove(info);
        return project;
    }

    @Transactional
    public Project removeMyFromOtherProject(RemoveMyFromOtherProjectRequest removeMyFromOtherProjectRequest) {
        User user = authService.getCurrentUser();
        String userName = user.getUsername();
        Long projectId = removeMyFromOtherProjectRequest.getProjectId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("No project with id " + projectId));
        if(project.getOwnerName().equals(user.getUsername())) {
            throw new RuntimeException("Cannot remove user from its own project");
        }
        if(project.getUsers().stream().noneMatch(u -> u.getUsername().equals(userName))) {
            throw new RuntimeException("Logged in user is not in project with id " + projectId);
        }
        if(project.getInfoList().stream().anyMatch(i -> i.getUser().getUsername().equals(userName))) {
            throw new RuntimeException("Project with id " + projectId + " contains infos of user " + userName);
        }
        project.getUsers().remove(user);
        user.getProjects().remove(project);
        return project;
    }


    private Info findInfoOfUser(String infoName, User user) {
        return user.getInfoList().stream().filter(i -> i.getInfoName().equals(infoName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User " + user.getUsername() + " does not have info " + infoName));
    }

    private Project findMemberProjectOfUser(Long projectId, User user) {
        return user.getProjects().stream().filter(p -> p.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User " + user.getUsername() + " does not have project with id " + projectId));
    }

    // PACKAGES IN PROJECT
    public Info getInfoOfUserAndProject(Long projectId, String userName, String infoName) {
        User currentUser = authService.getCurrentUser();
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new RuntimeException("No project with id " + projectId));
        if(project.getUsers().stream().noneMatch(u -> u.getUsername().equals(currentUser.getUsername()))) {
            throw new RuntimeException("Currently logged in user is not in the project with id " + projectId);
        }
        return project.getInfoList()
                .stream()
                .filter(i -> i.getInfoName().equals(infoName) && i.getUser().getUsername().equals(userName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No info " + infoName + " of user " + userName + " in the project with id " + projectId));
    }

    public PathNode getPackageFolderStructureOfUserAndProject(Long projectId, String userName, String infoName) {
        User currentUser = authService.getCurrentUser();
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new RuntimeException("No project with id " + projectId));
        if (project.getUsers().stream().noneMatch(u -> u.getUsername().equals(currentUser.getUsername()))) {
            throw new RuntimeException("Currently logged in user is not in the project with id " + projectId);
        }
        if (project.getInfoList().stream().noneMatch(i -> i.getInfoName().equals(infoName) && i.getUser().getUsername().equals(userName))) {
            throw new RuntimeException("No info " + infoName + " of user " + userName + " in the project with id " + projectId);
        }
        System.out.println("case 5");
        return storageRepository.getFolderStructure(getDefault().getPath(STORAGE, userName, infoName));
    }

    // downloading files is in FolderService
}
