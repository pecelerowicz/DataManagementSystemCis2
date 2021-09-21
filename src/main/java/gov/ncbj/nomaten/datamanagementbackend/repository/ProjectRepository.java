package gov.ncbj.nomaten.datamanagementbackend.repository;

import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAll();
    Optional<Project> findById(Long id);
    List<Project> findByownerName(String ownerName);
}
