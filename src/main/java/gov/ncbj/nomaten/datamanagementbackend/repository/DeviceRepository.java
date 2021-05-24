package gov.ncbj.nomaten.datamanagementbackend.repository;

import gov.ncbj.nomaten.datamanagementbackend.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByName(String name);
}
