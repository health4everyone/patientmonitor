package org.health4everyone.patientmonitoring.repositories;

import java.util.Optional;
import java.util.UUID;

import org.health4everyone.patientmonitoring.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
	Device save(Device device);
	Optional<Device> findById(UUID id);
}
