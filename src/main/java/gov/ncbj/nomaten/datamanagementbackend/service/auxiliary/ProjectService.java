package gov.ncbj.nomaten.datamanagementbackend.service.auxiliary;

import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

}
