package org.health4everyone.patientmonitoring.repositories;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import org.health4everyone.patientmonitoring.models.DeviceDataNumeric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface DeviceDataNumericRepository extends JpaRepository<DeviceDataNumeric, UUID>{
	DeviceDataNumeric save(DeviceDataNumeric deviceDataNumeric);
	Optional<DeviceDataNumeric> findById(UUID id);

	@Query("SELECT COUNT(dd.id) FROM DeviceDataNumeric dd where DATE(dd.timestamp)=DATE(?1)")
			//+ "YEAR(dd.timestamp)=YEAR(?1) and MONTH(dd.timestamp)=YEAR(?1) and DATE(dd.timestamp)=DAY(?1)")
    long countGroupByDate(Date date);
}
