package gov.ncbj.nomaten.datamanagementbackend.service.auxiliary;

import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;
import static java.nio.file.FileSystems.getDefault;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CheckService {

    private final FolderService folderService;

    public void projectDoesNotContainInfo(Project project, Info info) {
        if(project.getInfoList()
                .stream()
                .anyMatch(i -> i.getInfoName().equals(info.getInfoName()) &&
                        i.getUser().getUsername().equals(info.getUser().getUsername()))) {
            throw new RuntimeException("Project " + project.getId() + " contains info " + info.getInfoName() +
                    " by user " + info.getUser().getUsername());
        }
    }

    public void userOwnsProject(User projectOwner, Project project) {
        if(!project.getOwnerName().equals(projectOwner.getUsername())) {
            throw new RuntimeException("User " + projectOwner.getUsername() +
                    " does not own project " + project.getId());
        }
    }

    public void projectWithThisNameAndUserDoesNotExist(User user, String projectName) {
        if(user.getProjects().stream().map(Project::getProjectName).anyMatch(n -> n.equals(projectName))) {
            throw new RuntimeException("Project with this name already exists for the user " + user.getUsername());
        }
    }

    public void userDoesNotOwnProject(User user, Project project) {
        if(project.getOwnerName().equals(user.getUsername())) {
            throw new RuntimeException("User " + user.getUsername() + " is project owner");
        }
    }

    public void projectDoesNotContainDataByUser(Project project, User user) {
        if(project.getInfoList().stream().anyMatch(i -> i.getUser().getUsername().equals(user.getUsername()))) {
            throw new RuntimeException("Project with id " + project.getId() +
                    " contains data by user " + user.getUsername());
        }
    }

    public void userIsInProject(User user, Project project) {
        if(project.getUsers().stream().map(User::getUsername).noneMatch(n -> n.equals(user.getUsername()))) {
            throw new RuntimeException("User " + user.getUsername() + " is not member of project " + project.getId());
        }
    }

    public void userIsNotInProject(User user, Project project) {
        if(project.getUsers().stream().map(User::getUsername).anyMatch(n -> n.equals(user.getUsername()))) {
            throw new RuntimeException("User " + user.getUsername() + " is already member of project " + project.getId());
        }
    }

    public void projectDoesNotContainData(Project project) {
        if(!project.getInfoList().isEmpty()) {
            throw new RuntimeException("Project " + project.getId() + " contains data");
        }
    }

    public void projectDoesNotContainMembers(Project project) {
        if(project.getUsers().size() > 1) {
            throw new RuntimeException("Project with id " + project.getId() + " contains member users");
        }
    }

    public void infoByUserIsInProject(String infoName, User packageOwner, Project project) {
        if(project.getInfoList().stream().noneMatch(i ->
                i.getInfoName().equals(infoName) && i.getUser().getUsername().equals(packageOwner.getUsername()))) {
            throw new RuntimeException("Project " + project.getId() + " does not contain package " + infoName +
                    " by user " + packageOwner.getUsername());
        }
    }

    public void mainFolderByUserExists(User packageOwner, String infoName) {
        if(!folderService.fileOrFolderExists(getDefault().getPath(STORAGE, packageOwner.getUsername(), infoName))) {
            throw new RuntimeException("Item does not exist");
        }
        if(!folderService.isDirectory(getDefault().getPath(STORAGE, packageOwner.getUsername(), infoName))) {
            throw new RuntimeException("Main folder does not exist");
        }
    }

    public void infoBelongsToUser(User user, Info info) {
        if(!info.getUser().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("The info does not belong to user " + user.getUsername());
        }
    }

    public void infoIsNotInProject(Project project, Info info) {
        if(project.getInfoList().stream()
                .anyMatch(i -> i.getInfoName().equals(info.getInfoName()) &&
                        i.getUser().getUsername().equals(info.getUser().getUsername()))) {
            throw new RuntimeException("The package is already in the project");
        }
    }

    public void infoIsNotInProject(Info info) {
        if(!info.getProjects().isEmpty()) {
            throw new RuntimeException("The info is member of a project");
        }
    }

    public void infoIsInProject(Info info, Project project) {
        if(project.getInfoList().stream().noneMatch(i -> i.getId().equals(info.getId()))) {
            throw new RuntimeException("The package is not in the project");
        }
    }

    public void packageDoesNotExist(User user, String packageName) {
        List<String> metadataNames = user.getInfoList().stream().map(Info::getInfoName).collect(toList());
        List<String> storageNames = folderService.getDirectSubfolders(getDefault().getPath(STORAGE, user.getUsername()));
        if(metadataNames.contains(packageName) || storageNames.contains(packageName)) {
            throw new RuntimeException("Package " + packageName + " already exists");
        }
    }

    public void packageExists(User user, String packageName) {
        List<String> metadataNames = user.getInfoList().stream().map(Info::getInfoName).collect(toList());
        List<String> storageNames = folderService.getDirectSubfolders(getDefault().getPath(STORAGE, user.getUsername()));
        if(!metadataNames.contains(packageName) && !storageNames.contains(packageName)) {
            throw new RuntimeException("Package " + packageName + " does not exists");
        }
    }

    public void folderDoesNotExist(Path path) {
        if(Files.exists(path) && Files.isDirectory(path)) {
            throw new RuntimeException("Folder already exists");
        }
    }

    public void infoIsPublic(Info info) {
        if(!info.getAccess().equals(Info.Access.PUBLIC)) {
            throw new RuntimeException("Item is not public");
        }
    }
}
