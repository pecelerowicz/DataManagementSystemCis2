package gov.ncbj.nomaten.datamanagementbackend.service.support;

import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> getProjectsByUserOwned(User user) {
        return user.getProjects().stream().filter(p -> p.getOwnerName().equals(user.getUsername())).sorted().collect(toList());
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("No project with id " + projectId));
    }

    public List<Project> getProjectsByUserNotOwned(User user) {
        return user.getProjects()
                .stream()
                .filter(p -> !p.getOwnerName().equals(user.getUsername()))
                .sorted()
                .collect(toList());
    }

    public Project createProject(User projectOwner, String projectName, String description) {
        Project project = Project.builder()
                .projectName(projectName)
                .description(description)
                .ownerName(projectOwner.getUsername())
                .localDateTime(LocalDateTime.now().plusHours(2)) // ?
                .build();
        projectOwner.getProjects().add(project);
        return project;
    }

    public Project updateProject(Project project, String newName, String newDescription) {
        project.setProjectName(newName);
        project.setDescription(newDescription);
        return project;
    }

    public Project addUserToProject(Project project, User userToAdd) {
        project.getUsers().add(userToAdd);
        userToAdd.getProjects().add(project);
        return project;
    }

    public Project addInfoToProject(Project project, Info info) {
        project.getInfoList().add(info);
        info.getProjects().add(project);
        return project;
    }

    public Project removeInfoFromProject(Project project, Info info) {
        project.getInfoList().remove(info);
        info.getProjects().remove(project);
        return project;
    }

    public Project removeUserFromProject(Project project, User user) {
        project.getUsers().remove(user);
        user.getProjects().remove(project);
        return project;
    }

    public void deleteProject(Project project) {
        String projectOwnerName = project.getOwnerName();
        User projectOwner = project.getUsers().stream().filter(u -> u.getUsername().equals(projectOwnerName)).findAny().get();
        projectOwner.getProjects().remove(project);
        project.getUsers().remove(projectOwner);
        projectRepository.delete(project);
    }
}
