package gov.ncbj.nomaten.datamanagementbackend.repository;

import gov.ncbj.nomaten.datamanagementbackend.model.DataSet;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSetRepository extends JpaRepository<DataSet, Long> {

//    @Query("select d from DataSet d where user=?1")
//    @Query("select d from DataSet d where user = :user")
    List<DataSet> findByUser(User user);

    List<DataSet> findByUser(User user, Pageable pageable);
}
