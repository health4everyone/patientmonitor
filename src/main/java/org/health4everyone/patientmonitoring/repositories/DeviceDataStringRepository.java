package org.health4everyone.patientmonitoring.repositories;

import java.util.Optional;
import java.util.UUID;

import org.health4everyone.patientmonitoring.models.DeviceDataString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;


public interface DeviceDataStringRepository extends JpaRepository<DeviceDataString, UUID>{
	DeviceDataString save(DeviceDataString deviceDataString);
	Optional<DeviceDataString> findById(UUID id);
}
