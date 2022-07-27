package gov.ncbj.nomaten.datamanagementbackend.service.main;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.nio.file.FileSystems.getDefault;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

@Service
@AllArgsConstructor
public class AllProjectsService {

    private final AuthService authService;
    private final ProjectService projectService;
    private final FolderService folderService;
    private final InfoService infoService;
    private final CheckService checkService;

    public Project getProject(Long projectId) {
        User currentUser = authService.getCurrentUser();
        Project project = projectService.getProjectById(projectId);
        checkService.userIsInProject(currentUser, project);
        return project;
    }

    public List<Project> getProjects() {
        User currentUser = authService.getCurrentUser();
        return projectService.getProjectsByUserNotOwned(currentUser);
    }

    public List<Info> getInfoList() {
        User currentUser = authService.getCurrentUser();
        return infoService.getInfoList(currentUser);
    }

    @Transactional
    public Project addMyInfoToOtherProject(AddMyInfoToOtherProjectRequest addMyInfoToOtherProjectRequest) {
        User currentUser = authService.getCurrentUser();
        Info info = infoService.getInfo(addMyInfoToOtherProjectRequest.getInfoName(), currentUser);
        Project project = projectService.getProjectById(addMyInfoToOtherProjectRequest.getProjectId());
        checkService.userIsInProject(currentUser, project);
        checkService.userDoesNotOwnProject(currentUser, project);
        checkService.infoBelongsToUser(currentUser, info);
        checkService.infoIsNotInProject(project, info);
        return projectService.addInfoToProject(project, info);
    }

    @Transactional
    public Project removeMyInfoFromOtherProject(RemoveMyInfoFromOtherProjectRequest removeMyInfoFromOtherProjectRequest) {
        User currentUser = authService.getCurrentUser();
        Info info = infoService.getInfo(removeMyInfoFromOtherProjectRequest.getInfoName(), currentUser);
        Project project = projectService.getProjectById(removeMyInfoFromOtherProjectRequest.getProjectId());
        checkService.userDoesNotOwnProject(currentUser, project);
        checkService.userIsInProject(currentUser, project);
        checkService.infoIsInProject(info, project);
        return projectService.removeInfoFromProject(project, info);
    }

    // PACKAGES IN PROJECT
    public Info getInfoOfUserAndProject(Long projectId, String userName, String infoName) {
        User currentUser = authService.getCurrentUser();
        User packageOwner = authService.getUserByName(userName);
        Info info = infoService.getInfo(infoName, packageOwner);
        Project project = projectService.getProjectById(projectId);
        checkService.userIsInProject(currentUser, project);
        checkService.userIsInProject(packageOwner, project);
        checkService.infoIsInProject(info, project);
        return info;
    }

    public PathNode getPackageFolderStructureOfUserAndProject(Long projectId, String userName, String infoName) {
        User currentUser = authService.getCurrentUser();
        User packageOwner = authService.getUserByName(userName);
        Info info = infoService.getInfo(infoName, packageOwner);
        Project project = projectService.getProjectById(projectId);
        checkService.userIsInProject(currentUser, project);
        checkService.userIsInProject(packageOwner, project);
        checkService.infoIsInProject(info, project);
        checkService.infoBelongsToUser(packageOwner, info);
        // todo something like: does this info have a package?
        return folderService.getFolderStructure(getDefault().getPath(STORAGE, userName, infoName));
    }

    public Resource downloadFileOfProject(Long projectId, String userName, String infoName, String fileNameWithPath) {
        User currentUser = authService.getCurrentUser();
        User packageOwner = authService.getUserByName(userName);
        Info info = infoService.getInfo(infoName, packageOwner);
        Project project = projectService.getProjectById(projectId);
        checkService.userIsInProject(currentUser, project);
        checkService.userIsInProject(packageOwner, project);
        checkService.infoIsInProject(info, project);
        checkService.infoBelongsToUser(packageOwner, info); // redundant
        // todo something like: does this info have a package/
        // todo something like: doesTheFileExist (and is it a file not a folder?)
        return folderService.downloadFile(infoName, userName, fileNameWithPath);
    }
}
