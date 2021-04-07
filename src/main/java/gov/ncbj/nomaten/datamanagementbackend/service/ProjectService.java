package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    AuthService authService;

    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjects() {
        User user = authService.getCurrentUser();
        return user.getProjects();
    }

    // TODO to chyba powinno zwracać Project.
    @Transactional
    public String createProject(String name, String description) {
        User user = authService.getCurrentUser();
        Project project = Project.builder().name(name).description(description).build();
        project.addUser(user);
        user.getProjects().add(project);
        return project.getName();
    }

    @Transactional
    public void attachUserToProject(String anotherUserName, String projectName) {
        User currentUser = authService.getCurrentUser();
        User anotherUser = authService.getUserByName(anotherUserName);
        Project project = currentUser.getProjects()
                .stream()
                .filter(p -> projectName.equals(p.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No project " + projectName + " found!"));
        project.addUser(anotherUser);
        anotherUser.getProjects().add(project);
    }

    @Transactional
    public void giveUpProject(String projectName) {
        User user = authService.getCurrentUser();
        Project project = user.getProjects()
                .stream()
                .filter(p -> projectName.equals(p.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No project " + projectName + " found!"));
        user.getProjects().remove(project);
        project.getUsers().remove(user);
    }

}
