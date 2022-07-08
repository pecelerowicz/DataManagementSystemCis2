package gov.ncbj.nomaten.datamanagementbackend.service.main;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.repository.ProjectRepository;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.nio.file.FileSystems.getDefault;
import static java.util.stream.Collectors.toList;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

@Service
public class AllProjectsService {

    @Autowired
    AuthService authService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    private StorageService storageService;

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
        return storageService.getFolderStructure(getDefault().getPath(STORAGE, userName, infoName));
    }

    // downloading files is in FolderService
}
