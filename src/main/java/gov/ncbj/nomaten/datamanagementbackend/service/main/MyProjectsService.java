package gov.ncbj.nomaten.datamanagementbackend.service.main;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.repository.ProjectRepository;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;
import static java.nio.file.FileSystems.getDefault;

@Service
@AllArgsConstructor
public class MyProjectsService {

    private final ProjectRepository projectRepository;

    private final ProjectService projectService;

    private final AuthService authService;

    private final FolderService folderService;

    private final InfoService infoService;

    private final CheckService checkService;

    public Project getOwnedProject(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        User projectOwner = authService.getCurrentUser();
        checkService.userOwnsProject(projectOwner, project);
        return project;
    }

    public List<Project> getOwnedProjects() {
        User projectOwner = authService.getCurrentUser();
        return projectService.getProjectsByUserOwned(projectOwner);
    }

    @Transactional
    public Project createOwnedProject(CreateProjectRequest createProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        checkService.projectWithThisNameAndUserDoesNotExist(projectOwner, createProjectRequest.getProjectName());

        Project project = Project.builder()
                .projectName(createProjectRequest.getProjectName())
                .description(createProjectRequest.getDescription())
                .ownerName(projectOwner.getUsername())
                .localDateTime(LocalDateTime.now().plusHours(2)) // ?
                .build();
        projectOwner.getProjects().add(project);
        return project;
    }

    @Transactional
    public Project updateOwnedProject(UpdateProjectRequest updateProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        Project project = projectService.getProjectById(updateProjectRequest.getProjectId());

        checkService.userOwnsProject(projectOwner, project);

        project.setProjectName(updateProjectRequest.getNewName());
        project.setDescription(updateProjectRequest.getNewDescription());
        return project;
    }

    @Transactional
    public Project addUserToOwnedProject(AddUserRequest addUserRequest) {
        User userToAdd = authService.getUserByName(addUserRequest.getUserName());
        User projectOwner = authService.getCurrentUser();
        Project project = projectService.getProjectById(addUserRequest.getProjectId());

        checkService.userOwnsProject(projectOwner, project);
        checkService.userDoesNotOwnProject(userToAdd, project);
        checkService.userIsNotInProject(userToAdd, project);

        project.getUsers().add(userToAdd);
        userToAdd.getProjects().add(project);
        return project;
    }

    public List<Info> getInfoList() {
        User user = authService.getCurrentUser();
        return infoService.getInfoList(user);
    }

    @Transactional
    public Project addMyInfoToMyProject(AddMyInfoToOwnedProjectRequest addMyInfoToOwnedProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        Project project = projectService.getProjectById(addMyInfoToOwnedProjectRequest.getProjectId());
        Info info = infoService.getInfo(addMyInfoToOwnedProjectRequest.getInfoName(), projectOwner);

        checkService.userOwnsProject(projectOwner, project);
        checkService.projectDoesNotContainInfo(project, info);

        project.getInfoList().add(info);
        info.getProjects().add(project);
        return project;
    }

    @Transactional
    public Project removeInfoFromMyProject(RemoveInfoFromOwnedProjectRequest removeInfoFromOwnedProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        User packageOwner = authService.getUserByName(removeInfoFromOwnedProjectRequest.getUsername());
        Project project = projectService.getProjectById(removeInfoFromOwnedProjectRequest.getProjectId());
        checkService.userOwnsProject(projectOwner, project);
        checkService.userIsInProject(projectOwner, project);
        checkService.userIsInProject(packageOwner, project);
        Info info = getInfoInMyProject(project.getId(),
                removeInfoFromOwnedProjectRequest.getInfoName(), packageOwner.getUsername());

        project.getInfoList().remove(info);
        info.getProjects().remove(project);
        return project;
    }

    @Transactional
    public Project removeUserFromMyProject(RemoveUserFromOwnedProjectRequest removeUserFromOwnedProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        User userToRemove = authService.getUserByName(removeUserFromOwnedProjectRequest.getUserName());
        Project project = projectService.getProjectById(removeUserFromOwnedProjectRequest.getProjectId());

        checkService.userOwnsProject(projectOwner, project);
        checkService.userIsInProject(projectOwner, project);
        checkService.userIsInProject(userToRemove, project);
        checkService.userDoesNotOwnProject(userToRemove, project);
        checkService.projectDoesNotContainDataByUser(project, userToRemove);

        project.getUsers().remove(userToRemove);
        userToRemove.getProjects().remove(project);
        return project;
    }

    @Transactional
    public List<Project> deleteMyProject(DeleteOwnedProjectRequest deleteOwnedProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        Long projectId = deleteOwnedProjectRequest.getProjectId();
        Project project = projectService.getProjectById(projectId);

        checkService.userOwnsProject(projectOwner, project);
        checkService.projectDoesNotContainData(project);
        checkService.projectDoesNotContainMembers(project);

        projectOwner.getProjects().remove(project);
        project.getUsers().remove(projectOwner);
        projectRepository.delete(project);
        return projectOwner.getProjects();
    }

    public Info getInfoInMyProject(Long projectId, String infoName, String userName) {
        User projectOwner = authService.getCurrentUser();
        Project project = projectService.getProjectById(projectId);
        checkService.userOwnsProject(projectOwner, project);
        return project.getInfoList()
                .stream()
                .filter(i -> i.getInfoName().equals(infoName) && i.getUser().getUsername().equals(userName))
                .findAny()
                .orElseThrow(() -> new RuntimeException(
                        "No info " + infoName + " of user " + userName + " in your project with id " + projectId));
    }

    public PathNode getPackageFolderStructure(Long projectId, String userName, String infoName) {
        User projectOwner = authService.getCurrentUser();
        User packageOwner = authService.getUserByName(userName);
        Project project = projectService.getProjectById(projectId);

        checkService.userOwnsProject(projectOwner, project);
        checkService.userIsInProject(projectOwner, project);
        checkService.userIsInProject(packageOwner, project);
        checkService.packageByUserIsInProject(infoName, packageOwner, project);
        checkService.mainFolderByUserExists(packageOwner, infoName);
        return folderService.getFolderStructure(getDefault().getPath(STORAGE, userName, infoName));
    }

    public Resource downloadFileOfProject(Long projectId, String userName, String infoName, String fileNameWithPath) {
        Project project = projectService.getProjectById(projectId);
        User projectOwner = authService.getCurrentUser();
        User packageOwner = authService.getUserByName(userName);

        checkService.userOwnsProject(projectOwner, project);
        checkService.userIsInProject(projectOwner, project);
        checkService.userIsInProject(packageOwner, project);
        checkService.packageByUserIsInProject(infoName, packageOwner, project);
        // todo something like: doesTheFileExist (and is it a file not a folder?)
        return folderService.downloadFile(infoName, userName, fileNameWithPath);
    }

}
