package org.health4everyone.patientmonitoring.repositories;

import java.util.Optional;
import java.util.UUID;

import org.health4everyone.patientmonitoring.models.DeviceDataBinary;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeviceDataBinaryRepository extends JpaRepository<DeviceDataBinary, UUID>{
	DeviceDataBinary save(DeviceDataBinary deviceDataBinary);
	Optional<DeviceDataBinary> findById(UUID id);
}
