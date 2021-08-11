package gov.ncbj.nomaten.datamanagementbackend.repository;

import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoRepository extends JpaRepository<Info, Long> {
    List<Info> findAll();
    List<Info> findByUser(User user);
    List<Info> findByUserUsername(String username);
}
