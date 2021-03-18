package gov.ncbj.nomaten.datamanagementbackend.repository;

import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAll();
}
