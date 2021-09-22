package org.health4everyone.patientmonitoring.repositories;

import java.util.UUID;

import org.health4everyone.patientmonitoring.models.Device;
import org.health4everyone.patientmonitoring.models.DeviceDataKey;
import org.springframework.data.repository.Repository;


public interface DeviceDataKeyRepository extends Repository<DeviceDataKey, UUID>{
	DeviceDataKey save(DeviceDataKey deviceDataKey);
	DeviceDataKey findById(UUID id);
	DeviceDataKey findByDeviceAndKey(Device device,String key);
}
