package gov.ncbj.nomaten.datamanagementbackend.service.action;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.service.support.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;
import static java.nio.file.FileSystems.getDefault;

@Service
@AllArgsConstructor
public class MyProjectsService {

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

    public Project createOwnedProject(CreateProjectRequest createProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        checkService.projectWithThisNameAndUserDoesNotExist(projectOwner, createProjectRequest.getProjectName());
        return projectService.createProject(projectOwner, createProjectRequest.getProjectName(), createProjectRequest.getDescription());
    }

    public Project updateOwnedProject(UpdateProjectRequest updateProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        Project project = projectService.getProjectById(updateProjectRequest.getProjectId());
        checkService.userOwnsProject(projectOwner, project);
        return projectService.updateProject(project, updateProjectRequest.getNewName(), updateProjectRequest.getNewDescription());
    }

    public Project addUserToOwnedProject(AddUserRequest addUserRequest) {
        User userToAdd = authService.getUserByName(addUserRequest.getUserName());
        User projectOwner = authService.getCurrentUser();
        Project project = projectService.getProjectById(addUserRequest.getProjectId());
        checkService.userOwnsProject(projectOwner, project);
        checkService.userDoesNotOwnProject(userToAdd, project);
        checkService.userIsNotInProject(userToAdd, project);
        return projectService.addUserToProject(project, userToAdd);
    }

    public List<Info> getInfoList() {
        User user = authService.getCurrentUser();
        return infoService.getInfoList(user);
    }

    public Project addMyInfoToMyProject(AddMyInfoToOwnedProjectRequest addMyInfoToOwnedProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        Project project = projectService.getProjectById(addMyInfoToOwnedProjectRequest.getProjectId());
        Info info = infoService.getInfo(addMyInfoToOwnedProjectRequest.getInfoName(), projectOwner);
        checkService.userOwnsProject(projectOwner, project);
        checkService.projectDoesNotContainInfo(project, info);
        return projectService.addInfoToProject(project, info);
    }

    public Project removeInfoFromMyProject(RemoveInfoFromOwnedProjectRequest removeInfoFromOwnedProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        User packageOwner = authService.getUserByName(removeInfoFromOwnedProjectRequest.getUsername());
        Project project = projectService.getProjectById(removeInfoFromOwnedProjectRequest.getProjectId());
        Info info = infoService.getInfo(removeInfoFromOwnedProjectRequest.getInfoName(), packageOwner);
        checkService.userOwnsProject(projectOwner, project);
        checkService.userIsInProject(projectOwner, project);
        checkService.userIsInProject(packageOwner, project);
        checkService.infoIsInProject(info, project);
        checkService.infoBelongsToUser(packageOwner, info);
        return projectService.removeInfoFromProject(project, info);
    }

    public Project removeUserFromMyProject(RemoveUserFromOwnedProjectRequest removeUserFromOwnedProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        User userToRemove = authService.getUserByName(removeUserFromOwnedProjectRequest.getUserName());
        Project project = projectService.getProjectById(removeUserFromOwnedProjectRequest.getProjectId());
        checkService.userOwnsProject(projectOwner, project);
        checkService.userIsInProject(projectOwner, project);
        checkService.userIsInProject(userToRemove, project);
        checkService.userDoesNotOwnProject(userToRemove, project);
        checkService.projectDoesNotContainDataByUser(project, userToRemove);
        return projectService.removeUserFromProject(project, userToRemove);
    }

    public List<Project> deleteMyProject(DeleteOwnedProjectRequest deleteOwnedProjectRequest) {
        User projectOwner = authService.getCurrentUser();
        Project project = projectService.getProjectById(deleteOwnedProjectRequest.getProjectId());
        checkService.userOwnsProject(projectOwner, project);
        checkService.userIsInProject(projectOwner, project);
        checkService.projectDoesNotContainData(project);
        checkService.projectDoesNotContainMembers(project);
        projectService.deleteProject(project);
        return projectOwner.getProjects();
    }

    public Info getInfoInMyProject(Long projectId, String infoName, String userName) {
        User projectOwner = authService.getCurrentUser();
        User packageOwner = authService.getUserByName(userName);
        Project project = projectService.getProjectById(projectId);
        Info info = infoService.getInfo(infoName, packageOwner);
        checkService.userOwnsProject(projectOwner, project);
        checkService.userIsInProject(projectOwner, project);
        checkService.userIsInProject(packageOwner, project);
        checkService.infoIsInProject(info, project);
        return info;
    }

    public PathNode getPackageFolderStructure(Long projectId, String userName, String infoName) {
        User projectOwner = authService.getCurrentUser();
        User packageOwner = authService.getUserByName(userName);
        Project project = projectService.getProjectById(projectId);
        checkService.userOwnsProject(projectOwner, project);
        checkService.userIsInProject(projectOwner, project);
        checkService.userIsInProject(packageOwner, project);
        checkService.infoByUserIsInProject(infoName, packageOwner, project); // todo probably remove
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
        checkService.infoByUserIsInProject(infoName, packageOwner, project); // todo probably remove
        // todo something like: doesTheFileExist (and is it a file not a folder?)
        return folderService.downloadFile(infoName, userName, fileNameWithPath);
    }

}
